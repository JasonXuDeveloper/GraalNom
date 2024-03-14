package com.nom.graalnom.runtime.reflections;

import com.nom.graalnom.runtime.NomContext;
import com.oracle.truffle.api.strings.TruffleString;

public abstract class NomNamed {
    protected final long name;

    public NomNamed(long name){
        this.name = name;
    }

    public TruffleString GetName(){
        return NomContext.constants.GetString(name).GetText();
    }
}
