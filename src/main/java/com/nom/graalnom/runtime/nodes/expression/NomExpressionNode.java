package com.nom.graalnom.runtime.nodes.expression;

import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.NomTypes;
import com.nom.graalnom.runtime.nodes.NomTypesGen;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@TypeSystemReference(NomTypes.class)
@NodeInfo(description = "The abstract base node for all expressions")
public abstract class NomExpressionNode extends NomStatementNode {
    /**
     * The execute method when no specialization is possible. This is the most general case,
     * therefore it must be provided by all subclasses.
     */
    public abstract Object executeGeneric(VirtualFrame frame);

    @Override
    public void executeVoid(VirtualFrame frame) {
        executeGeneric(frame);
    }


    /*
     * Execute methods for specialized types. They all follow the same pattern: they call the
     * generic execution method and then expect a result of their return type. Type-specialized
     * subclasses overwrite the appropriate methods.
     */

    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return NomTypesGen.expectLong(executeGeneric(frame));
    }

    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return NomTypesGen.expectBoolean(executeGeneric(frame));
    }

    public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
        return NomTypesGen.expectDouble(executeGeneric(frame));
    }
}
