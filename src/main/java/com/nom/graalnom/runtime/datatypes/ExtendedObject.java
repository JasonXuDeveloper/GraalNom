package com.nom.graalnom.runtime.datatypes;

import com.nom.graalnom.runtime.reflections.NomClass;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.object.Shape;

@ExportLibrary(InteropLibrary.class)
public class ExtendedObject extends NomObject {

    @DynamicField
    private Object _obj0;
    @DynamicField
    private Object _obj1;
    @DynamicField
    private Object _obj2;
    @DynamicField
    private long _long0;
    @DynamicField
    private long _long1;
    @DynamicField
    private long _long2;

    public ExtendedObject(Shape shape, NomClass cls) {
        super(shape, cls);
    }
}