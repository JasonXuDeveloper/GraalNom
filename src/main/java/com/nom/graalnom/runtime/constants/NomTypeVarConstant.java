package com.nom.graalnom.runtime.constants;

public class NomTypeVarConstant extends NomConstant{
    public int index;
    public NomTypeVarConstant(int index) {
        super(NomConstantType.CTTypeVar);
        this.index = index;
    }
    @Override
    public void Print(boolean resolve) {
        System.out.print("TypeVar " + index);
    }
}
