package com.nom.graalnom.runtime.opcodes.method;

public class InvokeStaticMethodOpCode extends InvokeMethodOpCode{
    public InvokeStaticMethodOpCode(int regIdx, long nameId, int[] argIdxs) {
        super(regIdx, nameId, argIdxs);
    }
}
