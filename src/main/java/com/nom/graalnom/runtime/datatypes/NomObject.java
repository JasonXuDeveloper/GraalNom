package com.nom.graalnom.runtime.datatypes;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomClassConstant;
import com.nom.graalnom.runtime.constants.NomClassTypeConstant;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.nom.graalnom.runtime.reflections.NomTypedField;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.interop.*;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.DynamicObjectLibrary;
import com.oracle.truffle.api.object.Shape;

import java.util.Map;

public class NomObject {
    private final NomClass cls;
    private final long Id;
    private static long nextId = 0;


    public long GetId() {
        return Id;
    }

    @Override
    public int hashCode() {
        return (int) Id;
    }

    public final Map<String, NomFunction> methodTable;

    public NomObject(NomClass cls) {
        this.cls = cls;
        this.Id = nextId++;
        this.methodTable = NomContext.functionsObject.get(cls);
    }

    public NomClass GetClass() {
        return cls;
    }

    public NomFunction GetFunction(String name) {
        if(methodTable == null){
            return null;
        }
        return methodTable.get(name);
    }

    public final Map<String, Object> objectMap = new java.util.HashMap<>();

    public Object readMember(String name)
            throws UnknownIdentifierException {
        Object result = objectMap.get(name);
        if (result == null) {
            NomTypedField f = (NomTypedField) cls.GetField(name);
            if (f != null) {
                NomClassTypeConstant type = f.GetTypeConstant();
                NomClassConstant typeCls = type.GetClass();
                if (typeCls.GetName().equals("Int_0")) {
                    objectMap.put(name, 0L);
                    return 0L;
                } else if (typeCls.GetName().equals("Float_0")) {
                    objectMap.put(name, 0.0);
                    return 0.0;
                } else if (typeCls.GetName().equals("Bool_0")) {
                    objectMap.put(name, false);
                    return false;
                } else {
                    System.out.println("Unknown type: " + typeCls.GetName());
                    return 0;
                }
            }
            /* Property does not exist. */
            throw UnknownIdentifierException.create(name);
        }
//        System.out.println("readMember: " + name + " = " + result);
        return result;
    }

    public void writeMember(String name, Object value) {
        objectMap.put(name, value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NomObject && ((NomObject) obj).Id == Id;
    }

    @Override
    public String toString() {
        return "NomObject(" + Id + ")";
    }
}
