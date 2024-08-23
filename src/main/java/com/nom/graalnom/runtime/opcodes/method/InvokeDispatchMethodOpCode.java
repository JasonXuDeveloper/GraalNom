package com.nom.graalnom.runtime.opcodes.method;

public class InvokeDispatchMethodOpCode extends InvokeMethodOpCode{
    private final int receiverIdx;
    public InvokeDispatchMethodOpCode(int regIdx, long nameId, int[] argIdxs, int receiverIdx) {
        super(regIdx, nameId, argIdxs);
        this.receiverIdx = receiverIdx;
    }

    public int getReceiverIdx() {
        return receiverIdx;
    }
}
