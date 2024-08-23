package com.nom.graalnom.runtime.opcodes.method;

public class InvokeCtorOpCode extends InvokeMethodOpCode{
    private final boolean hasInstance;
    public InvokeCtorOpCode(int regIdx, long nameId, int[] argIdxs, boolean hasInstance) {
        super(regIdx, nameId, argIdxs);
        this.hasInstance = hasInstance;
    }

    public boolean getHasInstance() {
        return hasInstance;
    }
}
