package com.nom.graalnom.runtime.nodes.expression.binary;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild("leftNode")
@NodeChild("rightNode")
public abstract class NomBinaryNode extends NomExpressionNode {

    protected abstract NomExpressionNode getLeftNode();
    protected abstract NomExpressionNode getRightNode();

    protected String Symbol(){
        return NomLanguage.lookupNodeInfo(this.getClass()).shortName();
    }

    @Override
    public String toString() {
        return getLeftNode().toString() + " "+ Symbol() +" " + getRightNode().toString();
    }


    @Fallback
    protected Object typeError(Object left, Object right) {
        throw new RuntimeException("Type error: " + left.getClass() + " " + Symbol() + " " + right.getClass());
    }
}
