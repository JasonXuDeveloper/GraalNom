package com.nom.graalnom.parser;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.google.common.io.LittleEndianDataInputStream;
import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.*;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.controlflow.*;
import com.nom.graalnom.runtime.nodes.expression.NomCastNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.NomInvokeNode;
import com.nom.graalnom.runtime.nodes.expression.binary.*;
import com.nom.graalnom.runtime.nodes.expression.literal.*;
import com.nom.graalnom.runtime.nodes.expression.object.NomNewObjectNode;
import com.nom.graalnom.runtime.nodes.expression.object.NomReadFieldNodeGen;
import com.nom.graalnom.runtime.nodes.expression.object.NomWriteFieldNodeGen;

import com.nom.graalnom.runtime.nodes.expression.unary.NomNegateNodeGen;
import com.nom.graalnom.runtime.nodes.expression.unary.NomNotNodeGen;
import com.nom.graalnom.runtime.nodes.local.*;
import com.nom.graalnom.runtime.reflections.*;
import org.graalvm.collections.Pair;

@SuppressWarnings("unused")
public class ByteCodeReader {
    public static int BYTECODE_VERSION = 2;

    public static void ReadBytecodeFile(String filename, boolean debug) throws Exception {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("file not found");
        }

        try (FileInputStream fs = new FileInputStream(filename)) {
            LittleEndianDataInputStream s = new LittleEndianDataInputStream(fs);
            int ver = s.readInt();
            if (ver > BYTECODE_VERSION) {
                throw new IllegalArgumentException("file" + filename + " (ver" + ver + ") is too new");
            }
            HashMap<Long, Long> constants = new HashMap<>();//local id -> global id
            constants.put(0L, 0L);//null constant
            while (s.available() > 0) {
//                System.out.println(NomContext.constants.Constants().size());
                int b = s.read();//need to use int to get 0-255, I HATE JAVA
                BytecodeTopElementType nextType = BytecodeTopElementType.fromValue(b);
                if (nextType == BytecodeTopElementType.None) return;
                long localConstId = -1;
                if (debug)
                    System.out.println("Read " + nextType);
                switch (nextType) {
                    case StringConstant -> {
                        localConstId = s.readLong();
                        long length = s.readLong();
                        char[] chars = new char[(int) length];
                        for (int i = 0; i < length; i++) {
                            chars[i] = s.readChar();
                        }
                        String str = new String(chars);
                        constants.put(localConstId, NomContext.constants.AddString(str, GetGlobalId(constants, localConstId)));
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
                    case InterfaceConstant -> {
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddInterface(
                                GetGlobalId(constants, s.readLong()),
                                GetGlobalId(constants, s.readLong()),
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
                    case CTBottom -> {
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddBottomType(
                                TryGetGlobalId(constants, localConstId)));
                    }
                    case CTTypeParameters -> {
                        localConstId = s.readLong();
                        long paramCount = s.readLong();
                        NomTypeParameterConstant[] params = new NomTypeParameterConstant[(int) paramCount];
                        for (int i = 0; i < paramCount; i++) {
                            long lowerBound = GetGlobalId(constants, s.readLong());
                            long upperBound = GetGlobalId(constants, s.readLong());
                            params[i] = new NomTypeParameterConstant(lowerBound, upperBound);
                        }
                        constants.put(localConstId, NomContext.constants.AddTypeParameters(
                                params,
                                TryGetGlobalId(constants, localConstId)));
                    }
                    case TypeVarConstant -> {
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddTypeVariable(
                                s.readInt(),
                                TryGetGlobalId(constants, localConstId)));
                    }
                    case CTDynamic -> {
                        localConstId = s.readLong();
                        constants.put(localConstId, NomContext.constants.AddDynType(
                                TryGetGlobalId(constants, localConstId)));
                    }
                    case Class -> ReadClass(s, constants, debug);
                    case Interface -> ReadInterface(s, constants, debug);
                    case null, default -> throw new IllegalArgumentException("unknown type (" + b + "): " + nextType);
                }
                if (constants.containsKey(localConstId) && debug) {
                    System.out.println("Local id -> Global id: " + localConstId + " -> " + constants.get(localConstId));
                    NomContext.constants.Get(constants.get(localConstId).intValue()).Print(true);
                    System.out.println();
                }
            }
        }
    }

    private static final List<NomExpressionNode> args = new ArrayList<>();

