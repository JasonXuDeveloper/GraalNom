package com.nom.graalnom.runtime.constants;

import com.nom.graalnom.runtime.NomContext;

public class NomSuperClassConstant extends NomConstant{
    public final long SuperClass;
    public final long Arguments;

    //public NomClass value;

    public NomSuperClassConstant(long superclass, long args) {
        super(NomConstantType.CTSuperClass);
        SuperClass = superclass;
        Arguments = args;
    }

    public NomClassConstant GetSuperClass() {
        return NomContext.constants.GetClass(SuperClass);
    }

    /*
    NomInstantiationRef<NomClass>  NomSuperClassConstant::GetClassType(NomSubstitutionContextRef context) const
    {
        if (value.Elem == nullptr)
        {
            value = NomInstantiationRef<NomClass>(NomConstants::GetClass(this->SuperClass)->GetClass(), NomConstants::GetTypeList(this->Arguments)->GetTypeList(context));
        }
        return value;
    }

    virtual void FillConstantDependencies(NOM_CONSTANT_DEPENCENCY_CONTAINER& result) override
    {
        result.push_back(SuperClass);
        result.push_back(Arguments);
    }
     */

    @Override
    public void Print(boolean resolve) {
        System.out.print("SuperClass ");
        NomContext.constants.PrintConstant(SuperClass, resolve);
        System.out.print("<");
        NomContext.constants.PrintConstant(Arguments, resolve);
        System.out.print(">");
    }
}
