package com.nom.graalnom.runtime;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.constants.*;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.strings.TruffleString;
import org.graalvm.collections.Pair;

import java.util.List;

public class NomContext {
    private final NomLanguage language;
    @CompilerDirectives.CompilationFinal
    private TruffleLanguage.Env env;

    public NomContext(NomLanguage language, TruffleLanguage.Env env) {
        this.language = language;
        this.env = env;
    }



    public static List<NomConstant> constants = new java.util.ArrayList<>(1);

    public static void PrintConstant(long constant, boolean resolve) {
        System.out.print("$" + constant);
        if (resolve && constant > 0) {
            System.out.print("(");
            var c = constants.get((int) constant);
            if (c != null) {
                c.Print(resolve);
            }
            else {
                System.out.print("null");
            }
            System.out.print(")");
        }
    }

    public static long GetConstantId() {
        constants.add(null);
        return constants.size() - 1;
    }

    public static long AddString(TruffleString string, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomStringConstant(string));
        return cid;
    }

    public static long AddClass(long library, long name, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomClassConstant(library, name));
        return cid;
    }

    public static long AddSuperClass(long cls, long args, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomSuperClassConstant(cls, args));
        return cid;
    }

    public static long AddSuperInterfaceList(List<Pair<Long,Long>> entries, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomSuperInterfacesConstant(entries));
        return cid;
    }

    public static long AddClassType(long cls, long args, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomClassTypeConstant(cls, args));
        return cid;
    }

    public static long AddMethod(long cls, long name, long typeArgs, long argTypes, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomMethodConstant(cls, name, typeArgs, argTypes));
        return cid;
    }
}
