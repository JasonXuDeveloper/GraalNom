package com.nom.graalnom.runtime.opcodes.method;

public class InvokeInstanceMethodOpCode extends InvokeMethodOpCode{
    private final int receiverIdx;
    public InvokeInstanceMethodOpCode(int regIdx, long nameId, int[] argIdxs, int receiverIdx) {
        super(regIdx, nameId, argIdxs);
        this.receiverIdx = receiverIdx;
    }

    public int getReceiverIdx() {
        return receiverIdx;
    }
}
