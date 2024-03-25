package com.nom.graalnom.runtime.reflections;

public class NomMethod extends NomCallable{
    private long returnType;
    private boolean isFinal;
    public NomInterface declaringClass;

    public NomMethod(String name, NomInterface parent, String qname, long returnType, long typeArgs, long arguments, long regcount, boolean declOnly, boolean isFinal){
        super(name, qname, regcount, typeArgs, arguments, declOnly);
        this.returnType = returnType;
        this.declaringClass = parent;
        this.isFinal = isFinal;
    }

    @Override
    public int GetArgCount() {
        return super.GetArgCount() + 1;
    }
}
