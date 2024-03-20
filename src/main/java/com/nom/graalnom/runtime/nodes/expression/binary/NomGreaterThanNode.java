package com.nom.graalnom.runtime.nodes.expression.binary;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = ">")
public abstract class NomGreaterThanNode extends NomBinaryNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    protected boolean doLong(long left, long right) {
        return left > right;
    }

    @Specialization
    protected boolean doDouble(double left, double right) {
        return left > right;
    }

    @Specialization
    protected boolean doDouble(long left, double right) {
        return left > right;
    }

    @Specialization
    protected boolean doDouble(double left, long right) {
        return left > right;
    }
}
