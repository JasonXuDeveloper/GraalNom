package com.nom.graalnom.runtime.constants;

import com.nom.graalnom.runtime.NomContext;

public class NomClassTypeConstant extends NomConstant {
    private long cls;
    private long args;

    /*
    virtual void FillConstantDependencies(NOM_CONSTANT_DEPENCENCY_CONTAINER& result) override
    {
        result.push_back(cls);
        result.push_back(args);
    }

    NomClassTypeRef NomClassTypeConstant::GetClassType(NomSubstitutionContextRef context) const
    {
        //TODO: possible memleak
        NomConstantType ct = NomConstants::Get(this->cls)->Type;
        if (ct == NomConstantType::CTClass)
        {
            value = NomConstants::GetClass(this->cls)->GetClass()->GetType(NomConstants::GetTypeList(this->args)->GetTypeList(context));
        }
        else
        {
            value = NomConstants::GetInterface(this->cls)->GetInterface()->GetType(NomConstants::GetTypeList(this->args)->GetTypeList(context));
        }
        return value;
    }
     */

    public NomClassTypeConstant(long cls, long args) {
        super(NomConstantType.CTClassType);
        this.cls = cls;
        this.args = args;
    }

    public NomClassConstant Class() {
        return NomContext.constants.GetClass(cls);
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
