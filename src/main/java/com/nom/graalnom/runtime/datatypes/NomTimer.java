package com.nom.graalnom.runtime.datatypes;


public class NomTimer extends NomObject{
    public long curMs;

    public NomTimer(long curMs) {
        super(null);
        this.curMs = curMs;
    }
}
