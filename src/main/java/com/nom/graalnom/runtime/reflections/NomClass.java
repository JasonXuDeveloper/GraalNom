package com.nom.graalnom.runtime.reflections;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomClassConstant;
import com.nom.graalnom.runtime.constants.NomSuperClassConstant;
import com.oracle.truffle.api.strings.TruffleString;

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

/*
    std::vector<NomLambda*> Lambdas;
    std::vector<NomRecord*> Structs;

 */

    public NomClass(long name, long typeArgs, long superClass, long superInterfaces) {
        super(name, typeArgs, superInterfaces);
        this.SuperClass = superClass;
        this.Fields = new java.util.ArrayList<>();
        this.AllFields = new java.util.ArrayList<>();
        this.StaticMethods = new java.util.ArrayList<>();
        this.Constructors = new java.util.ArrayList<>();
    }

    public NomTypedField AddField(long name, long type, Visibility visibility, boolean isReadOnly, boolean isVolatile) {
        NomTypedField field = new NomTypedField(this, name, type, visibility, isReadOnly, isVolatile);
        Fields.add(field);
        return field;
    }

    public NomStaticMethod AddStaticMethod(TruffleString name, TruffleString qname, long typeArgs, long returnType, long argTypes, int regCount) {
        NomStaticMethod staticMethod = new NomStaticMethod(name, this, qname, returnType, typeArgs, argTypes, regCount, false);
        StaticMethods.add(staticMethod);
        return staticMethod;
    }

    public NomConstructor AddConstructor(long arguments, int regcount) {
        TruffleString name =
                TruffleString.fromJavaStringUncached("_Constructor_" + this.GetName() + "_" +
                                (NomContext.constants.GetTypeList(arguments) == null ?
                                        0 :
                                        NomContext.constants.GetTypeList(arguments).Count()),
                        TruffleString.Encoding.UTF_8);
        NomConstructor constructor = new NomConstructor(name, this, name, 0, arguments, regcount, false);
        Constructors.add(constructor);
        return constructor;
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

    public NomField GetField(TruffleString name) {
        for (NomTypedField field : Fields) {
            if (name.equals(NomContext.constants.GetString(field.Name).Value())) {
                return field;
            }
        }
        for (NomTypedField field : AllFields) {
            if (name.equals(NomContext.constants.GetString(field.Name).Value())) {
                return field;
            }
        }
        return null;
    }

    public HashSet<NomClass> superClasses = new HashSet<>();

    @Override
    public void Register(NomLanguage language) {
        super.Register(language);
        //copy methods from superclass
        NomSuperClassConstant sc = NomContext.constants.GetSuperClass(SuperClass);
        NomClassConstant superClassRef = sc.GetSuperClass();
        NomClass superClass = null;
        if (NomContext.classes.get(superClassRef.GetName()) != null) {
            superClass = (NomClass) NomContext.classes.get(superClassRef.GetName());
            if (!superClass.registered) {
                superClass.Register(language);
                superClass.registered = true;
            }
            superClasses.addAll(superClass.superClasses);
        }
        superClasses.add(this);
        var clsFunctions = NomContext.functionsObject.computeIfAbsent(this, k -> new HashMap<>());
        var ctorFunctions = NomContext.ctorFunctions.computeIfAbsent(this.GetName(), k -> new HashMap<>());
        if (superClass != null) {
            for (var method : superClass.StaticMethods) {
                clsFunctions.put(method.GetName(), method.GetFunction(language));
                clsFunctions.put(TruffleString.fromJavaStringUncached(method.GetName().toString() + "_dyn", TruffleString.Encoding.UTF_8), method.GetDynFunction(language));
            }
            for (var method : superClass.AllMethods) {
                clsFunctions.put(method.GetName(), method.GetFunction(language));
                clsFunctions.put(TruffleString.fromJavaStringUncached(method.GetName().toString() + "_dyn", TruffleString.Encoding.UTF_8), method.GetDynFunction(language));
            }
            this.AllFields.addAll(superClass.AllFields);
        }

        //constructors
        for (var constructor : Constructors) {
            ctorFunctions.put(constructor.GetArgCount(), constructor.GetFunction(language));
            clsFunctions.put(TruffleString.fromJavaStringUncached(constructor.GetName().toString() + "_dyn", TruffleString.Encoding.UTF_8), constructor.GetDynFunction(language));
        }
        //static methods
        for (var method : StaticMethods) {
            clsFunctions.put(method.GetName(), method.GetFunction(language));
            clsFunctions.put(TruffleString.fromJavaStringUncached(method.GetName().toString() + "_dyn", TruffleString.Encoding.UTF_8), method.GetDynFunction(language));
        }
        //methods
        for (var method : Methods) {
            clsFunctions.put(method.GetName(), method.GetFunction(language));
            clsFunctions.put(TruffleString.fromJavaStringUncached(method.GetName().toString() + "_dyn", TruffleString.Encoding.UTF_8), method.GetDynFunction(language));
        }

        AllFields.addAll(Fields);
        AllMethods.addAll(Methods);
    }
}
