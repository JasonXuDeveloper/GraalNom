package com.nom.graalnom.runtime.reflections;

import java.util.ArrayList;
import java.util.List;

public class NomConstructor extends NomCallable{
    public NomClass declaringClass;
    private List<Integer> superConstructorArgs;

    public NomConstructor(String name, NomClass parent, String qname, long typeArgs, long arguments, long regcount, boolean declOnly){
        super(name, qname, regcount, typeArgs, arguments, declOnly);
        this.declaringClass = parent;
        this.superConstructorArgs = new ArrayList<>();
    }

    public void AddSuperConstructorArg(int arg){
        superConstructorArgs.add(arg);
    }
}
