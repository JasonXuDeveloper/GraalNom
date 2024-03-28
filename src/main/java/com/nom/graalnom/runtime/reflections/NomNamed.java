package com.nom.graalnom.runtime.reflections;

import com.nom.graalnom.runtime.NomContext;

public abstract class NomNamed {
    protected final long name;

    public NomNamed(long name){
        this.name = name;
    }

    public String GetName(){
        return NomContext.constants.GetString(name).Value();
    }
}
