package com.nom.graalnom.parser;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;

import com.google.common.io.LittleEndianDataInputStream;
import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomMethodConstant;
import com.nom.graalnom.runtime.constants.NomStaticMethodConstant;
import com.nom.graalnom.runtime.constants.NomTypeListConstant;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.datatypes.NomString;
import com.nom.graalnom.runtime.nodes.NomRootNode;
import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomBlockNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomFunctionBodyNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomIfNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomReturnNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.NomInvokeNode;
import com.nom.graalnom.runtime.nodes.expression.binary.*;
import com.nom.graalnom.runtime.nodes.expression.literal.*;
import com.nom.graalnom.runtime.nodes.expression.NomNoopNode;
import com.nom.graalnom.runtime.nodes.expression.unary.NomNegateNodeGen;
import com.nom.graalnom.runtime.nodes.expression.unary.NomNotNodeGen;
import com.nom.graalnom.runtime.nodes.local.*;
import com.nom.graalnom.runtime.reflections.*;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.strings.TruffleString;
import org.graalvm.collections.Pair;

public class ByteCodeReader {
    public static int BYTECODE_VERSION = 2;

    private static final HashSet<String> loadedFiles = new HashSet<>();

    public static void ReadBytecodeFile(NomLanguage language, String filename, Boolean debug) throws Exception {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("file not found");
        }

        byte[] data = Files.readAllBytes(path);
        byte[] hash = MessageDigest.getInstance("MD5").digest(data);
        String checksum = new BigInteger(1, hash).toString(16);

