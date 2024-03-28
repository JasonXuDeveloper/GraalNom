package com.nom.graalnom.runtime.nodes.expression.literal;

import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@NodeInfo(shortName = "const")
public final class NomStringLiteralNode extends NomExpressionNode {
    private final String value;

    public NomStringLiteralNode(String value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return value;
    }

    public String Value() {
        return value;
    }

    @Override
    public String toString() {
        return "'" + value + "'";
    }
}