    public static NomStatementNode ReadInstruction(NomCallable curCallable, LittleEndianDataInputStream s, Map<Long, Long> constants, boolean debug) throws Exception {
        int opCodeVal = s.read();
        OpCode opCode = OpCode.fromValue(opCodeVal);
        long nameId;
        long typeArgsId;//generic type arguments
        int regIndex;//output register
        int receiverRegIndex;//this instance register
        int curMethodArgCount = curCallable.GetArgCount();//how many arguments (register offset)
        if (debug) {
            System.out.println("Read " + opCode);
        }
        switch (opCode) {
            case Noop -> {
                return null;
            }
            case Argument -> args.add(ReadFromFrame(curMethodArgCount, s.readInt()));
            case Return -> {
                return new NomReturnNode(ReadFromFrame(curMethodArgCount, s.readInt()));
            }
            case ReturnVoid -> {
                return new NomReturnNode(null);
            }
            case EnsureCheckedMethod -> {
                nameId = GetGlobalId(constants, s.readLong());
                receiverRegIndex = s.readInt();
            }
            case EnsureDynamicMethod -> {
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
                                GetString(nameId).Value()));
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

                NomFunction function = NomContext.getMethod(method);
                if (function != null) {
                    NomStatementNode ret = WriteToFrame(
                            curMethodArgCount, regIndex,
                            new NomInvokeNode<>(function, method.MethodName(), methArgs));
                    args.clear();
                    return ret;
                }

                NomStatementNode ret = WriteToFrame(
                        curMethodArgCount, regIndex,
                        new NomInvokeNode<>(true, null, method, NomMethodConstant::QualifiedMethodName,
                                method.MethodName(), NomContext::getMethod, methArgs));
                args.clear();
                return ret;
            }
            case CallDispatchBest -> {
                regIndex = s.readInt();
                receiverRegIndex = s.readInt();
                nameId = GetGlobalId(constants, s.readLong());
                typeArgsId = GetGlobalId(constants, s.readLong());
                String methName = NomContext.constants.GetString(nameId).Value();
                NomExpressionNode[] methArgs = new NomExpressionNode[args.size() + 1];
                methArgs[0] = ReadFromFrame(curMethodArgCount, receiverRegIndex);
                for (int i = 0; i < args.size(); i++) {
                    methArgs[i + 1] = args.get(i);
                }

                NomStatementNode ret = WriteToFrame(
                        curMethodArgCount, regIndex,
                        new NomInvokeNode<>(true, methName + "_dyn", null, (a) -> methName + "_dyn",
                                methName, (a) -> NomContext.builtinFunctions.get(methName), methArgs));
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

                NomFunction function = NomContext.getMethod(staticMethod);
                if (function != null) {
                    NomStatementNode ret = WriteToFrame(
                            curMethodArgCount, regIndex,
                            new NomInvokeNode<>(function, staticMethod.MethodName(), methArgs));
                    args.clear();
                    return ret;
                }

                NomStatementNode ret = WriteToFrame(
                        curMethodArgCount, regIndex,
                        new NomInvokeNode<>(false, null, staticMethod, NomStaticMethodConstant::QualifiedMethodName,
                                staticMethod.MethodName(), NomContext::getMethod, methArgs));
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
                    case Equals, RefEquals ->
                            WriteToFrame(curMethodArgCount, regIndex, NomRefEqualsNodeGen.create(left, right));
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
            case CallConstructor -> {
                regIndex = s.readInt();
                nameId = GetGlobalId(constants, s.readLong());
                typeArgsId = GetGlobalId(constants, s.readLong());
                NomSuperClassConstant superClass = NomContext.constants.GetSuperClass(nameId);
                NomExpressionNode[] methArgs = new NomExpressionNode[args.size() + 1];
                methArgs[0] = new NomNewObjectNode(superClass);
                for (int i = 0; i < args.size(); i++) {
                    methArgs[i + 1] = args.get(i);
                }
                args.clear();

                var ret = NomLanguage.callCtorNode(superClass, curMethodArgCount, regIndex, methArgs.length, methArgs);
                if (ret == null) {
                    //TODO probably shouldnt do it this way
                    return WriteToFrame(curMethodArgCount, regIndex, methArgs[0]);
                }
                return ret;
            }
            case Cast -> {
                regIndex = s.readInt();
                int value = s.readInt();
                long typeId = GetGlobalId(constants, s.readLong());
                //assuming compiler DOES check on the types so that the cast
                //object is always an instance under the type with typeId
                return WriteToFrame(curMethodArgCount, regIndex, new NomCastNode(value, (int) typeId));
            }
            case WriteField -> {
                receiverRegIndex = s.readInt();//this
                int value = s.readInt();//arg
                long fieldName = GetGlobalId(constants, s.readLong());//stringconstant
                long receiverType = GetGlobalId(constants, s.readLong());//classconstant
                return NomWriteFieldNodeGen.create(
                        ReadFromFrame(curMethodArgCount, receiverRegIndex),
                        new NomStringLiteralNode(NomContext.constants.GetString(fieldName).Value()),
                        ReadFromFrame(curMethodArgCount, value));
            }
            case ReadField -> {
                regIndex = s.readInt();
                receiverRegIndex = s.readInt();
                long fieldName = GetGlobalId(constants, s.readLong());
                long receiverType = GetGlobalId(constants, s.readLong());
                return WriteToFrame(curMethodArgCount, regIndex,
                        NomReadFieldNodeGen.create(ReadFromFrame(curMethodArgCount, receiverRegIndex),
                                new NomStringLiteralNode(NomContext.constants.GetString(fieldName).Value())));
            }
            case PhiNode -> {
                int incoming = s.readInt();// how many branches jumps here
                int regCount = s.readInt();//mapped registers types
                while (regCount > 0) {
                    int reg = s.readInt();
                    long typeId = GetGlobalId(constants, s.readLong());
                    regCount--;
                }
            }
            case Branch -> {
                int target = s.readInt();
                int incoming = s.readInt();
                List<NomStatementNode> instructions = new ArrayList<>();
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

                return new NomBranchNode(instructions.toArray(new NomStatementNode[0]), blockIndex);
            }
            case CondBranch -> {
                int condRegIndex = s.readInt();
                int thenTarget = s.readInt();
                int elseTarget = s.readInt();
                int thenCount = s.readInt();
                int elseCount = s.readInt();
                List<NomStatementNode> thenInstructions = new ArrayList<>();
                List<NomStatementNode> elseInstructions = new ArrayList<>();

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

                return new NomIfNode(
                        ReadFromFrame(curMethodArgCount, condRegIndex),
                        new NomBranchNode(thenInstructions.toArray(new NomStatementNode[0]), thenBlockIndex),
                        new NomBranchNode(elseInstructions.toArray(new NomStatementNode[0]), elseBlockIndex));
            }
            case null, default -> throw new IllegalStateException("Unexpected value: " + opCode);
        }
        return null;
    }

    private static NomExpressionNode ReadFromFrame(int methodArgCnt, int index) {
        return NomReadRegisterNodeGen.create(index);
    }

    public static NomStatementNode WriteToFrame(int methodArgCnt, int index, NomExpressionNode value) {
        return new NomWriteRegisterNode(index, value);
    }

    public static void ReadInterface(LittleEndianDataInputStream s, Map<Long, Long> constants, boolean debug) throws Exception {
        long nameId = GetGlobalId(constants, s.readLong());
        long typeParams = GetGlobalId(constants, s.readLong());
        byte visibility = s.readByte();
        byte flags = s.readByte();
        long superInterfacesId = GetGlobalId(constants, s.readLong());
        NomInterface cls = new NomInterface(nameId, typeParams, superInterfacesId);
        NomClass.RegisterClass(NomContext.constants.GetString(nameId).Value(), cls);
        long methodCount = s.readLong();
        while (methodCount > 0) {
            ReadMethod(s, cls, constants, debug);
            methodCount--;
        }
    }

    public static void ReadClass(LittleEndianDataInputStream s, Map<Long, Long> constants, boolean debug) throws Exception {
        long nameId = GetGlobalId(constants, s.readLong());
        long typeArgsId = GetGlobalId(constants, s.readLong());
        byte visibility = s.readByte();
        byte flags = s.readByte();
        long superInterfacesId = GetGlobalId(constants, s.readLong());
        long superClassId = GetGlobalId(constants, s.readLong());
        NomClass cls = new NomClass(nameId, typeArgsId, superClassId, superInterfacesId);
        NomClass.RegisterClass(NomContext.constants.GetString(nameId).Value(), cls);
        long methodCount = s.readLong();
        while (methodCount > 0) {
            ReadMethod(s, cls, constants, debug);
            methodCount--;
        }
        long fieldCount = s.readLong();
        while (fieldCount > 0) {
            ReadField(s, cls, constants);
            fieldCount--;
        }
        long staticMethodCount = s.readLong();
        while (staticMethodCount > 0) {
            ReadStaticMethod(s, cls, constants, debug);
            staticMethodCount--;
        }
        long constructorCount = s.readLong();
        while (constructorCount > 0) {
            ReadConstructor(s, cls, constants, debug);
            constructorCount--;
        }
        long lambdaCount = s.readLong();
        while (lambdaCount > 0) {
            //ReadLambda(cls);
            lambdaCount--;
        }
        long structCount = s.readLong();
        while (structCount > 0) {
            //ReadStruct(cls);
            structCount--;
        }
    }

    public static void ReadMethod(LittleEndianDataInputStream s, NomInterface cls, Map<Long, Long> constants, boolean debug) throws Exception {
        if (s.read() != BytecodeInternalElementType.Method.getValue()) {
            throw new IllegalArgumentException("Expected method, but did not encounter method marker");
        }
        long nameId = GetGlobalId(constants, s.readLong());
        long typeArgs = GetGlobalId(constants, s.readLong());//type that contains the method
        long returnType = GetGlobalId(constants, s.readLong());
        long argTypes = GetGlobalId(constants, s.readLong());//type of the argument
        String nameStr = NomContext.constants.GetString(nameId).Value();
        boolean isFinal = s.readBoolean();
        int regCount = s.readInt();
        String qNameStr = cls.GetName() + "." + nameStr;
        NomMethod meth = cls.AddMethod(nameStr, qNameStr, typeArgs, returnType, argTypes, regCount, isFinal);
        long instructionCount = s.readLong();
        args.clear();
        while (instructionCount > 0) {
            NomStatementNode instr = ReadInstruction(meth, s, constants, debug);
            instructionCount--;
            meth.AddInstruction(instr);
        }
    }

    public static void ReadStaticMethod(LittleEndianDataInputStream s, NomClass cls, Map<Long, Long> constants, boolean debug) throws Exception {
        if (s.read() != BytecodeInternalElementType.StaticMethod.getValue()) {
            throw new IllegalArgumentException("Expected static method, but did not encounter static method marker");
        }
        long nameId = GetGlobalId(constants, s.readLong());
        long typeArgs = GetGlobalId(constants, s.readLong());//type that contains the method
        long returnType = GetGlobalId(constants, s.readLong());
        long argTypes = GetGlobalId(constants, s.readLong());//type of the argument
        String nameStr = NomContext.constants.GetString(nameId).Value();
        int regCount = s.readInt();
        String qNameStr = cls.GetName() + "." + nameStr;
        NomStaticMethod meth = cls.AddStaticMethod(nameStr, qNameStr, typeArgs, returnType, argTypes, regCount);
        long instructionCount = s.readLong();
        args.clear();
        while (instructionCount > 0) {
            NomStatementNode instr = ReadInstruction(meth, s, constants, debug);
            instructionCount--;
            meth.AddInstruction(instr);
        }
    }

    public static void ReadConstructor(LittleEndianDataInputStream s, NomClass cls, Map<Long, Long> constants, boolean debug) throws Exception {
        if (s.read() != BytecodeInternalElementType.Constructor.getValue()) {
            throw new IllegalArgumentException("Expected constructor, but did not encounter constructor marker");
        }
        long argTypes = GetGlobalId(constants, s.readLong());//type of the argument
        int regCount = s.readInt();
        NomConstructor ctor = cls.AddConstructor(argTypes, regCount);
        long preInstructionCount = s.readLong();
        long superArgCount = s.readLong();
        long instructionCount = s.readLong();
        args.clear();
        while (preInstructionCount > 0) {
            NomStatementNode instr = ReadInstruction(ctor, s, constants, debug);
            preInstructionCount--;
            ctor.AddInstruction(instr);
        }
        args.clear();
        if (superArgCount > 0) {
            args.add(NomReadRegisterNodeGen.create(0));
            while (superArgCount > 0) {
                args.add(NomReadRegisterNodeGen.create(s.readInt()));
                superArgCount--;
            }
            if (!args.isEmpty()) {
                ctor.AddInstruction(
                        NomLanguage.callCtorNode(
                                NomContext.constants.GetSuperClass(ctor.declaringClass.SuperClass),
                                ctor.GetArgCount(),
                                ctor.GetArgCount(),
                                args.size(),
                                args.toArray(new NomExpressionNode[0])));
                args.clear();
            }
        }

        while (instructionCount > 0) {
            NomStatementNode instr = ReadInstruction(ctor, s, constants, debug);
            instructionCount--;
            ctor.AddInstruction(instr);
        }
        ctor.AddInstruction(new NomReturnNode(NomReadRegisterNodeGen.create(0)));
    }

    public static void ReadField(LittleEndianDataInputStream s, NomClass cls, Map<Long, Long> constants) throws Exception {
        if (s.read() != BytecodeInternalElementType.Field.getValue()) {
            throw new IllegalArgumentException("Expected field, but did not encounter field marker");
        }
        long name = GetGlobalId(constants, s.readLong());
        long type = GetGlobalId(constants, s.readLong());
        byte visibility = s.readByte();
        byte flags = s.readByte();
        cls.AddField(name, type, Visibility.fromValue(visibility), (flags & 1) == 1, (flags & 2) == 2);
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
