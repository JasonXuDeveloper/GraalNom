package com.nom.graalnom.runtime.nodes.local;

import com.nom.graalnom.runtime.datatypes.NomNull;
import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomFunctionBodyNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Node to write a local variable to a function's {@link VirtualFrame frame}. The Truffle frame API
 * allows to store primitive values of all Java primitive types, and Object values.
 */
public class NomWriteRegisterNode extends NomStatementNode {

    public int getRegIndex(){
        return regIndex;
    }

    public NomExpressionNode getValueNode(){
        return valueNode;
    }

    private final int regIndex;
    @CompilerDirectives.CompilationFinal
    @Child
    private NomExpressionNode valueNode;

    public NomWriteRegisterNode(int regIndex, NomExpressionNode valueNode) {
        this.regIndex = regIndex;
        this.valueNode = valueNode;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        NomFunctionBodyNode.getRegs()[getRegIndex()] = valueNode.executeGeneric(frame);
    }

    @Override
    public String toString() {
        return "reg[" + getRegIndex() + "] = " + getValueNode().toString();
    }
}
