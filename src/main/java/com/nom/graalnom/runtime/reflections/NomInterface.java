package com.nom.graalnom.runtime.reflections;

import java.util.List;

public class NomInterface extends NomNamed{
    public List<NomCallable> Methods;
    public NomInterface(long name) {
        super(name);
        this.Methods = new java.util.ArrayList<>();
    }
}
