package com.nom.graalnom.runtime.constants;

import com.nom.graalnom.runtime.NomContext;

import java.util.List;

public class NomTypeListConstant extends NomConstant{
    private List<Long> types;

    public int Count() {
        return types.size();
    }

    public NomTypeListConstant(List<Long> types) {
        super(NomConstantType.CTTypeList);
        this.types = types;
    }

    @Override
    public void Print(boolean resolve) {
        System.out.print("TypeList (");
        boolean first = true;
        for (long type : types) {
            if (!first) {
                System.out.print(", ");
            }
            first = false;
            NomContext.constants.PrintConstant(type, resolve);
        }
        System.out.print(")");
    }
}
