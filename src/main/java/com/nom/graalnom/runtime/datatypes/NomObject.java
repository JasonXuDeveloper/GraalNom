package com.nom.graalnom.runtime.datatypes;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomClassConstant;
import com.nom.graalnom.runtime.constants.NomClassTypeConstant;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.nom.graalnom.runtime.reflections.NomTypedField;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.interop.*;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.DynamicObjectLibrary;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.api.strings.TruffleString;

@ExportLibrary(InteropLibrary.class)
public class NomObject extends DynamicObject implements TruffleObject {
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

    public NomObject(Shape shape, NomClass cls) {
        super(shape);
        this.cls = cls;
        this.Id = nextId++;
    }

    @ExportMessage
    boolean hasLanguage() {
        return true;
    }

    @ExportMessage
    Object readMember(String name,
                      @CachedLibrary("this") DynamicObjectLibrary objectLibrary)
            throws UnknownIdentifierException {
        Object result = objectLibrary.getOrDefault(this, name, null);
        if (result == null) {
            var s = cls.Fields.stream().filter(f ->
                    NomContext.constants.GetString(f.Name).toString().equals(name)).findFirst();
            if (s.isPresent()) {
                NomTypedField f = s.get();
                NomClassTypeConstant type = f.GetTypeConstant();
                NomClassConstant typeCls = type.GetClass();
                if (typeCls.GetName().equals("Int_0")) {
                    objectLibrary.putLong(this, name, 0);
                    return 0;
                } else if (typeCls.GetName().equals("Float_0")) {
                    objectLibrary.putDouble(this, name, 0.0);
                    return 0.0;
                } else if (typeCls.GetName().equals("Bool_0")) {
                    objectLibrary.put(this, name, false);
                    return false;
                } else {
                    objectLibrary.put(this, name, NomNull.SINGLETON);
                    return NomNull.SINGLETON;
                }
            }
            /* Property does not exist. */
            throw UnknownIdentifierException.create(name);
        }
        return result;
    }

    @ExportMessage
    void writeMember(String name, Object value,
                     @CachedLibrary("this") DynamicObjectLibrary objectLibrary) {
        objectLibrary.put(this, name, value);
    }


    @ExportMessage
    void removeMember(String member,
                      @Cached @Cached.Shared("fromJavaStringNode") TruffleString.FromJavaStringNode fromJavaStringNode,
                      @CachedLibrary("this") DynamicObjectLibrary objectLibrary) throws UnknownIdentifierException {
        TruffleString memberTS = fromJavaStringNode.execute(member, NomLanguage.STRING_ENCODING);
        if (objectLibrary.containsKey(this, memberTS)) {
            objectLibrary.removeKey(this, memberTS);
        } else {
            throw UnknownIdentifierException.create(member);
        }
    }

    @ExportMessage
    final boolean hasMembers() {
        return true;
    }

    @ExportMessage
    Object getMembers(@SuppressWarnings("unused") boolean includeInternal,
                      @CachedLibrary("this") DynamicObjectLibrary objectLibrary) {
        return new Keys(objectLibrary.getKeyArray(this));
    }

    @ExportMessage(name = "isMemberReadable")
    @ExportMessage(name = "isMemberModifiable")
    @ExportMessage(name = "isMemberRemovable")
    boolean existsMember(String member,
                         @Cached @Cached.Shared("fromJavaStringNode") TruffleString.FromJavaStringNode fromJavaStringNode,
                         @CachedLibrary("this") DynamicObjectLibrary objectLibrary) {
        return objectLibrary.containsKey(this, fromJavaStringNode.execute(member, NomLanguage.STRING_ENCODING));
    }

    @ExportMessage
    boolean isMemberInsertable(String member,
                               @CachedLibrary("this") InteropLibrary receivers) {
        return !receivers.isMemberExisting(this, member);
    }

    @ExportLibrary(InteropLibrary.class)
    static final class Keys implements TruffleObject {

        private final Object[] keys;

        Keys(Object[] keys) {
            this.keys = keys;
        }

        @ExportMessage
        Object readArrayElement(long index) throws InvalidArrayIndexException {
            if (!isArrayElementReadable(index)) {
                throw InvalidArrayIndexException.create(index);
            }
            return keys[(int) index];
        }

        @ExportMessage
        boolean hasArrayElements() {
            return true;
        }

        @ExportMessage
        long getArraySize() {
            return keys.length;
        }

        @ExportMessage
        boolean isArrayElementReadable(long index) {
            return index >= 0 && index < keys.length;
        }
    }

    @ExportMessage
    Class<? extends TruffleLanguage<?>> getLanguage() {
        return NomLanguage.class;
    }


    @ExportMessage
    final Object toDisplayString(boolean allowSideEffects) {
        return null;
    }
}
