package com.nom.graalnom.parser;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.LittleEndianDataInputStream;
import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.datatypes.NomString;
import com.nom.graalnom.runtime.nodes.NomRootNode;
import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomBlockNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomFunctionBodyNode;
import com.nom.graalnom.runtime.nodes.expression.NomLongLiteralNode;
import com.nom.graalnom.runtime.nodes.local.NomReadRegisterNodeGen;
import com.nom.graalnom.runtime.nodes.local.NomWriteRegisterNodeGen;
import com.nom.graalnom.runtime.nodes.expression.binary.NomAddNodeGen;
import com.nom.graalnom.runtime.reflections.*;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.strings.TruffleString;
import org.graalvm.collections.Pair;

public class ByteCodeReader {
    public static int BYTECODE_VERSION = 2;

    public static void ReadBytecodeFile(NomLanguage language, String filename, Boolean debug) throws IllegalArgumentException {
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
                long localConstId = -1;
                if (debug)
                    System.out.println("Read " + nextType);
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
                        List<Pair<Long, Long>> args = new ArrayList<>();
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
                        ReadClass(s, constants, language);
                        break;
                    case None:
                        return;
                    case null:
                    default:
                        throw new IllegalArgumentException("unknown type (" + b + "): " + nextType);
                }
                if (constants.containsKey(localConstId) && debug) {
                    System.out.println("Local id -> Global id: " + localConstId + " -> " + constants.get(localConstId));
                    NomContext.constants.Get(constants.get(localConstId).intValue()).Print(true);
                    System.out.println();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static NomStatementNode ReadInstruction(LittleEndianDataInputStream s, Map<Long, Long> constants) throws Exception {
        int opCodeVal = s.read();
        OpCode opCode = OpCode.fromValue(opCodeVal);
        long nameId;
        int regIndex;//output register
        int receiverRegIndex;
        switch (opCode) {
            case Noop:
                break;
            case ReturnVoid:
                break;
            case EnsureCheckedMethod:
                nameId = s.readLong();
                receiverRegIndex = s.readInt();
                System.out.println("EnsureCheckedMethod " +
                        NomContext.constants.GetString(GetGlobalId(constants, nameId)).GetText() +
                        " -> reg " + receiverRegIndex);
                break;
            case LoadIntConstant:
                long longVal = s.readLong();
                regIndex = s.readInt();
                System.out.println("LoadIntConstant " + longVal + " -> reg " + regIndex);
                return NomWriteRegisterNodeGen.create(new NomLongLiteralNode(longVal), regIndex);
            case InvokeCheckedInstance:
                nameId = s.readLong();
                long typeArgsId = s.readLong();
                regIndex = s.readInt();
                receiverRegIndex = s.readInt();
                System.out.println("InvokeCheckedInstance " +
                        NomContext.constants.GetMethod(GetGlobalId(constants, nameId)).MethodName() +
                        " -> reg " + regIndex + " reg receiver " + receiverRegIndex);
                break;
            case BinOp:
                BinOperator op = BinOperator.fromValue(s.readByte());
                int leftRegIndex = s.readInt();
                int rightRegIndex = s.readInt();
                regIndex = s.readInt();
                System.out.println("BinOp " + op + " reg " + leftRegIndex + " reg " + rightRegIndex + " -> reg " + regIndex);
                switch (op) {
                    case Add:
                        return NomWriteRegisterNodeGen.create(
                                NomAddNodeGen.create(
                                        NomReadRegisterNodeGen.create(leftRegIndex),
                                        NomReadRegisterNodeGen.create(rightRegIndex)
                                ), regIndex);
                    case null:
                    default:
                        return null;
//                        throw new IllegalStateException("Unexpected value: " + op);
                }
            case null:
            default:
                throw new IllegalStateException("Unexpected value: " + opCode);
        }
        return null;
    }

    public static NomClass ReadClass(LittleEndianDataInputStream s, Map<Long, Long> constants, NomLanguage language) throws Exception {
        long lId = s.readLong();
        long nameId = GetGlobalId(constants, lId);
        long typeArgsId = GetGlobalId(constants, s.readLong());
        byte visibility = s.readByte();
        byte flags = s.readByte();
        long superInterfacesId = GetGlobalId(constants, s.readLong());
        long superClassId = GetGlobalId(constants, s.readLong());
        NomClass cls = new NomClass(nameId, typeArgsId, superClassId, superInterfacesId);
        NomClass.RegisterClass(NomContext.constants.GetString(nameId).GetText().toString(), cls);
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
            ReadStaticMethod(s, cls, constants, language);
            staticMethodCount--;
        }
        /*
        uint64_t constructorCount = stream.read<uint64_t>();
			while (constructorCount > 0) {
				ReadConstructor(cls);
				constructorCount--;
			}
			uint64_t lambdaCount = stream.read<uint64_t>();
			while (lambdaCount > 0) {
				ReadLambda(cls);
				lambdaCount--;
			}
			uint64_t structCount = stream.read<uint64_t>();
			while (structCount > 0) {
				ReadStruct(cls);
				structCount--;
			}
			if (handler != nullptr)
			{
				handler->ReadClass(cls);
			}
         */
        return cls;
    }

