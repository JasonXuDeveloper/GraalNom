package com.nom.graalnom.runtime.nodes.expression.binary;

import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "%")
public abstract class NomModNode extends NomBinaryNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    protected long doLong(long left, long right) {
        return left % right;
    }

    @Specialization
    protected double doDouble(double left, double right) {
        return left % right;
    }

    @Specialization
    protected double doDouble(long left, double right) {
        return left % right;
    }

    @Specialization
    protected double doDouble(double left, long right) {
        return left % right;
    }
}
