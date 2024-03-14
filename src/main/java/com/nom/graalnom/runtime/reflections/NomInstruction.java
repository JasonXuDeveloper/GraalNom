package com.nom.graalnom.runtime.reflections;


public class NomInstruction {
    private final OpCode opCode;
    public NomInstruction(OpCode opCode){
        this.opCode = opCode;
    }

    public OpCode getOpCode(){
        return opCode;
    }
}
