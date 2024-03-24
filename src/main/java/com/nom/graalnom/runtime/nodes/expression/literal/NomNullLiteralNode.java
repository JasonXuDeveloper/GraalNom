package com.nom.graalnom.runtime.nodes.expression.literal;

import com.nom.graalnom.runtime.datatypes.NomNull;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@NodeInfo(shortName = "const")
public final class NomNullLiteralNode extends NomExpressionNode {


    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return NomNull.SINGLETON;
    }

    @Override
    public String toString() {
        return "Null";
    }
}
