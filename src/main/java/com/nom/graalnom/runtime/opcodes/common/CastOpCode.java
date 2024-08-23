package com.nom.graalnom.runtime.opcodes.common;

import com.nom.graalnom.runtime.opcodes.TransformedOpCode;

public class CastOpCode extends TransformedOpCode {
    private final int valIdx;
    private final int regIdx;
    private final long typeId;

    public CastOpCode(int valIdx, int regIdx, long typeId) {
        this.valIdx = valIdx;
        this.regIdx = regIdx;
        this.typeId = typeId;
    }

    public int getRegIdx() {
        return regIdx;
    }

    public long getTypeId() {
        return typeId;
    }

    public int getValIdx() {
        return valIdx;
    }
}
