package com.nom.graalnom.runtime.opcodes.common;

import com.nom.graalnom.runtime.opcodes.TransformedOpCode;

public class WriteFieldOpCode extends TransformedOpCode {
    private final int receiverRegIdx;
    private final int argRegIdx;
    private final long nameId;

    public WriteFieldOpCode(int receiverRegIdx, int argRegIdx, long nameId) {
        this.receiverRegIdx = receiverRegIdx;
        this.argRegIdx = argRegIdx;
        this.nameId = nameId;
    }

    public int getReceiverRegIdx() {
        return receiverRegIdx;
    }

    public int getArgRegIdx() {
        return argRegIdx;
    }

    public long getNameId() {
        return nameId;
    }
}
