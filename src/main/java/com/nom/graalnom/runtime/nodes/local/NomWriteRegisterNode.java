package com.nom.graalnom.runtime.nodes.local;

import com.nom.graalnom.runtime.nodes.controlflow.NomFunctionBodyNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Node to write a local variable to a function's {@link VirtualFrame frame}. The Truffle frame API
 * allows to store primitive values of all Java primitive types, and Object values.
 */
@NodeChild("valueNode")
@NodeField(name = "regIndex", type = int.class)
public abstract class NomWriteRegisterNode extends NomExpressionNode {

    /**
     * Returns the descriptor of the accessed local variable. The implementation of this method is
     * created by the Truffle DSL based on the {@link NodeField} annotation on the class.
     */
    public abstract int getRegIndex();

    public abstract NomExpressionNode getValueNode();

    @Specialization
    protected Object write(VirtualFrame frame, Object value) {
        /*
         * Regardless of the type before, the new and final type of the local variable is Object.
         * Changing the slot kind also discards compiled code, because the variable type is
         * important when the compiler optimizes a method.
         *
         * No-op if kind is already Object.
         */
        Object[] reg = NomFunctionBodyNode.getRegs();
        if(reg.length <= getRegIndex()) {
            Object[] newRegs = new Object[reg.length * 2];
            System.arraycopy(reg, 0, newRegs, 0, reg.length);
            NomFunctionBodyNode.regsMap.set(NomFunctionBodyNode.depth, newRegs);
            reg = newRegs;
        }
        reg[getRegIndex()] = value;
//        System.out.println("Wrote " + value.getClass().getSimpleName() + " to " + getRegName());
        return value;
    }

    public abstract void executeWrite(VirtualFrame frame, Object value);

    @Override
    public String toString() {
        return "reg[" + getRegIndex() + "] = " + getValueNode().toString();
    }
}
