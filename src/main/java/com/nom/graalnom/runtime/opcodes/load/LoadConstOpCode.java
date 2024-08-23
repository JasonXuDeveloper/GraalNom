package com.nom.graalnom.runtime.opcodes.load;

import com.nom.graalnom.runtime.opcodes.TransformedOpCode;

public class LoadConstOpCode<T> extends TransformedOpCode {
    private final int regIndex;
    private final T value;

    public LoadConstOpCode(int regIndex, T value){
        this.regIndex = regIndex;
        this.value = value;
    }

    public int getRegIndex(){
        return regIndex;
    }

    public T getValue(){
        return value;
    }
}
