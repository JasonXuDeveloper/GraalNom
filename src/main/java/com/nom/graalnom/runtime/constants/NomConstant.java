package com.nom.graalnom.runtime.constants;

public abstract class NomConstant {
    public NomConstantType Type;
    public abstract void Print(boolean resolve);

    public NomConstant(NomConstantType type){
        this.Type = type;
    }
}
