package com.nom.graalnom.runtime.opcodes.common;

import com.nom.graalnom.runtime.opcodes.TransformedOpCode;

public class ReadFieldOpCode extends TransformedOpCode {
    private final int receiverRegIdx;
    private final int regIdx;
    private final long nameId;

    public ReadFieldOpCode(int receiverRegIdx, int regIdx, long nameId) {
        this.receiverRegIdx = receiverRegIdx;
        this.regIdx = regIdx;
        this.nameId = nameId;
    }

    public int getReceiverRegIdx() {
        return receiverRegIdx;
    }

    public int getRegIdx() {
        return regIdx;
    }

    public long getNameId() {
        return nameId;
    }
}
