package com.nom.graalnom.runtime.constants;

import com.nom.graalnom.runtime.NomContext;

public class NomClassTypeConstant extends NomConstant {
    private long cls;
    private long args;

    public NomClassTypeConstant(long cls, long args) {
        super(NomConstantType.CTClassType);
        this.cls = cls;
        this.args = args;
    }

    public NomClassConstant GetClass() {
        return NomContext.constants.GetClass(cls);
    }

    public NomInterfaceConstant GetInterface() {
        return NomContext.constants.GetInterface(cls);
    }

    @Override
    public void Print(boolean resolve)
    {
        System.out.print("ClassType ");
        NomContext.constants.PrintConstant(cls, resolve);
        System.out.print("<");
        NomContext.constants.PrintConstant(args, resolve);
        System.out.print(">");
    }
}
