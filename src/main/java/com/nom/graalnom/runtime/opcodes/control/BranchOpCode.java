package com.nom.graalnom.runtime.opcodes.control;

import com.nom.graalnom.runtime.opcodes.TransformedOpCode;

public class BranchOpCode extends TransformedOpCode {
    private int target;
    private final int[] incomings;
    private final int[] outgoings;

    public BranchOpCode(int target, int[] incomings, int[] outgoings) {
        this.target = target;
        this.incomings = incomings;
        this.outgoings = outgoings;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int[] getIncomings() {
        return incomings;
    }

    public int[] getOutgoings() {
        return outgoings;
    }
}