    public static NomStaticMethod ReadStaticMethod(LittleEndianDataInputStream s, NomClass cls, Map<Long, Long> constants, NomLanguage language) throws Exception {
        if (s.read() != BytecodeInternalElementType.StaticMethod.getValue()) {
            throw new IllegalArgumentException("Expected static method, but did not encounter static method marker");
        }
        long nameId = GetGlobalId(constants, s.readLong());
        long typeArgs = GetGlobalId(constants, s.readLong());
        long returnType = GetGlobalId(constants, s.readLong());
        long argTypes = GetGlobalId(constants, s.readLong());
        String nameStr = NomContext.constants.GetString(nameId).GetText().toString();
        int regCount = s.readInt();
        String qNameStr = cls.GetName().toString() + "." + nameStr;
        NomStaticMethod meth = cls.AddStaticMethod(nameStr, qNameStr, typeArgs, returnType, argTypes, regCount);
        long instructionCount = s.readLong();
        System.out.println("ReadStaticMethod " + qNameStr + " with " + instructionCount +
                " instructions that require " + regCount + " registers");
        List<NomStatementNode> instructions = new ArrayList<>();
        while (instructionCount > 0) {
            NomStatementNode instr = ReadInstruction(s, constants);
            instructionCount--;
            if (instr == null) {
                continue;
            }
            instructions.add(instr);
            /*
            if (NomVerbose)
            {
                instr->Print(true);
            }
             */
            //        if(instruction.GetOpCode() == OpCode.PhiNode){
            //            phiNodes.add((PhiNode)instruction);
            //        }
        }
        NomBlockNode block = new NomBlockNode(instructions.toArray(new NomStatementNode[0]));
        NomFunctionBodyNode body = new NomFunctionBodyNode(block);
        FrameDescriptor.Builder builder = FrameDescriptor.newBuilder();
        for (int i = 0; i < regCount; i++) {
            builder.addSlot(FrameSlotKind.Illegal, "reg" + i, null);
        }
        NomRootNode root = new NomRootNode(language, builder.build(), body, NomContext.constants.GetString(nameId).GetText());

        System.out.println();
        System.out.println("RootNode: " + root);

        NomFunction func = new NomFunction(NomContext.constants.GetString(nameId).GetText(), root.getCallTarget());
        var clsFunctions = NomContext.functionsObject.computeIfAbsent(cls, k -> new HashMap<>());
        clsFunctions.put(NomContext.constants.GetString(nameId).GetText().toString(), func);
        return meth;
    }

    public static NomTypedField ReadField(LittleEndianDataInputStream s, NomClass cls, Map<Long, Long> constants) throws Exception {
        if (s.read() != BytecodeInternalElementType.Field.getValue()) {
            throw new IllegalArgumentException("Expected field, but did not encounter field marker");
        }
        long name = GetGlobalId(constants, s.readLong());
        long type = GetGlobalId(constants, s.readLong());
        byte visibility = s.readByte();
        byte flags = s.readByte();
        return cls.AddField(name, type, Visibility.fromValue(visibility), (flags & 1) == 1, (flags & 2) == 2);
    }

    public static long TryGetGlobalId(Map<Long, Long> constants, long id) {
        if (!constants.containsKey(id)) {
            return 0;
        }

        return constants.get(id);
    }

    public static long GetGlobalId(Map<Long, Long> constants, long id) {
        if (!constants.containsKey(id)) {
            constants.put(id, NomContext.constants.GetConstantId());
        }

        return constants.get(id);
    }
}
