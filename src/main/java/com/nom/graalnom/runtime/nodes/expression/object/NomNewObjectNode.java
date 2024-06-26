package com.nom.graalnom.runtime.nodes.expression.object;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomSuperClassConstant;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.util.Timer;

@NodeInfo(shortName = "new()")
public class NomNewObjectNode extends NomExpressionNode {
    private final NomSuperClassConstant superClassConstant;

    public NomNewObjectNode(NomSuperClassConstant superClassConstant) {
        this.superClassConstant = superClassConstant;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        String name = superClassConstant.GetSuperClass().GetName();
        //TODO built in lookup
        if (name.equals("Timer_0")) {
            return NomLanguage.createTimer();
        }
        return NomLanguage.createObject((NomClass) NomContext.classes.get(name));
    }

    @Override
    public String toString() {
        return "new "+ superClassConstant.GetSuperClass().GetName() +"()";
    }
}
