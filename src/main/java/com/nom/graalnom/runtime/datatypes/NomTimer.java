package com.nom.graalnom.runtime.datatypes;

import com.oracle.truffle.api.object.Shape;

public class NomTimer extends NomObject{
    public long curMs;

    public NomTimer(long curMs) {
        super(null);
        this.curMs = curMs;
    }
}
