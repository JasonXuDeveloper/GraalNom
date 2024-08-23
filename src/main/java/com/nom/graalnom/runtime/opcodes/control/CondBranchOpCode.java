package com.nom.graalnom.runtime.opcodes.control;

import com.nom.graalnom.runtime.opcodes.TransformedOpCode;

public class CondBranchOpCode extends TransformedOpCode {
    private final int condRegIdx;
    private int trueTarget;
    private int falseTarget;
    private final int[] trueIncomings;
    private final int[] falseIncomings;
    private final int[] trueOutgoings;
    private final int[] falseOutgoings;

    public CondBranchOpCode(int condRegIdx, int trueTarget, int falseTarget, int[] trueIncomings, int[] falseIncomings, int[] trueOutgoings, int[] falseOutgoings) {
        this.condRegIdx = condRegIdx;
        this.trueTarget = trueTarget;
        this.falseTarget = falseTarget;
        this.trueIncomings = trueIncomings;
        this.falseIncomings = falseIncomings;
        this.trueOutgoings = trueOutgoings;
        this.falseOutgoings = falseOutgoings;
    }

    public int getCondRegIdx() {
        return condRegIdx;
    }

    public int getTrueTarget() {
        return trueTarget;
    }

    public int getFalseTarget() {
        return falseTarget;
    }

    public int[] getTrueIncomings() {
        return trueIncomings;
    }

    public int[] getFalseIncomings() {
        return falseIncomings;
    }

    public int[] getTrueOutgoings() {
        return trueOutgoings;
    }

    public int[] getFalseOutgoings() {
        return falseOutgoings;
    }

    public void setTrueTarget(int trueTarget) {
        this.trueTarget = trueTarget;
    }

    public void setFalseTarget(int falseTarget) {
        this.falseTarget = falseTarget;
    }
}
