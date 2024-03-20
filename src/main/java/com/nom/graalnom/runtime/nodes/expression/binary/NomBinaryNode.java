package com.nom.graalnom.runtime.nodes.expression.binary;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild("leftNode")
@NodeChild("rightNode")
public abstract class NomBinaryNode extends NomExpressionNode {

    protected abstract NomExpressionNode getLeftNode();
    protected abstract NomExpressionNode getRightNode();

    @Override
    public String toString() {
        return getLeftNode().toString() + " "+ NomLanguage.lookupNodeInfo(this.getClass()).shortName() +" " + getRightNode().toString();
    }

}
