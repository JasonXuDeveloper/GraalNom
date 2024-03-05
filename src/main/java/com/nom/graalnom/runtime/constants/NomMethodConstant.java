package com.nom.graalnom.runtime.constants;

import com.nom.graalnom.runtime.NomContext;

public class NomMethodConstant extends NomConstant{
    private final long classConstant;
    private final long methodName;
    private final long typeArgs;
    private final long argTypes;
    //private NomMethod method;

    public NomMethodConstant(long cls, long methodname, long typeArgs, long argTypes) {
        super(NomConstantType.CTMethod);
        this.classConstant = cls;
        this.methodName = methodname;
        this.typeArgs = typeArgs;
        this.argTypes = argTypes;
    }

    /*
    virtual void FillConstantDependencies(NOM_CONSTANT_DEPENCENCY_CONTAINER& result) override
    {
        result.push_back(classConstant);
        result.push_back(methodName);
        result.push_back(typeArgs);
        result.push_back(argTypes);
    }

    NomInstantiationRef<const NomMethod> NomMethodConstant::GetMethod(NomSubstitutionContextRef  context) const
    {
        //TODO : possible memleak
        {
            auto clstype = NomConstants::GetClassType(classConstant)->GetClassType(context);
            auto targs = NomConstants::GetTypeList(typeArgs)->GetTypeList(context);
            auto clsargs = clstype->Arguments;
            NomTypeRef* carr = nullptr;
            const int clsargscount = clsargs.size();
            const int targcount = targs.size() + clsargscount;
            if (targcount > 0)
            {
                carr = (NomTypeRef*)nmalloc(sizeof(NomTypeRef) * targcount);
                for (int i = 0; i < targcount; i++)
                {
                    if (i < clsargscount)
                    {
                        carr[i] = clsargs[i];
                    }
                    else
                    {
                        carr[i] = targs[i - clsargscount];
                    }
                }
            }
            auto argarggref = llvm::ArrayRef<NomTypeRef>(carr, targcount);
            auto nsc = NomSubstitutionContextList(argarggref);
            if (clstype->Named->IsInterface())
            {
                method = (dynamic_cast<const NomInterface*>(clstype->Named))->GetMethod(&nsc, NomConstants::GetString(methodName)->GetText(), targs, NomConstants::GetTypeList(argTypes)->GetTypeList(&nsc));
            }
            else
            {
                method = (dynamic_cast<const NomClass*>(clstype->Named))->GetMethod(&nsc, NomConstants::GetString(methodName)->GetText(), targs, NomConstants::GetTypeList(argTypes)->GetTypeList(&nsc));
            }
        }
        return method;
    }
     */

    @Override
    public void Print(boolean resolve) {
        System.out.print("Method ");
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
