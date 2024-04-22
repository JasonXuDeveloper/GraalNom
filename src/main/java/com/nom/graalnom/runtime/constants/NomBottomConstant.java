package com.nom.graalnom.runtime.constants;

public class NomBottomConstant extends NomConstant {
    public NomBottomConstant() {
        super(NomConstantType.CTBottom);
    }

    @Override
    public void Print(boolean resolve) {
        System.out.print("Bottom");
    }
}
