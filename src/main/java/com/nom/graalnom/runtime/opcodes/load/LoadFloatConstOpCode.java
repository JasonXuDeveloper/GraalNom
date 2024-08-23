package com.nom.graalnom.runtime.opcodes.load;

public class LoadFloatConstOpCode extends LoadConstOpCode<Double>{
    public LoadFloatConstOpCode(int regIndex, Double value) {
        super(regIndex, value);
    }
}
