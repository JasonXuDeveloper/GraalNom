package com.nom.graalnom.runtime.nodes.expression.binary;

import com.nom.graalnom.runtime.datatypes.NomNull;
import com.nom.graalnom.runtime.datatypes.NomObject;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "==")
public abstract class NomRefEqualsNode extends NomBinaryNode {
    @Specialization
    protected boolean longEquals(long left, long right) {
        return left == right;
    }

    @Specialization
    protected boolean doubleEquals(double left, double right) {
        return left == right;
    }

    @Specialization
    protected boolean nullEquals(NomNull left, NomNull right) {
        return left == right;
    }

    @Specialization
    protected boolean boolEquals(boolean left, boolean right) {
        return left == right;
    }

    @Specialization
    protected boolean equals(NomObject left, NomObject right) {
        return left.equals(right);
    }
}