        if (loadedFiles.contains(checksum)) {
            return;
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
                    case TypeListConstant -> {
                        localConstId = s.readLong();
                        int count = s.readInt();
                        List<Long> args = new ArrayList<>();
                        while (count-- > 0) {
                            args.addLast(GetGlobalId(constants, s.readLong()));
                        }
                        constants.put(localConstId, NomContext.constants.AddTypeList(
                                args,
                                TryGetGlobalId(constants, localConstId)));
                    }
                    case StaticMethodConstant -> {
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddStaticMethod(
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

        loadedFiles.add(checksum);
    }

    private static final List<NomExpressionNode> args = new ArrayList<>();
    private static boolean endOfBlock = false;
    private static final List<NomBlockNode> blocks = new ArrayList<>();
    private static NomBlockNode GetBlock(int index) {
        while (blocks.size() <= index) {
            blocks.add(new NomBlockNode(new NomStatementNode[0]));
        }
        return blocks.get(index);
    }

    public static NomStatementNode ReadInstruction(NomStaticMethod curMethod, LittleEndianDataInputStream s, Map<Long, Long> constants) throws Exception {
        int opCodeVal = s.read();
        OpCode opCode = OpCode.fromValue(opCodeVal);
        long nameId;
        long typeArgsId;//generic type arguments
        int regIndex;//output register
        int receiverRegIndex;//this instance register
        int curMethodArgCount = curMethod.ArgCount();//how many arguments (register offset)
        switch (opCode) {
            case Noop -> {
                return new NomNoopNode();
            }
            case Argument -> {
                args.add(ReadFromFrame(curMethodArgCount, s.readInt()));
            }
            case Return -> {
                endOfBlock = true;
                return new NomReturnNode(ReadFromFrame(curMethodArgCount, s.readInt()));
            }
            case ReturnVoid -> {
                endOfBlock = true;
                return new NomReturnNode(null);
            }
            case EnsureCheckedMethod -> {
                nameId = GetGlobalId(constants, s.readLong());
                receiverRegIndex = s.readInt();
            }
            case LoadIntConstant -> {
                long longVal = s.readLong();
                regIndex = s.readInt();
                return WriteToFrame(curMethodArgCount, regIndex, new NomLongLiteralNode(longVal));
            }
            case LoadFloatConstant -> {
                double floatVal = s.readDouble();
                regIndex = s.readInt();
                return WriteToFrame(curMethodArgCount, regIndex, new NomDoubleLiteralNode(floatVal));
            }
            case LoadBoolConstant -> {
                boolean boolVal = s.readBoolean();
                regIndex = s.readInt();
                return WriteToFrame(curMethodArgCount, regIndex, new NomBoolLiteralNode(boolVal));
            }
            case LoadNullConstant -> {
                regIndex = s.readInt();
                return WriteToFrame(curMethodArgCount, regIndex, new NomNullLiteralNode());
            }
            case LoadStringConstant -> {
                nameId = GetGlobalId(constants, s.readLong());
                regIndex = s.readInt();
                return WriteToFrame(curMethodArgCount, regIndex, new NomStringLiteralNode(
                        NomContext.constants.
                                GetString(nameId).GetText()));
            }
            case InvokeCheckedInstance -> {
                nameId = GetGlobalId(constants, s.readLong());
                typeArgsId = GetGlobalId(constants, s.readLong());
                regIndex = s.readInt();
                receiverRegIndex = s.readInt();
                NomMethodConstant method = NomContext.constants.GetMethod(nameId);
                NomExpressionNode[] methArgs = new NomExpressionNode[args.size() + 1];
                methArgs[0] = ReadFromFrame(curMethodArgCount, receiverRegIndex);
                for (int i = 0; i < args.size(); i++) {
                    methArgs[i + 1] = args.get(i);
                }
                NomExpressionNode ret = WriteToFrame(
                        curMethodArgCount, regIndex,
                        new NomInvokeNode(method, NomContext::getMethod, methArgs));
                args.clear();
                return ret;
            }
            case CallCheckedStatic -> {
                nameId = GetGlobalId(constants, s.readLong());
                typeArgsId = s.readLong();
                regIndex = s.readInt();
                NomStaticMethodConstant staticMethod = NomContext.constants.GetStaticMethod(nameId);
                NomExpressionNode[] methArgs = new NomExpressionNode[args.size()];
                for (int i = 0; i < args.size(); i++) {
                    methArgs[i] = args.get(i);
                }
                NomExpressionNode ret = WriteToFrame(
                        curMethodArgCount, regIndex,
                        new NomInvokeNode(staticMethod, NomContext::getMethod, methArgs));
                args.clear();
                return ret;
            }
            case BinOp -> {
                BinOperator op = BinOperator.fromValue(s.readByte());
                int leftRegIndex = s.readInt();
                int rightRegIndex = s.readInt();
                regIndex = s.readInt();
                NomExpressionNode left = ReadFromFrame(curMethodArgCount, leftRegIndex);
                NomExpressionNode right = ReadFromFrame(curMethodArgCount, rightRegIndex);
                return switch (op) {
                    case Add -> WriteToFrame(curMethodArgCount, regIndex, NomAddNodeGen.create(left, right));
                    case Subtract -> WriteToFrame(curMethodArgCount, regIndex, NomSubNodeGen.create(left, right));
                    case Multiply -> WriteToFrame(curMethodArgCount, regIndex, NomMulNodeGen.create(left, right));
                    case Divide -> WriteToFrame(curMethodArgCount, regIndex, NomDivNodeGen.create(left, right));
                    case Mod -> WriteToFrame(curMethodArgCount, regIndex, NomModNodeGen.create(left, right));
                    case And -> WriteToFrame(curMethodArgCount, regIndex, NomAndNodeGen.create(left, right));
                    case Or -> WriteToFrame(curMethodArgCount, regIndex, NomOrNodeGen.create(left, right));
                    case GreaterThan ->
                            WriteToFrame(curMethodArgCount, regIndex, NomGreaterThanNodeGen.create(left, right));
                    case GreaterOrEqualTo ->
                            WriteToFrame(curMethodArgCount, regIndex, NomGreaterOrEqualNodeGen.create(left, right));
                    case LessThan -> WriteToFrame(curMethodArgCount, regIndex, NomLessThanNodeGen.create(left, right));
                    case LessOrEqualTo ->
                            WriteToFrame(curMethodArgCount, regIndex, NomLessOrEqualNodeGen.create(left, right));
                    case null, default -> throw new IllegalStateException("Unexpected value: " + op);
                };
            }
            case UnaryOp -> {
                UnaryOperator op = UnaryOperator.fromValue(s.readByte());
                receiverRegIndex = s.readInt();
                regIndex = s.readInt();
                return switch (op) {
                    case Negate -> WriteToFrame(curMethodArgCount, regIndex,
                            NomNegateNodeGen.create(ReadFromFrame(curMethodArgCount, receiverRegIndex)));
                    case Not -> WriteToFrame(curMethodArgCount, regIndex,
                            NomNotNodeGen.create(ReadFromFrame(curMethodArgCount, receiverRegIndex)));
                    case null -> throw new IllegalStateException("Unexpected value: " + null);
                };
            }
            case PhiNode -> {
                int incoming = s.readInt();// how many branches jumps here
                int regCount = s.readInt();//mapped registers types
                while (regCount > 0) {
                    int reg = s.readInt();
                    long typeId = GetGlobalId(constants, s.readLong());
                    regCount--;
                }
                endOfBlock = false;
            }
            case Branch -> {
                int target = s.readInt();
                int incoming = s.readInt();
                List<NomExpressionNode> instructions = new ArrayList<>();
                while (incoming > 0) {
                    int to = s.readInt();
                    int from = s.readInt();
                    instructions
                            .add(WriteToFrame(curMethodArgCount, to,
                                    ReadFromFrame(curMethodArgCount, from)));
                    incoming--;
                }
                //which block to jump to (target + 1 since the beginning of the body is a block)
                int blockIndex = target + 1;

                NomBlockNode ret = GetBlock(blockIndex);
                ret.append(instructions.toArray(new NomStatementNode[0]));
                endOfBlock = true;
                return ret;
            }
            case CondBranch -> {
                int condRegIndex = s.readInt();
                int thenTarget = s.readInt();
                int elseTarget = s.readInt();
                int thenCount = s.readInt();
                int elseCount = s.readInt();
                List<NomExpressionNode> thenInstructions = new ArrayList<>();
                List<NomExpressionNode> elseInstructions = new ArrayList<>();

                while (thenCount > 0) {
                    int to = s.readInt();
                    int from = s.readInt();
                    thenInstructions
                            .add(WriteToFrame(curMethodArgCount, to,
                                    ReadFromFrame(curMethodArgCount, from)));
                    thenCount--;
                }

                while (elseCount > 0) {
                    int to = s.readInt();
                    int from = s.readInt();
                    elseInstructions
                            .add(WriteToFrame(curMethodArgCount, to,
                                    ReadFromFrame(curMethodArgCount, from)));
                    elseCount--;
                }

                int thenBlockIndex = thenTarget + 1;
                int elseBlockIndex = elseTarget + 1;

                NomBlockNode thenBlock = GetBlock(thenBlockIndex);
                NomBlockNode elseBlock = GetBlock(elseBlockIndex);

                thenBlock.append(thenInstructions.toArray(new NomStatementNode[0]));
                elseBlock.append(elseInstructions.toArray(new NomStatementNode[0]));

                endOfBlock = true;
                return new NomIfNode(
                        ReadFromFrame(curMethodArgCount, condRegIndex),
                        thenBlock, elseBlock);
            }
            case null, default -> throw new IllegalStateException("Unexpected value: " + opCode);
        }
        return null;
    }

    private static NomExpressionNode ReadFromFrame(int methodArgCnt, int index) {
        if (index < methodArgCnt) {
            return new NomReadArgumentNode(index);
        }
        index -= methodArgCnt;

        return NomReadRegisterNodeGen.create(index);
    }

    private static NomExpressionNode WriteToFrame(int methodArgCnt, int index, NomExpressionNode value) {
        index -= methodArgCnt;
        return NomWriteRegisterNodeGen.create(value, index);
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
        long typeArgs = GetGlobalId(constants, s.readLong());//type that contains the method
        long returnType = GetGlobalId(constants, s.readLong());
        long argTypes = GetGlobalId(constants, s.readLong());//type of the argument
        String nameStr = NomContext.constants.GetString(nameId).GetText().toString();
        int regCount = s.readInt();
        String qNameStr = cls.GetName().toString() + "." + nameStr;
        NomStaticMethod meth = cls.AddStaticMethod(nameStr, qNameStr, typeArgs, returnType, argTypes, regCount);
        long instructionCount = s.readLong();
        NomTypeListConstant argsTypeList = NomContext.constants.GetTypeList(argTypes);
        int argCount = 0;
        if (argsTypeList != null) {
            argCount = argsTypeList.Count();
        }
        regCount -= argCount;
        System.out.println("ReadStaticMethod " + qNameStr + " with " + instructionCount +
                " instructions that require " + regCount + " registers, typeArgs " + typeArgs + ", returnType " + returnType + ", argTypes " + argTypes);
        List<NomStatementNode> instructions = new ArrayList<>();
        int curBlockIndex = 0;
        blocks.clear();
        args.clear();
        blocks.add(new NomBlockNode(new NomStatementNode[0]));
        endOfBlock = false;
//        int idx = 0;
        while (instructionCount > 0) {
//            System.out.print(idx++);
//            System.out.print(' ');
            NomStatementNode instr = ReadInstruction(meth, s, constants);
            instructionCount--;
            if (instr == null) {
                continue;
            }
            instructions.add(instr);
            if (endOfBlock) {
                blocks.get(curBlockIndex).append(instructions.toArray(new NomStatementNode[0]));
                instructions.clear();
                curBlockIndex++;
            }
        }
        NomBlockNode block = blocks.getFirst();
        NomFunctionBodyNode body = new NomFunctionBodyNode(block);
        FrameDescriptor.Builder builder = FrameDescriptor.newBuilder();
        for (int i = 0; i < regCount; i++) {
            builder.addSlot(FrameSlotKind.Illegal, "reg" + i, null);
        }

        TruffleString methName = NomString.create(qNameStr);
        NomRootNode root = new NomRootNode(language, builder.build(), body, methName, argCount);

        System.out.println("Parsed Interpreter Nodes: (size=" + block.bodyNodeCount() + ")");
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
