package com.nom.graalnom.runtime.reflections;

import java.util.List;

public class NomStaticMethod {
    public String name;
    public String qName;
    public short regCount;
    public long argTypesId;
    private long returnType;
    private NomType returnTypeBuf;

    public NomStaticMethod(String name, NomClass parent, String qname, long returnType, long typeArgs, long arguments, long regcount, boolean declOnly){
        this.returnType = returnType;
        this.name = name;
        this.qName = qname;
        this.regCount = (short)regcount;
        this.argTypesId = arguments;
//        this.returnTypeBuf = new NomType(returnType);
    }
}
