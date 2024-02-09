package com.nom.graalnom.parser;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.LittleEndianDataInputStream;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.NomString;
import com.oracle.truffle.api.strings.TruffleString;
import org.graalvm.collections.Pair;

public class ByteCodeReader {
    public static int BYTECODE_VERSION = 2;

    public static void ReadBytecodeFile(String filename) throws IllegalArgumentException {
        if (filename == null || !Files.exists(Paths.get(filename))) {
            throw new IllegalArgumentException("file not found");
        }

        try (FileInputStream fs = new FileInputStream(filename)) {
            LittleEndianDataInputStream s = new LittleEndianDataInputStream(fs);
            int ver = s.readInt();
            if (ver > BYTECODE_VERSION) {
                throw new IllegalArgumentException("file" + filename + " (ver" + ver + ") is too new");
            }
            HashMap<Long, Long> constants = new HashMap<>();//local id -> global id
            while (s.available() > 0) {
                int b = s.read();//need to use int to get 0-255, I HATE JAVA
                BytecodeTopElementType nextType = BytecodeTopElementType.fromValue(b);
                long localConstId;
                switch (nextType) {
                    case StringConstant:
                        localConstId = s.readLong();
                        TruffleString str = NomString.create(s);
                        constants.put(localConstId, NomContext.AddString(str, TryGetGlobalId(constants, localConstId)));
                        break;
                    case ClassConstant:
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.AddClass(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                TryGetGlobalId(constants, localConstId)));
                        break;
                    case SuperClass:
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.AddSuperClass(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                TryGetGlobalId(constants, localConstId)));
                        break;
                    case CTSuperInterfaceList:
                        localConstId = s.readLong();
                        long count = s.readLong();
                        List<Pair<Long, Long>> args = new java.util.ArrayList<>();
                        while (count-- > 0) {
                            // (classNameId, typeListId)
                            args.addLast(Pair.create(
                                    GetGlobalId(constants, s.readLong()),
                                    GetGlobalId(constants, s.readLong())));
                        }
                        constants.put(localConstId, NomContext.AddSuperInterfaceList(
                                args,
                                TryGetGlobalId(constants, localConstId)));
                        break;
                    case ClassTypeConstant:
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.AddClassType(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                TryGetGlobalId(constants, localConstId)));
                        break;
                    case MethodConstant:
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.AddMethod(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                TryGetGlobalId(constants, localConstId)));
                        break;
                    case null:
                    default:
                        throw new IllegalArgumentException("unknown type (" + b + "): " + nextType);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static long TryGetGlobalId(Map<Long, Long> constants, long id) {
        if (!constants.containsKey(id)) {
            return 0;
        }

        return constants.get(id);
    }

    private static long GetGlobalId(Map<Long, Long> constants, long id) {
        if (!constants.containsKey(id)) {
            constants.put(id, NomContext.GetConstantId());
        }

        return constants.get(id);
    }
}
