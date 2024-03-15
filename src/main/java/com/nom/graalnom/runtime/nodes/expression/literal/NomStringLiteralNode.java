package com.nom.graalnom.runtime.nodes.expression.literal;

import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.strings.TruffleString;

@NodeInfo(shortName = "const")
public final class NomStringLiteralNode extends NomExpressionNode {
    private final TruffleString value;

    public NomStringLiteralNode(TruffleString value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return value;
    }

    @Override
    public String toString() {
        return "String(" + value.toString() + ")";
    }
}
