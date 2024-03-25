package com.nom.graalnom.runtime.reflections;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomClassConstant;
import com.nom.graalnom.runtime.constants.NomSuperClassConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class NomClass extends NomInterface {
    public List<NomTypedField> Fields;
    public List<NomTypedField> AllFields;
    public long SuperClass;
    public NomClass SuperClassRef;

    public List<NomStaticMethod> StaticMethods;
    public List<NomConstructor> Constructors;

    private HashSet<Long> SuperClassDependencies;

/*
    std::vector<NomLambda*> Lambdas;
    std::vector<NomRecord*> Structs;

 */

    public NomClass(long name, long typeArgs, long superClass, long superInterfaces) {
        super(name);
        this.SuperClass = superClass;
        this.Fields = new java.util.ArrayList<>();
        this.AllFields = new java.util.ArrayList<>();
        this.StaticMethods = new java.util.ArrayList<>();
        this.Constructors = new java.util.ArrayList<>();
    }

    public static void RegisterClass(String name, NomClass cls) {
        NomContext.classes.put(name, cls);
    }

    public static NomClass getClass(String name) {
        return NomContext.classes.get(name);
    }


    public NomTypedField AddField(long name, long type, Visibility visibility, boolean isReadOnly, boolean isVolatile) {
        NomTypedField field = new NomTypedField(this, name, type, visibility, isReadOnly, isVolatile);
        Fields.add(field);
        return field;
    }

    public NomMethod AddMethod(String name, String qname, long typeArgs, long returnType, long argTypes, int regCount, boolean isFinal) {
        NomMethod method = new NomMethod(name, this, qname, returnType, typeArgs, argTypes, regCount, false, isFinal);
        Methods.add(method);
        return method;
    }

    public NomStaticMethod AddStaticMethod(String name, String qname, long typeArgs, long returnType, long argTypes, int regCount) {
        NomStaticMethod staticMethod = new NomStaticMethod(name, this, qname, returnType, typeArgs, argTypes, regCount, false);
        StaticMethods.add(staticMethod);
        return staticMethod;
    }

    public NomConstructor AddConstructor(long arguments, int regcount) {
        String name = "_Constructor_" + this.GetName().toString() + "_" + NomContext.constants.GetTypeList(arguments).Count();
        NomConstructor constructor = new NomConstructor(name, this, name, 0, arguments, regcount, false);
        Constructors.add(constructor);
        return constructor;
    }

    /*
    virtual void PushDependencies(std::set<ConstantID>& set) const override
			{
				NomInterfaceLoaded::PushDependencies(set);
				set.insert(superClass);

				for (auto method : StaticMethods)
				{
					dynamic_cast<NomCallableLoaded*>(method)->PushDependencies(set);
				}
				for (auto cnstr : Constructors)
				{
					dynamic_cast<NomCallableLoaded*>(cnstr)->PushDependencies(set);
				}
				for (auto lambda : Lambdas)
				{
					dynamic_cast<NomCallableLoaded*>(lambda)->PushDependencies(set);
				}
				for (auto strct : Structs)
				{
					dynamic_cast<NomCallableLoaded*>(strct)->PushDependencies(set);
				}
				for (auto field : Fields)
				{
					field->PushDependencies(set);
				}
			}
     */
    public void PushDependencies(List<Long> set) {
        set.add(SuperClass);
        for (NomTypedField field : Fields) {
            field.PushDependencies(set);
        }
    }

    public NomField GetField(long nameId) {
        for (NomTypedField field : Fields) {
            if (field.Name == nameId) {
                return field;
            }
        }
        for (NomTypedField field : AllFields) {
            if (field.Name == nameId) {
                return field;
            }
        }
        return null;
    }

    public NomField GetField(String name) {
        for (NomTypedField field : Fields) {
            if (name.equals(NomContext.constants.GetString(field.Name).GetText().toString())) {
                return field;
            }
        }
        for (NomTypedField field : AllFields) {
            if (name.equals(NomContext.constants.GetString(field.Name).GetText().toString())) {
                return field;
            }
        }
        return null;
    }

    public void Register(NomLanguage language) {
        //copy methods from superclass
        List<NomClass> superClassChain = new ArrayList<>();
        SuperClassDependencies = new HashSet<>();
        long superClassId = SuperClass;
        SuperClassDependencies.add(superClassId);
        while (superClassId != 0) {
            NomSuperClassConstant sc = NomContext.constants.GetSuperClass(superClassId);
            NomClassConstant superClassRef = sc.GetSuperClass();
            if (NomContext.classes.get(superClassRef.GetName()) != null) {
                NomClass cls = NomContext.classes.get(superClassRef.GetName());
                superClassChain.add(cls);
                SuperClassDependencies.add(superClassId);
                superClassId = cls.SuperClass;
            } else {
                superClassId = 0;
            }
        }
        superClassChain = superClassChain.reversed();
        var clsFunctions = NomContext.functionsObject.computeIfAbsent(this, k -> new HashMap<>());
        var ctorFunctions = NomContext.ctorFunctions.computeIfAbsent(this.GetName(), k -> new HashMap<>());
        for (NomClass superClass : superClassChain) {
            for (var method : superClass.StaticMethods) {
                clsFunctions.put(method.GetName(), method.GetFunction(language));
            }
            for (var method : superClass.Methods) {
                clsFunctions.put(method.GetName(), method.GetFunction(language));
            }
            for (var field : superClass.Fields) {
                if (!AllFields.contains(field)) {
                    AllFields.add(field);
                }
            }
        }

        //constructors
        for (var constructor : Constructors) {
            ctorFunctions.put(constructor.GetArgCount(), constructor.GetFunction(language));
        }
        //static methods
        for (var method : StaticMethods) {
            clsFunctions.put(method.GetName(), method.GetFunction(language));
        }
        //methods
        for (var method : Methods) {
            clsFunctions.put(method.GetName(), method.GetFunction(language));
        }
    }
}
