package com.nom.graalnom.parser;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.LittleEndianDataInputStream;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.datatypes.NomString;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.nom.graalnom.runtime.reflections.NomTypedField;
import com.nom.graalnom.runtime.reflections.Visibility;
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
                long localConstId = 0;
                switch (nextType) {
                    case StringConstant:
                        localConstId = s.readLong();
                        TruffleString str = NomString.create(s);
                        constants.put(localConstId, NomContext.constants.AddString(str, TryGetGlobalId(constants, localConstId)));
                        break;
                    case ClassConstant:
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddClass(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                TryGetGlobalId(constants, localConstId)));
                        break;
                    case SuperClass:
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddSuperClass(
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
                        constants.put(localConstId, NomContext.constants.AddSuperInterfaceList(
                                args,
                                TryGetGlobalId(constants, localConstId)));
                        break;
                    case ClassTypeConstant:
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddClassType(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                TryGetGlobalId(constants, localConstId)));
                        break;
                    case MethodConstant:
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddMethod(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                TryGetGlobalId(constants, localConstId)));
                        break;
                    case Class:
                        ReadClass(s, constants);
                        break;
                    case null:
                    default:
                        throw new IllegalArgumentException("unknown type (" + b + "): " + nextType);
                }
                System.out.println("Read " + nextType + " " + localConstId + " -> ");
                NomContext.constants.Get(constants.get(localConstId).intValue()).Print(true);
                System.out.println();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static NomClass ReadClass(LittleEndianDataInputStream s, Map<Long, Long> constants) throws Exception {
        long nameId = GetGlobalId(constants, s.readLong());
        long typeArgsId = GetGlobalId(constants, s.readLong());
        byte visibility = s.readByte();
        byte flags = s.readByte();
        long superInterfacesId = GetGlobalId(constants, s.readLong());
        long superClassId = GetGlobalId(constants, s.readLong());
        NomClass cls = new NomClass(nameId, typeArgsId, superClassId, superInterfacesId);
        long methodCount = s.readLong();
        while (methodCount > 0) {
            //ReadMethod(cls);
            methodCount--;
        }
        long fieldCount = s.readLong();
        while (fieldCount > 0) {
            ReadField(s, cls, constants);
            fieldCount--;
        }
        long staticMethodCount = s.readLong();
        while (staticMethodCount > 0) {
            //ReadMethod(cls);
            staticMethodCount--;
        }
        return cls;
    }

    private static void ReadStaticMethod(LittleEndianDataInputStream s, NomClass cls, Map<Long, Long> constants) throws Exception {
        if (s.read() != BytecodeInternalElementType.StaticMethod.getValue()) {
            throw new IllegalArgumentException("Expected static method, but did not encounter static method marker");
        }
        long name = GetGlobalId(constants, s.readLong());
        long typeArgs = GetGlobalId(constants, s.readLong());
        long returnType = GetGlobalId(constants, s.readLong());
        long argTypes = GetGlobalId(constants, s.readLong());
        String nameStr = NomContext.constants.GetString(name).GetText().toString();
        boolean isFinal = s.readBoolean();
        int regCount = s.readInt();
        String qNameStr = cls.GetName().toString() + "." + nameStr;
        NomStaticMethod meth = cls.AddStaticMethod(nameStr, qNameStr, typeArgs, returnType, argTypes, regCount);
        long instructionCount = s.readLong();
        while (instructionCount > 0) {
            /*
            auto instr = ReadInstruction();
            if (NomVerbose)
            {
                instr->Print(true);
            }
            meth->AddInstruction(instr);
             */
            instructionCount--;
        }
    }

    private static void ReadField(LittleEndianDataInputStream s, NomClass cls, Map<Long, Long> constants) throws Exception {
        if (s.read() != BytecodeInternalElementType.Field.getValue()) {
            throw new IllegalArgumentException("Expected field, but did not encounter field marker");
        }
        long name = GetGlobalId(constants, s.readLong());
        long type = GetGlobalId(constants, s.readLong());
        byte visibility = s.readByte();
        byte flags = s.readByte();
        cls.AddField(new NomTypedField(cls, name, type, Visibility.fromValue(visibility), (flags & 1) == 1, (flags & 2) == 2));
    }

    private static long TryGetGlobalId(Map<Long, Long> constants, long id) {
        if (!constants.containsKey(id)) {
            return 0;
        }

        return constants.get(id);
    }

    private static long GetGlobalId(Map<Long, Long> constants, long id) {
        if (!constants.containsKey(id)) {
            constants.put(id, NomContext.constants.GetConstantId());
        }

        return constants.get(id);
    }
}
