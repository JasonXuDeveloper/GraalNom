package com.nom.graalnom.runtime.reflections;

import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomClassTypeConstant;
import com.oracle.truffle.api.strings.TruffleString;

import java.util.List;

public class NomTypedField extends NomField {
    public long Name;
    public long Type;
    public NomClass Class;
    public int Index = -1;
    private final Visibility visibility;
    private final boolean readOnly;
    private final boolean isVolatile;

    public NomTypedField(NomClass cls, long name, long type, Visibility visibility, boolean readonly, boolean isvolatile) {
        Name = name;
        Type = type;
        Class = cls;
        this.visibility = visibility;
        readOnly = readonly;
        this.isVolatile = isvolatile;
    }

    public void SetIndex(int index) {
        if (Index < 0) {
            Index = index;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public NomType GetType() {
        /*
        NomSubstitutionContextMemberContext nscmc(Class);
        return NomConstants::GetType(&nscmc, Type);
         */
        return null;
    }

    public NomClassTypeConstant GetTypeConstant() {
        return NomContext.constants.GetClassType(Type);
    }

    @Override
    public TruffleString GetName() {
        return NomContext.constants.GetString(Name).GetText();
    }

    @Override
    public boolean IsReadOnly() {
        return readOnly;
    }

    @Override
    public boolean IsVolatile() {
        return isVolatile;
    }

    @Override
    public Visibility GetVisibility() {
        return visibility;
    }

    @Override
    public void PushDependencies(List<Long> set) {
        set.add(Name);
        set.add(Type);
    }
}
