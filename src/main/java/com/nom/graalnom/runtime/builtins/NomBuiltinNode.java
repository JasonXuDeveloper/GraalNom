package com.nom.graalnom.runtime.builtins;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@NodeChild(value = "arguments", type = NomExpressionNode[].class)
@GenerateNodeFactory
public abstract class NomBuiltinNode extends NomExpressionNode {

    public abstract NomExpressionNode[] getArguments();

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return execute(frame);
    }

    @Override
    public final boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return super.executeBoolean(frame);
    }

    @Override
    public final long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return super.executeLong(frame);
    }

    @Override
    public final double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
        return super.executeDouble(frame);
    }

    @Override
    public final void executeVoid(VirtualFrame frame) {
        super.executeVoid(frame);
    }

    protected abstract Object execute(VirtualFrame frame);

    @Specialization
    protected Object doNomObject(NomObject obj) {
        return obj.GetFunction(NomLanguage.lookupNodeInfo(this.getClass()).shortName()).getCallTarget().call(obj);
    }
}
