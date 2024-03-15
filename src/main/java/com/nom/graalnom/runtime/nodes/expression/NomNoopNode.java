package com.nom.graalnom.runtime.nodes.expression;

import com.nom.graalnom.runtime.datatypes.NomNull;
import com.oracle.truffle.api.frame.VirtualFrame;

public final class NomNoopNode extends NomExpressionNode {
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return NomNull.SINGLETON;
    }

    @Override
    public String toString() {
        return "Noop()";
    }
}
