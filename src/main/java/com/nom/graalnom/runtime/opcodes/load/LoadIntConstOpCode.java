package com.nom.graalnom.runtime.opcodes.load;

public class LoadIntConstOpCode extends LoadConstOpCode<Long>{

    public LoadIntConstOpCode(int regIndex, Long value) {
        super(regIndex, value);
    }
}
