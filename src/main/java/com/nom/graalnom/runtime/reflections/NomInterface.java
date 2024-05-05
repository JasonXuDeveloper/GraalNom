package com.nom.graalnom.runtime.reflections;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class NomInterface extends NomNamed {
    public List<NomMethod> Methods;
    public List<NomMethod> AllMethods;
    public long TypeArgs;
    public long SuperInterfaces;

    public NomInterface(long name, long typeArgs, long superInterfaces) {
        super(name);
        this.TypeArgs = typeArgs;
        this.SuperInterfaces = superInterfaces;
        this.Methods = new java.util.ArrayList<>();
        this.AllMethods = new java.util.ArrayList<>();
    }

    public static void RegisterClass(String name, NomInterface cls) {
        NomContext.classes.put(name, cls);
    }


    public NomMethod AddMethod(String name, String qname, long typeArgs, long returnType, long argTypes, int regCount, boolean isFinal) {
        NomMethod method = new NomMethod(name, this, qname, returnType, typeArgs, argTypes, regCount, false, isFinal);
        Methods.add(method);
        return method;
    }

    public HashSet<NomInterface> superInterfaces = new HashSet<>();
    protected boolean registered = false;


    public void Register(NomLanguage language) {
        //copy methods from superclass
        NomSuperInterfacesConstant sc = NomContext.constants.GetSuperInterfaces(SuperInterfaces);
        sc.entries.forEach(pair -> {
            // (classNameId, typeListId)
            long classNameId = pair.getLeft();
            String className = NomContext.constants.GetInterface((int) classNameId).GetName();
            NomInterface inter = NomContext.classes.get(className);
            if (inter != null) {
                if (!inter.registered) {
                    inter.Register(language);
                    inter.registered = true;
                    superInterfaces.addAll(inter.superInterfaces);
                }

                superInterfaces.add(inter);
            }
        });
        //TODO use sc.ClassNameId with sc.TypeArgList to find methods and add
//        var clsFunctions = NomContext.functionsObject.computeIfAbsent(this, k -> new HashMap<>());
//        //methods
//        for (var method : Methods) {
//            clsFunctions.put(method.GetName(), method.GetFunction(language));
//        }
        //we should not put interface method to method table -> just let its inheritor to put it in their method table
    }
}
