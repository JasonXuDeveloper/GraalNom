package com.nom.graalnom.runtime.reflections;


import java.util.List;

public abstract class NomField {
    public abstract NomType GetType();
    public abstract String GetName();
    public abstract boolean IsReadOnly();
    public abstract boolean IsVolatile();
    public abstract Visibility GetVisibility();
    public abstract void PushDependencies(List<Long> set);

    /*
    virtual NomValue GenerateRead(NomBuilder &builder, CompileEnv* env, NomValue receiver) const = 0;
    virtual void GenerateWrite(NomBuilder &builder, CompileEnv* env, NomValue receiver, NomValue value) const = 0;
     */
}
