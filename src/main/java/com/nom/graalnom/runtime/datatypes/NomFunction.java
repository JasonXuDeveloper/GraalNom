/*
 * Copyright (c) 2014, 2022, Oracle and/or its affiliates. All rights reserved.
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
package com.nom.graalnom.runtime.datatypes;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.nodes.NomRootNode;
import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomFunctionBodyNode;
import com.nom.graalnom.runtime.reflections.NomType;
import com.oracle.truffle.api.*;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.ReportPolymorphism;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.*;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.utilities.CyclicAssumption;
import com.oracle.truffle.api.utilities.TriState;

import java.util.logging.Level;

/**
 * Represents a MonNom function. On the Truffle level, a callable element is represented by a
 * {@link RootCallTarget call target}. This class encapsulates a call target, and adds version
 * support: functions in SL can be redefined, i.e. changed at run time. When a function is
 * redefined, the call target managed by this function object is changed (and {@link #callTarget} is
 * therefore not a final field).
 */
@SuppressWarnings("static-method")
public final class NomFunction {

    private static final TruffleLogger LOG = TruffleLogger.getLogger(NomLanguage.ID, NomFunction.class);

    /**
     * The name of the function.
     */
    private final TruffleString name;

    /**
     * The current implementation of this function.
     */
    private RootCallTarget callTarget;

    public final NomFunctionBodyNode functionBodyNode;

    public final int regCount;


    public NomFunction(TruffleString name, NomFunctionBodyNode functionBodyNode, RootCallTarget callTarget, int regCount) {
        this.name = name;
        this.regCount = regCount;
        this.functionBodyNode = functionBodyNode;
        setCallTarget(callTarget);
    }

    public TruffleString getName() {
        return name;
    }

    public void setCallTarget(RootCallTarget callTarget) {
        this.callTarget = callTarget;
        /*
         * We have a new call target. Invalidate all code that speculated that the old call target
         * was stable.
         */
        LOG.log(Level.FINE, "Installed call target for: {0}", name);
    }

    public RootCallTarget getCallTarget() {
        return callTarget;
    }


    /**
     * This method is, e.g., called when using a function literal in a string concatenation. So
     * changing it has an effect on SL programs.
     */
    @Override
    public String toString() {
        return name.toString();
    }
}
