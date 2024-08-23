package com.nom.graalnom.runtime.opcodes.load;

public class LoadBoolConstOpCode extends LoadConstOpCode<Boolean> {

    public LoadBoolConstOpCode(int regIndex, Boolean value) {
        super(regIndex, value);
    }
}
