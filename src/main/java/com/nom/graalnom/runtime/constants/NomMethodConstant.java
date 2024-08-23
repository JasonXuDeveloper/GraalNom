package com.nom.graalnom.runtime.constants;

import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.nom.graalnom.runtime.reflections.NomInterface;
import com.oracle.truffle.api.strings.TruffleString;

public class NomMethodConstant extends NomConstant {
    public final long classConstant;
    public final long methodName;
    public final long typeArgs;
    public final long argTypes;
    //private NomMethod method;

    public NomMethodConstant(long cls, long methodname, long typeArgs, long argTypes) {
        super(NomConstantType.CTMethod);
        this.classConstant = cls;
        this.methodName = methodname;
        this.typeArgs = typeArgs;
        this.argTypes = argTypes;
    }

    public TruffleString MethodName() {
        return NomContext.constants.GetString(methodName).Value();
    }

    public TruffleString QualifiedMethodName() {
        TruffleString name;
        if (ClassTypeConstant().GetClass() != null) {
            name = ClassTypeConstant().GetClass().GetName();
        } else {
            name = ClassTypeConstant().GetInterface().GetName();
        }
        return TruffleString.fromJavaStringUncached(name.toString() + "." + NomContext.constants.GetString(methodName).Value().toString(), TruffleString.Encoding.UTF_8);
    }

    public NomInterface Class() {
        NomClassTypeConstant clsConst = this.ClassTypeConstant();
        if (clsConst == null) {
            return null;
        }
        if (clsConst.GetClass() == null) {
            return NomContext.classes.get(clsConst.GetInterface().GetName());
        }
        return NomContext.classes.get(clsConst.GetClass().GetName());
    }

    public NomClassTypeConstant ClassTypeConstant() {
        return NomContext.constants.GetClassType(classConstant);
    }

    @Override
    public String toString() {
        return QualifiedMethodName().toString();
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
