package com.nom.graalnom.runtime.opcodes.unOp;

import com.nom.graalnom.runtime.opcodes.TransformedOpCode;

public abstract class UnOpCode extends TransformedOpCode {
    private final int receiverRegIdx;
    private final int regIdx;

    public UnOpCode(int receiverRegIdx, int regIdx) {
        this.receiverRegIdx = receiverRegIdx;
        this.regIdx = regIdx;
    }

    public int getReceiverRegIdx() {
        return receiverRegIdx;
    }

    public int getRegIdx() {
        return regIdx;
    }
}
