package com.nom.graalnom.runtime.constants;

import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.datatypes.NomString;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.oracle.truffle.api.strings.TruffleString;

public class NomStaticMethodConstant extends NomMethodConstant{
    public NomStaticMethodConstant(long cls, long methodname, long typeArgs, long argTypes) {
        super(cls, methodname, typeArgs, argTypes);
        this.Type = NomConstantType.CTStaticMethod;
    }

    @Override
    public TruffleString QualifiedMethodName() {
        return NomString.create(Class().GetName() + "." + NomContext.constants.GetString(methodName).GetText());
    }

    @Override
    public NomClass Class() {
        NomClassConstant clsConst = this.ClassConstant();
        if (clsConst == null) {
            return null;
        }
        return NomContext.classes.get(clsConst.GetName());
    }

    public NomClassConstant ClassConstant() {
        return NomContext.constants.GetClass(classConstant);
    }

    @Override
    public void Print(boolean resolve) {
        System.out.print("StaticMethod ");
        NomContext.constants.PrintConstant(classConstant, resolve);
        System.out.print(".");
        NomContext.constants.PrintConstant(methodName, resolve);
        System.out.print("<");
        NomContext.constants.PrintConstant(typeArgs, resolve);
        System.out.print(">(");
        NomContext.constants.PrintConstant(argTypes, resolve);
        System.out.print(")");
    }
}
