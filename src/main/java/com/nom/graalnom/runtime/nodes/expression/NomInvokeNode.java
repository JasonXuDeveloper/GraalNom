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
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.util.Arrays;
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

    private Function<T, NomFunction> function;
    private Function<T, String> getFuncQName;
    private Function<T, String> getFuncName;
    private T funcConst;
    @Node.Children
    private final NomExpressionNode[] argumentNodes;
    @Node.Child
    private InteropLibrary library;

    public NomInvokeNode(T funcConst, Function<T, String> getFuncQName, Function<T, String> getFuncName, Function<T, NomFunction> function, NomExpressionNode[] argumentNodes) {
        this.funcConst = funcConst;
        this.function = function;
        this.getFuncQName = getFuncQName;
        this.getFuncName = getFuncName;
        this.argumentNodes = argumentNodes;
        this.library = InteropLibrary.getFactory().createDispatched(3);
    }

    @ExplodeLoop
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        /*
         * The number of arguments is constant for one invoke node. During compilation, the loop is
         * unrolled and the execute methods of all arguments are inlined. This is triggered by the
         * ExplodeLoop annotation on the method. The compiler assertion below illustrates that the
         * array length is really constant.
         */
        CompilerAsserts.compilationConstant(argumentNodes.length);

        Object[] argumentValues = new Object[argumentNodes.length];
        for (int i = 0; i < argumentNodes.length; i++) {
            argumentValues[i] = argumentNodes[i].executeGeneric(frame);
        }

        try {
            NomFunction funcObj = function.apply(funcConst);
            //plausibly interface method
            if(funcObj == null){
                Object arg0 = argumentValues[0];
                if(arg0 instanceof NomObject obj){
                    funcObj = obj.GetFunction(getFuncName.apply(funcConst));
                }
            }

            if(funcObj == null)
            {
                throw new RuntimeException("Function not found: " + getFuncQName.apply(funcConst));
            }

//            System.out.println(funcObj.getName());
//            for (Object argumentValue : argumentValues) {
//                if(argumentValue instanceof NomObject obj){
//                    System.out.println(obj.GetClass().GetName());
//                    continue;
//                }
//                System.out.println(argumentValue);
//            }

            return library.execute(funcObj, argumentValues);
        } catch (ArityException | UnsupportedTypeException | UnsupportedMessageException e) {
            /* Execute was not successful. */
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getFuncQName.apply(funcConst) + "(" + String.join(", ",
                Arrays.stream(argumentNodes).map(Object::toString)
                        .toArray(String[]::new)) + ")";
    }
}
