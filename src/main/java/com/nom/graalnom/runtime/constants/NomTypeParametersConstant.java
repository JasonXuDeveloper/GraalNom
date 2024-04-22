package com.nom.graalnom.runtime.constants;

public class NomTypeParametersConstant extends NomConstant{
    public NomTypeParameterConstant[] typeParameters;

    public NomTypeParametersConstant(NomTypeParameterConstant[] typeParameters) {
        super(NomConstantType.CTTypeParameters);
        this.typeParameters = typeParameters;
    }
    @Override
    public void Print(boolean resolve) {
        System.out.print("<");
        for (int i = 0; i < typeParameters.length; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            typeParameters[i].Print(resolve);
        }
        System.out.print(">");
    }
}
