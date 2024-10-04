package com.nom.graalnom.runtime.nodes.expression.unary;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild("node")
public abstract class NomUnaryNode extends NomExpressionNode {

    protected abstract NomExpressionNode getNode();

    @Override
    public String toString() {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return NomLanguage.lookupNodeInfo(this.getClass()).shortName() + getNode().toString();
    }

}
