package com.nom.graalnom.runtime.opcodes.binOp;

import com.nom.graalnom.runtime.opcodes.TransformedOpCode;

public abstract class BinOpCode extends TransformedOpCode {
    private final int leftRegIdx;
    private final int rightRegIdx;
    private final int regIdx;

    public BinOpCode(int leftRegIdx, int rightRegIdx, int regIdx) {
        this.leftRegIdx = leftRegIdx;
        this.rightRegIdx = rightRegIdx;
        this.regIdx = regIdx;
    }

    public int getLeftRegIdx() {
        return leftRegIdx;
    }

    public int getRightRegIdx() {
        return rightRegIdx;
    }

    public int getRegIdx() {
        return regIdx;
    }
}
