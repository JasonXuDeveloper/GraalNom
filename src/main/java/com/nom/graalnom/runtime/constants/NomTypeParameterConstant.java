package com.nom.graalnom.runtime.constants;

import com.nom.graalnom.runtime.NomContext;

public class NomTypeParameterConstant extends NomConstant {
    public long UpperBound;
    public long LowerBound;

    public NomTypeParameterConstant(long lowerBound, long upperBound) {
        super(NomConstantType.CTTypeParameter);
        UpperBound = upperBound;
        LowerBound = lowerBound;
    }

    @Override
    public void Print(boolean resolve) {
        System.out.print("[");
        NomContext.constants.PrintConstant(UpperBound, resolve);
        System.out.print(",");
        NomContext.constants.PrintConstant(LowerBound, resolve);
        System.out.print("]");
    }
}
