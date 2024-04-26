/*
 * Copyright (c) 2012, 2020, Oracle and/or its affiliates. All rights reserved.
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
package com.nom.graalnom.runtime.nodes.expression;

import com.nom.graalnom.runtime.constants.NomConstant;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.nodes.NomRootNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomFunctionBodyNode;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import org.graalvm.collections.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The node for function invocation in SL. Since SL has first class functions, the {@link NomFunction
 * target function} can be computed by an arbitrary expression. This node is responsible for
 * evaluating this expression, as well as evaluating the {@link #argumentNodes arguments}. The
 * actual invocation is delegated to a {@link InteropLibrary} instance.
 *
 * @see InteropLibrary#execute(Object, Object...)
 */
@NodeInfo(shortName = "invoke")
public final class NomInvokeNode<T extends NomConstant> extends NomExpressionNode {

    private final Function<T, NomFunction> function;
    private final Function<T, String> getFuncQName;
//    private final Function<T, String> getFuncName;
    private final String funcName;
    private final T funcConst;
    @Node.Children
    public NomExpressionNode[] argumentNodes;

    private final boolean instanceMethodCall;

    public NomInvokeNode(boolean instanceMethodCall, T funcConst, Function<T, String> getFuncQName, String funcName, Function<T, NomFunction> function, NomExpressionNode[] argumentNodes) {
        this.funcConst = funcConst;
        this.function = function;
        this.getFuncQName = getFuncQName;
        this.funcName = funcName;
        this.argumentNodes = argumentNodes;
        this.instanceMethodCall = instanceMethodCall;
    }

    private NomFunction func;

    public NomInvokeNode(NomFunction func,String funcName, NomExpressionNode[] argumentNodes) {
        this.func = func;
        this.argumentNodes = argumentNodes;
        this.funcConst = null;
        this.function = null;
        this.getFuncQName = null;
        this.funcName = funcName;
        this.instanceMethodCall = false;
    }

    public Object[] getArgumentValues(VirtualFrame frame) {
        Object[] argumentValues = new Object[argumentNodes.length];
        for (int i = 0; i < argumentNodes.length; i++) {
            argumentValues[i] = argumentNodes[i].executeGeneric(frame);
        }

        return argumentValues;
    }


    public NomFunction getFunction(Object[] argumentValues) {
        if (func != null) return func;

        if (instanceMethodCall) {
            return ((NomObject) argumentValues[0]).GetFunction(funcName);
        }

        assert function != null;
        func = function.apply(funcConst);
        return func;
    }

    @Override
    @ExplodeLoop(kind = ExplodeLoop.LoopExplosionKind.FULL_UNROLL)
    public Object executeGeneric(VirtualFrame frame) {
        Object[] args = getArgumentValues(frame);
        NomFunction funcObj = getFunction(args);

        try {
            RootCallTarget target = funcObj.getCallTarget();
            NomRootNode root = (NomRootNode) target.getRootNode();
            NomExpressionNode expr = root.getBodyNode();
            NomFunctionBodyNode.enterScope(funcObj.regCount, args);
            Object ret = expr.executeGeneric(frame);
            while (ret instanceof Pair<?, ?> pair && pair.getLeft() instanceof NomFunctionBodyNode fb) {
                NomFunctionBodyNode.leaveScope();//at tail we dont care previous args/regs
                args = (Object[]) pair.getRight();
                NomFunctionBodyNode.enterScope(fb.regCount, args);
                ret = fb.executeGeneric(frame);
            }
            NomFunctionBodyNode.leaveScope();
            return ret;
        } catch (StackOverflowError e) {
            throw new RuntimeException("Stack overflow in function: " + funcObj.getName());
        }
    }

    @Override
    public String toString() {
        String funcName;
        if (func != null) {
            funcName = func.getName();
        } else {
            funcName = getFuncQName.apply(funcConst);
        }
        return funcName + "(" + String.join(", ",
                Arrays.stream(argumentNodes).map(Object::toString)
                        .toArray(String[]::new)) + ")";
    }
}
