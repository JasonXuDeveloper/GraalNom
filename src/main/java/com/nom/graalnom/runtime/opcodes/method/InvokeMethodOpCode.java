package com.nom.graalnom.runtime.opcodes.method;

import com.nom.graalnom.runtime.opcodes.TransformedOpCode;

public abstract class InvokeMethodOpCode extends TransformedOpCode {
    private final int regIdx;
    private final long nameId;
    private final int[] argIdxs;

    protected InvokeMethodOpCode(int regIdx, long nameId, int[] argIdxs) {
        this.regIdx = regIdx;
        this.nameId = nameId;
        this.argIdxs = argIdxs;
    }

    public int getRegIdx() {
        return regIdx;
    }

    public long getNameId() {
        return nameId;
    }

    public int[] getArgIdxs() {
        return argIdxs;
    }
}
