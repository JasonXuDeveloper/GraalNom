package com.nom.graalnom.runtime.nodes.expression.unary;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "!")
public abstract class NomNotNode extends NomUnaryNode{
    @Specialization
    protected boolean not(boolean value) {
        return !value;
    }

    @Specialization
    protected boolean not(long value) {
        return value == 0;
    }

    @Specialization
    protected boolean not(double value) {
        return value == 0;
    }

    @Fallback
    @CompilerDirectives.TruffleBoundary
    protected Object typeError(Object value) {
        throw new RuntimeException("Not a number: " + value);
    }
}
