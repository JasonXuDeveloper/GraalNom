package com.nom.graalnom.runtime.reflections;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomTypeListConstant;

import java.util.HashMap;

public class NomStaticMethod extends NomCallable {
    private long returnType;
    public NomClass declaringClass;

    public NomStaticMethod(String name, NomClass parent, String qname, long returnType, long typeArgs, long arguments, long regcount, boolean declOnly){
        super(name, qname, regcount, typeArgs, arguments, declOnly);
        this.returnType = returnType;
        this.declaringClass = parent;
    }
}
