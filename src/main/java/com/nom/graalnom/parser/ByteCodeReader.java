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
import com.nom.graalnom.runtime.constants.NomMethodConstant;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.datatypes.NomString;
import com.nom.graalnom.runtime.nodes.NomRootNode;
import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomBlockNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomFunctionBodyNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomReturnNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.NomInvokeNode;
import com.nom.graalnom.runtime.nodes.expression.binary.*;
import com.nom.graalnom.runtime.nodes.expression.literal.*;
import com.nom.graalnom.runtime.nodes.expression.NomNoopNode;
import com.nom.graalnom.runtime.nodes.local.NomReadRegisterNode;
import com.nom.graalnom.runtime.nodes.local.NomReadRegisterNodeGen;
import com.nom.graalnom.runtime.nodes.local.NomWriteRegisterNodeGen;
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
                if (nextType == BytecodeTopElementType.None) return;
                long localConstId = -1;
                if (debug)
                    System.out.println("Read " + nextType);
                switch (nextType) {
                    case StringConstant -> {
                        localConstId = s.readLong();
                        TruffleString str = NomString.create(s);
                        constants.put(localConstId, NomContext.constants.AddString(str, TryGetGlobalId(constants, localConstId)));
                    }
                    case ClassConstant -> {
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddClass(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                TryGetGlobalId(constants, localConstId)));
                    }
                    case SuperClass -> {
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddSuperClass(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                TryGetGlobalId(constants, localConstId)));
                    }
                    case CTSuperInterfaceList -> {
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
                    }
                    case ClassTypeConstant -> {
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddClassType(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                TryGetGlobalId(constants, localConstId)));
                    }
                    case MethodConstant -> {
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddMethod(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
                                TryGetGlobalId(constants, localConstId)));
                    }
                    case Class -> ReadClass(s, constants, language);
                    case null, default -> throw new IllegalArgumentException("unknown type (" + b + "): " + nextType);
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
        long typeArgsId;
        int regIndex;//output register
        int receiverRegIndex;
        switch (opCode) {
            case Noop -> {
                return new NomNoopNode();
            }
            case Return -> {
                System.out.println("Return");
                return new NomReturnNode(NomReadRegisterNodeGen.create(s.readInt()));
            }
            case ReturnVoid -> {
                System.out.println("ReturnVoid");
                return new NomReturnNode(null);
            }
            case EnsureCheckedMethod -> {
                nameId = s.readLong();
                receiverRegIndex = s.readInt();
                System.out.println("EnsureCheckedMethod " +
                        NomContext.constants.GetString(GetGlobalId(constants, nameId)).GetText() +
                        " -> reg " + receiverRegIndex);
                //can be omitted since method check already exists in invoke
            }
            case LoadIntConstant -> {
                long longVal = s.readLong();
                regIndex = s.readInt();
                System.out.println("LoadIntConstant " + longVal + " -> reg " + regIndex);
                return NomWriteRegisterNodeGen.create(new NomLongLiteralNode(longVal), regIndex);
            }
            case LoadFloatConstant -> {
                double floatVal = s.readDouble();
                regIndex = s.readInt();
                System.out.println("LoadFloatConstant " + floatVal + " -> reg " + regIndex);
                return NomWriteRegisterNodeGen.create(new NomDoubleLiteralNode(floatVal), regIndex);
            }
            case LoadBoolConstant -> {
                boolean boolVal = s.readBoolean();
                regIndex = s.readInt();
                System.out.println("LoadBoolConstant " + boolVal + " -> reg " + regIndex);
                return NomWriteRegisterNodeGen.create(new NomBoolLiteralNode(boolVal), regIndex);
            }
            case LoadNullConstant -> {
                regIndex = s.readInt();
                System.out.println("LoadNullConstant -> reg " + regIndex);
                return NomWriteRegisterNodeGen.create(new NomNullLiteralNode(), regIndex);
            }
            case LoadStringConstant -> {
                nameId = s.readLong();
                regIndex = s.readInt();
                System.out.println("LoadStringConstant " + NomContext.constants.GetString(GetGlobalId(constants, nameId)).GetText() + " -> reg " + regIndex);
                return NomWriteRegisterNodeGen.create(
                        new NomStringLiteralNode(
                                NomContext.constants.
                                        GetString(GetGlobalId(constants, nameId)).GetText()), regIndex);
            }
            case InvokeCheckedInstance -> {
                nameId = s.readLong();
                typeArgsId = s.readLong();
                regIndex = s.readInt();
                receiverRegIndex = s.readInt();
                NomMethodConstant method = NomContext.constants.GetMethod(GetGlobalId(constants, nameId));
                NomFunction func = NomContext.getMethod(method);
                if (func != null) {
                    System.out.println("InvokeCheckedInstance " +
                            method.ClassTypeConstant().Class().GetName() + "." + func.getName().toString() +
                            " -> reg " + regIndex + " receiver " + receiverRegIndex);
                    return NomWriteRegisterNodeGen.create(
                            new NomInvokeNode(func, new NomExpressionNode[]{
                                    NomReadRegisterNodeGen.create(receiverRegIndex)
                            }), regIndex);
                }

                throw new IllegalStateException("Method " + method.ClassTypeConstant().Class().GetName() + "." + method.MethodName().toString() + " not found");
            }
            case CallCheckedStatic -> {
                nameId = s.readLong();
                typeArgsId = s.readLong();
                regIndex = s.readInt();
                NomMethodConstant staticMethod = NomContext.constants.GetMethod(GetGlobalId(constants, nameId));
                NomFunction staticFunc = NomContext.getMethod(staticMethod);
                if (staticFunc != null) {
                    System.out.println("CallCheckedStatic " +
                            staticMethod.ClassTypeConstant().Class().GetName() + "." + staticFunc.getName().toString() +
                            " -> reg " + regIndex);
                    return NomWriteRegisterNodeGen.create(
                            new NomInvokeNode(staticFunc, new NomExpressionNode[0]), regIndex);
                }
            }
            case BinOp -> {
                BinOperator op = BinOperator.fromValue(s.readByte());
                int leftRegIndex = s.readInt();
                int rightRegIndex = s.readInt();
                regIndex = s.readInt();
                System.out.println("BinOp " + op + " reg " + leftRegIndex + " reg " + rightRegIndex + " -> reg " + regIndex);
                NomReadRegisterNode left = NomReadRegisterNodeGen.create(leftRegIndex);
                NomReadRegisterNode right = NomReadRegisterNodeGen.create(rightRegIndex);
                return switch (op) {
                    case Add -> NomWriteRegisterNodeGen.create(NomAddNodeGen.create(left, right), regIndex);
                    case Subtract -> NomWriteRegisterNodeGen.create(NomSubNodeGen.create(left, right), regIndex);
                    case Multiply -> NomWriteRegisterNodeGen.create(NomMulNodeGen.create(left, right), regIndex);
                    case Divide -> NomWriteRegisterNodeGen.create(NomDivNodeGen.create(left, right), regIndex);
                    case Mod -> NomWriteRegisterNodeGen.create(NomModNodeGen.create(left, right), regIndex);
                    case null, default -> null;
//                        throw new IllegalStateException("Unexpected value: " + op);
                };
            }
            case null, default -> throw new IllegalStateException("Unexpected value: " + opCode);
        }
        return null;
    }

    public static NomClass ReadClass(LittleEndianDataInputStream s, Map<Long, Long> constants, NomLanguage language) throws Exception {
        long nameId = GetGlobalId(constants, s.readLong());
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
                " instructions that require " + regCount + " registers, typeArgs " + typeArgs + ", returnType " + returnType + ", argTypes " + argTypes);
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

        TruffleString methName = NomString.create(qNameStr);
        NomRootNode root = new NomRootNode(language, builder.build(), body, methName);

        System.out.println();
        System.out.println("Parsed Interpreter Nodes: (size=" + instructions.size() + ")");
        System.out.println(root);
        System.out.println();

        NomFunction func = new NomFunction(methName, root.getCallTarget());
        var clsFunctions = NomContext.functionsObject.computeIfAbsent(cls, k -> new HashMap<>());
        clsFunctions.put(nameStr, func);
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
