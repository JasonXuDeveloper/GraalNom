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
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import org.graalvm.collections.Pair;

import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * The body of a user-defined Nom function. This is the node referenced by a {@link NomRootNode} for
 * user-defined functions. It handles the return value of a function: the {@link NomReturnNode return
 * statement} with the return value. This node catches
 * the exception. If the method ends without an explicit {@code return}, return the
 * {@link NomNull#SINGLETON default null value}.
 */
@NodeInfo(shortName = "body")
public final class NomFunctionBodyNode extends NomExpressionNode {

    /**
     * The body of the function.
     */
    @Node.Children
    public NomBasicBlockNode[] bodyNodes;

    private int curIndex;

    public NomFunctionBodyNode(NomBasicBlockNode[] bodyNodes) {
        this.bodyNodes = bodyNodes;
        for (NomBasicBlockNode bodyNode : bodyNodes) {
            bodyNode.mergeEndOfBlock();
        }
    }

    private static int depth;
    private static final Map<Integer, Object[]> argsMap = new java.util.HashMap<>();

    public static Object[] getArgs() {
        return argsMap.get(depth);
    }

    public static void putArgs(Object[] args) {
        argsMap.put(++depth, args);
    }

    public static void leaveScope() {
        depth--;
    }

    @Override
    @ExplodeLoop(kind = ExplodeLoop.LoopExplosionKind.FULL_UNROLL_UNTIL_RETURN)
    public Object executeGeneric(VirtualFrame frame) {
        curIndex = 0;
        /* Execute the function body. */
        while (true) {
            if (curIndex >= bodyNodes.length) {
                throw new RuntimeException("Function body has no return statement");
            }
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
                        NomFunction func = invokeNode.getFunction();
                        NomExpressionNode body = null;
                        if (func != null) {//non-interface method
                            RootCallTarget callTarget = func.getCallTarget();
                            NomRootNode rootNode = (NomRootNode) callTarget.getRootNode();
                            body = rootNode.getBodyNode();
                            if (body instanceof NomBuiltinNode) {
                                invokeNode.executeGeneric(frame);
                            }
                        }

                        //interface method
                        Object[] args = invokeNode.getArgumentValues(frame);
                        putArgs(args);
                        if (func == null) {
                            func = invokeNode.getFunction(args);
                            RootCallTarget callTarget = func.getCallTarget();
                            NomRootNode rootNode = (NomRootNode) callTarget.getRootNode();
                            body = rootNode.getBodyNode();
                        }

//                        System.out.println("tail call: " + func.getName());

                        //begin tail call
                        NomFunctionBodyNode functionBodyNode = (NomFunctionBodyNode) body;
                        Object retValue = Pair.create(functionBodyNode, args);
                        leaveScope();
                        while (retValue instanceof Pair<?, ?> p && p.getLeft() instanceof NomFunctionBodyNode) {
                            functionBodyNode = (NomFunctionBodyNode) p.getLeft();
                            args = (Object[]) p.getRight();
                            putArgs(args);
                            functionBodyNode.curIndex = 0;
                            /* Execute the function body. */
                            loop:
                            while (true) {
                                if (functionBodyNode.curIndex >= functionBodyNode.bodyNodes.length) {
                                    throw new RuntimeException("Function body has no return statement");
                                }
                                block = functionBodyNode.bodyNodes[functionBodyNode.curIndex];
                                block.executeVoid(frame);
                                stmt = block.getTerminatingNode();
                                switch (stmt) {
                                    case NomBranchNode br -> {
                                        for (NomStatementNode mapStmt : br.mappings) {
                                            mapStmt.executeVoid(frame);
                                        }
                                        functionBodyNode.curIndex = br.getSuccessor();
                                    }
                                    case NomIfNode condBr -> {
                                        if (condBr.cond(frame)) {
                                            for (NomStatementNode mapStmt : condBr.trueBranch.mappings) {
                                                mapStmt.executeVoid(frame);
                                            }
                                            functionBodyNode.curIndex = condBr.getTrueSuccessor();
                                        } else {
                                            for (NomStatementNode mapStmt : condBr.falseBranch.mappings) {
                                                mapStmt.executeVoid(frame);
                                            }
                                            functionBodyNode.curIndex = condBr.getFalseSuccessor();
                                        }
                                    }
                                    case NomReturnNode r -> {
                                        if (r.valueNode == null) return NomNull.SINGLETON;
                                        if (r.valueNode instanceof NomInvokeNode<?> node) {
                                            func = node.getFunction();
                                            body = null;
                                            if (func != null) {//non-interface method
                                                RootCallTarget callTarget = func.getCallTarget();
                                                NomRootNode rootNode = (NomRootNode) callTarget.getRootNode();
                                                body = rootNode.getBodyNode();
                                                if (body instanceof NomBuiltinNode) {
                                                    node.executeGeneric(frame);
                                                }
                                            }

                                            //interface method
                                            args = node.getArgumentValues(frame);
                                            if (func == null) {
                                                func = node.getFunction(args);
                                                RootCallTarget callTarget = func.getCallTarget();
                                                NomRootNode rootNode = (NomRootNode) callTarget.getRootNode();
                                                body = rootNode.getBodyNode();
                                            }

//                        System.out.println("tail call: " + func.getName());

                                            functionBodyNode = (NomFunctionBodyNode) body;
                                            retValue = Pair.create(functionBodyNode, args);
                                            break loop;
                                        }
                                        retValue = r.valueNode.executeGeneric(frame);
                                        break loop;
                                    }
                                    case null, default -> throw new RuntimeException("Invalid terminating node");
                                }
                            }
                            leaveScope();
                        }
                        return retValue;
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