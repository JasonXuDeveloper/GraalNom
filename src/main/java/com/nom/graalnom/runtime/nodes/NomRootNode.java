/*
 * Copyright (c) 2012, 2022, Oracle and/or its affiliates. All rights reserved.
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
package com.nom.graalnom.runtime.nodes;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.nodes.expression.*;
import com.nom.graalnom.runtime.nodes.controlflow.*;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.*;
import com.oracle.truffle.api.strings.TruffleString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The root of all Nom execution trees. It is a Truffle requirement that the tree root extends the
 * class {@link RootNode}. This class is used for both builtin and user-defined functions. For
 * builtin functions, the {@link #bodyNode} is a subclass of {@link com.nom.graalnom.runtime.builtins.NomBuiltinNode}. For user-defined
 * functions, the {@link #bodyNode} is a {@link NomBasicBlockNode}.
 */
@NodeInfo(language = "Nom", description = "The root of all Nom execution trees")
public class NomRootNode extends RootNode {
    /**
     * The function body that is executed, and specialized during execution.
     */
    @Child
    private NomExpressionNode bodyNode;

    /**
     * The name of the function, for printing purposes only.
     */
    private final TruffleString name;

    private final int argCount;

    public NomRootNode(NomLanguage language, FrameDescriptor frameDescriptor, NomExpressionNode bodyNode, TruffleString name, int argCount) {
        super(language, frameDescriptor);
        this.bodyNode = bodyNode;
        this.name = name;
        this.argCount = argCount;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        assert NomContext.get(this) != null;
        return bodyNode.executeGeneric(frame);
    }

    public NomExpressionNode getBodyNode() {
        return bodyNode;
    }

    @Override
    public String getName() {
        return name.toString();
    }

    @Override
    public String toString() {
        StringBuilder arg = new StringBuilder("(");
        for (int i = 0; i < argCount; i++) {
            arg.append("arg").append(i);
            if (i < argCount - 1) {
                arg.append(", ");
            }
        }
        arg.append(")");
        return "func " + name + arg;
    }
}
