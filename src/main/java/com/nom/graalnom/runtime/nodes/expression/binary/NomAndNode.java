package com.nom.graalnom.runtime.nodes.expression.binary;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "&&")
public abstract class NomAndNode extends NomBinaryNode {
    @Specialization
    protected boolean doDefault(boolean left, boolean right) {
        return left && right;
    }
}
