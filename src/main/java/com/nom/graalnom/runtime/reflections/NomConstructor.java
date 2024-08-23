package com.nom.graalnom.runtime.reflections;

import com.oracle.truffle.api.strings.TruffleString;

import java.util.ArrayList;
import java.util.List;

public class NomConstructor extends NomCallable{
    public NomClass declaringClass;

    public NomConstructor(TruffleString name, NomClass parent, TruffleString qname, long typeArgs, long arguments, long regcount, boolean declOnly){
        super(name, qname, regcount, typeArgs, arguments, declOnly);
        this.declaringClass = parent;
    }


    @Override
    public int GetArgCount() {
        return super.GetArgCount() + 1;
    }
}
