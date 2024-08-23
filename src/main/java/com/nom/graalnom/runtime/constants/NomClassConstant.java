package com.nom.graalnom.runtime.constants;

import com.nom.graalnom.runtime.NomContext;
import com.oracle.truffle.api.strings.TruffleString;

public class NomClassConstant extends NomConstant {
    private final long Library;
    private final long Name;

    public NomClassConstant(long library, long name) {
        super(NomConstantType.CTClass);
        this.Library = library;
        this.Name = name;
    }

    public TruffleString GetName() {
        return NomContext.constants.GetString(Name).Value();
    }

    public TruffleString GetTruffleName() {
        return NomContext.constants.GetString(Name).Value();
    }


    @Override
    public void Print(boolean resolve) {
        System.out.print("Class ");
        NomContext.constants.PrintConstant(Library, resolve);
        System.out.print("::");
        NomContext.constants.PrintConstant(Name, resolve);
    }

    @Override
    public String toString() {
        return NomContext.constants.GetString(Library).Value() + "::" + NomContext.constants.GetString(Name).Value();
    }
}
