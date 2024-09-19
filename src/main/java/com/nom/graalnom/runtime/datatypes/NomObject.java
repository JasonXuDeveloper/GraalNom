package com.nom.graalnom.runtime.datatypes;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomClassConstant;
import com.nom.graalnom.runtime.constants.NomClassTypeConstant;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.nom.graalnom.runtime.reflections.NomTypedField;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.interop.*;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.DynamicObjectLibrary;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.api.strings.TruffleString;

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

    public final Map<TruffleString, NomFunction> methodTable;

    public NomObject(NomClass cls) {
        this.cls = cls;
        this.Id = nextId++;
        this.methodTable = NomContext.functionsObject.get(cls);
        if(this.methodTable != null){
            for (NomFunction func : this.methodTable.values()) {
                if(func.getName().toString().endsWith(".")){
                    this.thisFunction = func;
                    return;
                }
            }
        }
    }

    public NomClass GetClass() {
        return cls;
    }

    public NomFunction GetFunction(TruffleString name) {
        if (methodTable == null) {
            return null;
        }
        return methodTable.get(name);
    }

    public NomFunction thisFunction;

    public final Map<TruffleString, Object> objectMap = new java.util.HashMap<>();

    public Object readMember(TruffleString name)
            throws UnknownIdentifierException {
        Object result = objectMap.get(name);
        if (result == null) {
            NomTypedField f = (NomTypedField) cls.GetField(name);
            if (f != null) {
                NomClassTypeConstant type = f.GetTypeConstant();
                NomClassConstant typeCls = type.GetClass();
                if (typeCls.GetName().equals(TruffleString.fromConstant("Int_0", TruffleString.Encoding.UTF_8))) {
                    objectMap.put(name, 0L);
                    return 0L;
                } else if (typeCls.GetName().equals(TruffleString.fromConstant("Float_0", TruffleString.Encoding.UTF_8))) {
                    objectMap.put(name, 0.0);
                    return 0.0;
                } else if (typeCls.GetName().equals(TruffleString.fromConstant("Bool_0", TruffleString.Encoding.UTF_8))) {
                    objectMap.put(name, false);
                    return false;
                } else {
                    CompilerDirectives.shouldNotReachHere("Unknown type: " + typeCls.GetName());
                    return 0;
                }
            }
            /* Property does not exist. */
            throw UnknownIdentifierException.create(name.toString());
        }
//        System.out.println("readMember: " + name + " = " + result);
        return result;
    }

    public void writeMember(TruffleString name, Object value) {
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
