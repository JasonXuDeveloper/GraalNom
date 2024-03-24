package com.nom.graalnom.runtime.nodes.expression.object;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomSuperClassConstant;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "instance")
public class NomNewObjectNode extends NomExpressionNode {
    private final NomSuperClassConstant superClassConstant;

    public NomNewObjectNode(NomSuperClassConstant superClassConstant) {
        this.superClassConstant = superClassConstant;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return NomLanguage.createObject(NomContext.classes.get(superClassConstant.GetSuperClass().GetName()));
    }

    @Override
    public String toString() {
        return "instance";
    }
}