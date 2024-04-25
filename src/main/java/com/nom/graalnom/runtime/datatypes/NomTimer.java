package com.nom.graalnom.runtime.datatypes;

import com.oracle.truffle.api.object.Shape;

public class NomTimer extends NomObject{
    public long curMs;

    public NomTimer(Shape shape, long curMs) {
        super(shape, null);
        this.curMs = curMs;
    }
}
