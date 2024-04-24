package com.nom.graalnom.runtime.constants;

public class NomDynamicTypeConstant extends NomConstant{
    public NomDynamicTypeConstant() {
        super(NomConstantType.CTDynamic);
    }

    @Override
    public void Print(boolean resolve) {
        System.out.print("dyn");
    }
}
