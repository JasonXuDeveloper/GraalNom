package com.nom.graalnom.runtime.nodes.expression.unary;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "-")
public abstract class NomNegateNode extends NomUnaryNode{
    @Specialization
    protected long negate(long value) {
        return -value;
    }

    @Specialization
    protected double negate(double value) {
        return -value;
    }

    @Fallback
    @CompilerDirectives.TruffleBoundary
    protected Object typeError(Object value) {
        throw new RuntimeException("Not a number: " + value);
    }
}
