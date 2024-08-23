package com.nom.graalnom.runtime.reflections;

import com.oracle.truffle.api.strings.TruffleString;

public class NomMethod extends NomCallable{
    private long returnType;
    private boolean isFinal;
    public NomInterface declaringClass;

    public NomMethod(TruffleString name, NomInterface parent, TruffleString qname, long returnType, long typeArgs, long arguments, long regcount, boolean declOnly, boolean isFinal){
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
