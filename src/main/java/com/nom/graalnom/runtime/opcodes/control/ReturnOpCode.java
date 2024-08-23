package com.nom.graalnom.runtime.opcodes.control;

import com.nom.graalnom.runtime.opcodes.TransformedOpCode;

public class ReturnOpCode extends TransformedOpCode {
    private final int regIndex;

    public ReturnOpCode(int regIndex){
        this.regIndex = regIndex;
    }

    public int getRegIndex(){
        return regIndex;
    }
}
