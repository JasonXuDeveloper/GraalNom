package com.nom.graalnom.runtime.constants;

import com.nom.graalnom.runtime.NomContext;

public class NomInterfaceConstant extends NomConstant {
    private final long Library;
    private final long Name;

    public NomInterfaceConstant(long library, long name) {
        super(NomConstantType.CTInterface);
        this.Library = library;
        this.Name = name;
    }

    public String GetName() {
        return NomContext.constants.GetString(Name).Value();
    }

    public String GetTruffleName() {
        return NomContext.constants.GetString(Name).Value();
    }


    @Override
    public void Print(boolean resolve) {
        System.out.print("Interface ");
        NomContext.constants.PrintConstant(Library, resolve);
        System.out.print("::");
        NomContext.constants.PrintConstant(Name, resolve);
    }

    @Override
    public String toString() {
        return NomContext.constants.GetString(Library).Value() + "::" + NomContext.constants.GetString(Name).Value();
    }
}
