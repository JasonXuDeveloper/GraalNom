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

import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomConstant;
import com.nom.graalnom.runtime.constants.NomInterfaceConstant;
import com.nom.graalnom.runtime.constants.NomSuperInterfacesConstant;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.nodes.NomRootNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomFunctionBodyNode;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.dsl.GenerateInline;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import org.graalvm.collections.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The node for function invocation in MonNom. The {@link NomFunction
 * target function} can be computed by an arbitrary expression. This node is responsible for
 * evaluating this expression, as well as evaluating the {@link #argumentNodes arguments}. The
 * actual invocation is delegated to a {@link InteropLibrary} instance.
 *
 */
@NodeInfo(shortName = "invoke")
@GenerateInline
public final class NomInvokeNode<T extends NomConstant> extends NomExpressionNode {

    @CompilerDirectives.CompilationFinal
    private final Function<T, NomFunction> function;
    @CompilerDirectives.CompilationFinal
    private final Function<T, String> getFuncQName;
    @CompilerDirectives.CompilationFinal
    public final String funcName;
    @CompilerDirectives.CompilationFinal
    private final T funcConst;
    @Node.Children
    public NomExpressionNode[] argumentNodes;

    @CompilerDirectives.CompilationFinal
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

    public NomInvokeNode(NomFunction func, String funcName, NomExpressionNode[] argumentNodes) {
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

        if (instanceMethodCall && argumentValues[0] instanceof NomObject obj){
            NomFunction f = obj.GetFunction(funcName);
            if (f == null && obj.GetClass() != null) {
                try {
                    Object member = obj.readMember(funcName);
                    if (member instanceof NomObject memObj) {
                        for (var s : memObj.methodTable.entrySet()) {
                            String methName = s.getKey();
                            NomSuperInterfacesConstant sc = NomContext.constants.GetSuperInterfaces(memObj.GetClass().SuperInterfaces);
                            for (Pair<Long, Long> pair : sc.entries) {
                                long classNameId = pair.getLeft();
                                NomInterfaceConstant inter = NomContext.constants.GetInterface((int) classNameId);
                                if (inter.GetName().equals(methName)) {
                                    f = s.getValue();
                                    argumentValues[0] = memObj;
                                    return f;
                                }
                            }
                        }
                    }
                } catch (UnknownIdentifierException e) {
                    throw new RuntimeException(e);
                }
            }

            if (f != null) {
                return f;
            }
        }

        assert function != null;
        func = function.apply(funcConst);
        return func;
    }

    @Override
    @ExplodeLoop(kind = ExplodeLoop.LoopExplosionKind.FULL_UNROLL_UNTIL_RETURN)
    public Object executeGeneric(VirtualFrame frame) {
        Object[] args = getArgumentValues(frame);
        NomFunction funcObj = getFunction(args);
        NomExpressionNode expr = funcObj.rootNode.getBodyNode();

        try {
            NomFunctionBodyNode.enterScope(funcObj.regCount, args);
            Object ret = expr.executeGeneric(frame);
            while (ret instanceof NomFunctionBodyNode.TailResult tailResult) {
                NomFunctionBodyNode.leaveScope();//at tail we dont care previous args/regs
                NomFunctionBodyNode.enterScope(tailResult.functionBodyNode().regCount, tailResult.args());
                ret = tailResult.functionBodyNode().executeGeneric(frame);
            }
            NomFunctionBodyNode.leaveScope();
            return ret;
        } catch (StackOverflowError e) {
            throw new RuntimeException("Stack overflow in function: " + funcObj.getName());
        }
//        catch (Exception e) {
//            System.out.println(this);
//            System.out.println(expr);
//            throw new RuntimeException(e);
//        }
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
