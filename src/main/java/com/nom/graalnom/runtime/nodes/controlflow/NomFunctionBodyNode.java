/*
 * Copyright (c) 2012, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.nom.graalnom.runtime.nodes.controlflow;

import com.nom.graalnom.runtime.builtins.NomBuiltinNode;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.nodes.*;
import com.nom.graalnom.runtime.datatypes.NomNull;
import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.NomInvokeNode;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.dsl.GenerateInline;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import org.graalvm.collections.Pair;

import java.util.ArrayList;

/**
 * The body of a user-defined Nom function. This is the node referenced by a {@link NomRootNode} for
 * user-defined functions. It handles the return value of a function: the {@link NomReturnNode return
 * statement} with the return value. This node catches
 * the exception. If the method ends without an explicit {@code return}, return the
 * {@link NomNull#SINGLETON default null value}.
 */
@NodeInfo(shortName = "body")
@GenerateInline
public final class NomFunctionBodyNode extends NomExpressionNode {

    /**
     * The body of the function.
     */
    @Node.Children
    public NomBasicBlockNode[] bodyNodes;

    public int regCount;

    public NomFunctionBodyNode(NomBasicBlockNode[] bodyNodes, int regCount) {
        this.bodyNodes = bodyNodes;
        this.regCount = regCount;
        for (NomBasicBlockNode bodyNode : bodyNodes) {
            bodyNode.mergeEndOfBlock();
            bodyNode.optimiseFCP();
            bodyNode.optimiseTail();
        }
    }

    public static int depth = -1;
    public static Object[][] argsMap = new Object[256][];
    public static Object[][] regsMap = new Object[256][];

    public static Object[] getArgs() {
        return argsMap[depth];
    }

    public static Object[] getRegs() {
        return regsMap[depth];
    }

    public static void enterScope(int regSize, Object[] args) {
        depth++;
        if (regsMap.length <= depth) {
            Object[][] newRegsMap = new Object[depth * 2][];
            System.arraycopy(regsMap, 0, newRegsMap, 0, depth);
            regsMap = newRegsMap;
        }
        if (regsMap[depth] == null || regsMap[depth].length < regSize) {
            Object[] newRegs = new Object[regSize];
            regsMap[depth] = newRegs;
        }
        if (argsMap.length <= depth) {
            Object[][] newArgsMap = new Object[depth * 2][];
            System.arraycopy(argsMap, 0, newArgsMap, 0, depth);
            argsMap = newArgsMap;
        }
        argsMap[depth] = args;
    }

    public record TailResult(NomFunctionBodyNode functionBodyNode, Object[] args) {

    }

    public static void leaveScope() {
        depth--;
    }

    @Override
    @ExplodeLoop(kind = ExplodeLoop.LoopExplosionKind.FULL_UNROLL_UNTIL_RETURN)
    public Object executeGeneric(VirtualFrame frame) {
        int curIndex = 0;
        /* Execute the function body. */
        while (true) {
            NomBasicBlockNode block = bodyNodes[curIndex];
            block.executeVoid(frame);
            NomEndOfBasicBlockNode stmt = block.getTerminatingNode();
            switch (stmt) {
                case NomBranchNode br -> {
                    for (NomStatementNode mapStmt : br.mappings) {
                        mapStmt.executeVoid(frame);
                    }
                    curIndex = br.getSuccessor();
                }
                case NomIfNode condBr -> {
                    if (condBr.cond(frame)) {
                        for (NomStatementNode mapStmt : condBr.trueBranch.mappings) {
                            mapStmt.executeVoid(frame);
                        }
                        curIndex = condBr.getTrueSuccessor();
                    } else {
                        for (NomStatementNode mapStmt : condBr.falseBranch.mappings) {
                            mapStmt.executeVoid(frame);
                        }
                        curIndex = condBr.getFalseSuccessor();
                    }
                }
                case NomReturnNode ret -> {
                    if (ret.valueNode == null) return NomNull.SINGLETON;
                    //noinspection rawtypes
                    if (ret.valueNode instanceof NomInvokeNode invokeNode) {
                        NomFunction func;
                        Object[] args = invokeNode.getArgumentValues(frame);
                        func = invokeNode.getFunction(args);
                        NomExpressionNode body =  func.rootNode.getBodyNode();

                        if (body instanceof NomBuiltinNode builtinNode) {
                            enterScope(0, args);
                            Object val = builtinNode.executeGeneric(frame);
                            leaveScope();
                            return val;
                        }

//                        System.out.println("tail call: " + func.getName());
                        //begin tail call
                        NomFunctionBodyNode functionBodyNode = (NomFunctionBodyNode) body;
                        return new TailResult(functionBodyNode, args);
                    }
                    return ret.valueNode.executeGeneric(frame);
                }
                case null, default -> throw new RuntimeException("Invalid terminating node");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (NomBasicBlockNode node : bodyNodes) {
            ret.append("\n\t").append(node.toString()).append("\n");
        }
        return ret.toString();
    }
}