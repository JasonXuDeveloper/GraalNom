package com.nom.graalnom.runtime.constants;

import com.oracle.truffle.api.strings.TruffleString;
import org.graalvm.collections.Pair;

import java.util.Collections;
import java.util.List;

public class NomConstants {
    private final List<NomConstant> constants = new java.util.ArrayList<>(1);

    public final List<NomConstant> Constants() {
        return Collections.unmodifiableList(constants);
    }

    public NomConstant Get(int index) {
        return constants.get(index);
    }

    public void PrintConstant(long constant, boolean resolve) {
        System.out.print("$" + constant);
        if (resolve && constant > 0) {
            System.out.print("(");
            var c = constants.get((int) constant);
            if (c != null) {
                c.Print(resolve);
            } else {
                System.out.print("null");
            }
            System.out.print(")");
        }
    }

    public long GetConstantId() {
        constants.add(null);
        return constants.size() - 1;
    }

    public NomStringConstant GetString(long constant) {
        var cnstnt = constants.get((int) constant);
        if (cnstnt == null || cnstnt.Type != NomConstantType.CTString) {
            throw new RuntimeException();
        }
        return (NomStringConstant) cnstnt;
    }

    public NomMethodConstant GetMethod(long constant) {
        var cnstnt = constants.get((int) constant);
        if (cnstnt == null || cnstnt.Type != NomConstantType.CTMethod) {
            throw new RuntimeException();
        }
        return (NomMethodConstant) cnstnt;
    }

    public NomStaticMethodConstant GetStaticMethod(long constant) {
        var cnstnt = constants.get((int) constant);
        if (cnstnt == null || cnstnt.Type != NomConstantType.CTStaticMethod) {
            throw new RuntimeException();
        }
        return (NomStaticMethodConstant) cnstnt;
    }

    public NomClassConstant GetClass(long constant) {
        var cnstnt = constants.get((int) constant);
        if (cnstnt == null || cnstnt.Type != NomConstantType.CTClass) {
            return null;
        }
        return (NomClassConstant) cnstnt;
    }

    public NomClassTypeConstant GetClassType(long constant) {
        var cnstnt = constants.get((int) constant);
        if (cnstnt == null || cnstnt.Type != NomConstantType.CTClassType) {
            return null;
        }
        return (NomClassTypeConstant) cnstnt;
    }

    public NomTypeListConstant GetTypeList(long constant) {
        var cnstnt = constants.get((int) constant);
        if (cnstnt == null || cnstnt.Type != NomConstantType.CTTypeList) {
            return null;
        }
        return (NomTypeListConstant) cnstnt;
    }

    public NomSuperClassConstant GetSuperClass(long constant) {
        var cnstnt = constants.get((int) constant);
        if (cnstnt == null || cnstnt.Type != NomConstantType.CTSuperClass) {
            return null;
        }
        return (NomSuperClassConstant) cnstnt;
    }

    public long AddString(TruffleString string, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomStringConstant(string));
        return cid;
    }

    public long AddClass(long library, long name, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomClassConstant(library, name));
        return cid;
    }

    public long AddSuperClass(long cls, long args, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomSuperClassConstant(cls, args));
        return cid;
    }

    public long AddSuperInterfaceList(List<Pair<Long, Long>> entries, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomSuperInterfacesConstant(entries));
        return cid;
    }

    public long AddClassType(long cls, long args, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomClassTypeConstant(cls, args));
        return cid;
    }

    public long AddMethod(long cls, long name, long typeArgs, long argTypes, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomMethodConstant(cls, name, typeArgs, argTypes));
        return cid;
    }

    public long AddTypeList(List<Long> entries, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomTypeListConstant(entries));
        return cid;
    }

    public long AddStaticMethod(long cls, long name, long typeArgs, long argTypes, long cid) {
        if (cid == 0) {
            cid = GetConstantId();
        }
        constants.set((int) cid, new NomStaticMethodConstant(cls, name, typeArgs, argTypes));
        return cid;
    }
}
