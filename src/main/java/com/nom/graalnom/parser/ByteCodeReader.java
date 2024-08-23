package com.nom.graalnom.parser;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.google.common.io.LittleEndianDataInputStream;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.*;
import com.nom.graalnom.runtime.opcodes.TransformedOpCode;
import com.nom.graalnom.runtime.opcodes.control.*;
import com.nom.graalnom.runtime.opcodes.load.*;
import com.nom.graalnom.runtime.opcodes.common.*;
import com.nom.graalnom.runtime.opcodes.binOp.*;
import com.nom.graalnom.runtime.opcodes.unOp.*;
import com.nom.graalnom.runtime.opcodes.method.*;
import com.nom.graalnom.runtime.reflections.*;
import com.oracle.truffle.api.strings.TruffleString;
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

    private static final List<Integer> args = new ArrayList<>();
    private static final List<TransformedOpCode> opCodes = new ArrayList<>();

    public static TransformedOpCode ReadInstruction(NomCallable curCallable, LittleEndianDataInputStream s, Map<Long, Long> constants, boolean debug) throws Exception {
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
            case Argument -> args.add(s.readInt());
            case Return -> {
                return new ReturnOpCode(s.readInt());
            }
            case ReturnVoid -> {
                return new ReturnVoidOpCode();
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
                return new LoadIntConstOpCode(regIndex, longVal);
            }
            case LoadFloatConstant -> {
                double floatVal = s.readDouble();
                regIndex = s.readInt();
                return new LoadFloatConstOpCode(regIndex, floatVal);
            }
            case LoadBoolConstant -> {
                boolean boolVal = s.readBoolean();
                regIndex = s.readInt();
                return new LoadBoolConstOpCode(regIndex, boolVal);
            }
            case LoadNullConstant -> {
                regIndex = s.readInt();
                return new LoadNullConstOpCode(regIndex);
            }
            case LoadStringConstant -> {
                nameId = GetGlobalId(constants, s.readLong());
                regIndex = s.readInt();
                TruffleString str = NomContext.constants.GetString(nameId).Value();
                return new LoadStringConstOpCode(regIndex, str);
            }
            case InvokeCheckedInstance -> {
                nameId = GetGlobalId(constants, s.readLong());
                typeArgsId = GetGlobalId(constants, s.readLong());
                regIndex = s.readInt();
                receiverRegIndex = s.readInt();
                int[] argRegs = new int[args.size()];
                for (int i = 0; i < args.size(); i++) {
                    argRegs[i] = args.get(i);
                }
                args.clear();
                return new InvokeInstanceMethodOpCode(
                        regIndex,
                        nameId,
                        argRegs,
                        receiverRegIndex);
            }
            case CallDispatchBest -> {
                regIndex = s.readInt();
                receiverRegIndex = s.readInt();
                nameId = GetGlobalId(constants, s.readLong());
                typeArgsId = GetGlobalId(constants, s.readLong());
                int[] argRegs = new int[args.size()];
                for (int i = 0; i < args.size(); i++) {
                    argRegs[i] = args.get(i);
                }
                args.clear();
                return new InvokeDispatchMethodOpCode(
                        regIndex,
                        nameId,
                        argRegs,
                        receiverRegIndex);
            }
            case CallCheckedStatic -> {
                nameId = GetGlobalId(constants, s.readLong());
                typeArgsId = s.readLong();
                regIndex = s.readInt();
                int[] argRegs = new int[args.size()];
                for (int i = 0; i < args.size(); i++) {
                    argRegs[i] = args.get(i);
                }
                args.clear();
                return new InvokeStaticMethodOpCode(
                        regIndex,
                        nameId,
                        argRegs);
            }
            case BinOp -> {
                BinOperator op = BinOperator.fromValue(s.readByte());
                int leftRegIndex = s.readInt();
                int rightRegIndex = s.readInt();
                regIndex = s.readInt();
                return switch (op) {
                    case Add -> new AddOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case Subtract -> new SubOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case Multiply -> new MulOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case Divide -> new DivOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case Mod -> new ModOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case And -> new AndOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case Or -> new OrOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case GreaterThan -> new GTOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case GreaterOrEqualTo -> new GTEOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case LessThan -> new LTOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case LessOrEqualTo -> new LTEOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case Equals -> new EQOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case RefEquals -> new RefEqOpCode(leftRegIndex, rightRegIndex, regIndex);
                    case null, default -> throw new IllegalStateException("Unexpected value: " + op);
                };
            }
            case UnaryOp -> {
                UnaryOperator op = UnaryOperator.fromValue(s.readByte());
                receiverRegIndex = s.readInt();
                regIndex = s.readInt();
                return switch (op) {
                    case Negate -> new NegateOpCode(receiverRegIndex, regIndex);
                    case Not -> new NotOpCode(receiverRegIndex, regIndex);
                    case null -> throw new IllegalStateException("Unexpected value: " + null);
                };
            }
            case CallConstructor -> {
                regIndex = s.readInt();
                nameId = GetGlobalId(constants, s.readLong());
                typeArgsId = GetGlobalId(constants, s.readLong());
                NomSuperClassConstant superClass = NomContext.constants.GetSuperClass(nameId);
                int[] argRegs = new int[args.size()];
                for (int i = 0; i < args.size(); i++) {
                    argRegs[i] = args.get(i);
                }
                args.clear();

                return new InvokeCtorOpCode(
                        regIndex,
                        nameId,
                        argRegs,
                        false);
            }
            case Cast -> {
                regIndex = s.readInt();
                int value = s.readInt();
                long typeId = GetGlobalId(constants, s.readLong());
                //assuming compiler DOES check on the types so that the cast
                //object is always an instance under the type with typeId
                return new CastOpCode(value, regIndex, typeId);
            }
            case WriteField -> {
                receiverRegIndex = s.readInt();//this
                int value = s.readInt();//arg
                long fieldName = GetGlobalId(constants, s.readLong());//stringconstant
                long receiverType = GetGlobalId(constants, s.readLong());//classconstant
                return new WriteFieldOpCode(receiverRegIndex, value, fieldName);
            }
            case ReadField -> {
                regIndex = s.readInt();
                receiverRegIndex = s.readInt();
                long fieldName = GetGlobalId(constants, s.readLong());
                long receiverType = GetGlobalId(constants, s.readLong());
                return new ReadFieldOpCode(receiverRegIndex, regIndex, fieldName);
            }
            case PhiNode -> {
                int incoming = s.readInt();// how many branches jumps here
                int regCount = s.readInt();//mapped registers types
                while (regCount > 0) {
                    int reg = s.readInt();
                    long typeId = GetGlobalId(constants, s.readLong());
                    regCount--;
                }

                return new PhiOpCode();
            }
            case Branch -> {
                int target = s.readInt();
                int incoming = s.readInt();
                int[] incomings = new int[incoming];
                int[] outgoings = new int[incoming];
                int idx = 0;
                while (incoming > 0) {
                    int to = s.readInt();
                    int from = s.readInt();
                    incomings[idx] = from;
                    outgoings[idx] = to;
                    idx++;
                    incoming--;
                }

                return new BranchOpCode(target, incomings, outgoings);
            }
            case CondBranch -> {
                int condRegIndex = s.readInt();
                int thenTarget = s.readInt();
                int elseTarget = s.readInt();
                int thenCount = s.readInt();
                int elseCount = s.readInt();

                int[] thenIncomings = new int[thenCount];
                int[] thenOutgoings = new int[thenCount];
                int[] elseIncomings = new int[elseCount];
                int[] elseOutgoings = new int[elseCount];

                int idx = 0;

                while (thenCount > 0) {
                    int to = s.readInt();
                    int from = s.readInt();
                    thenIncomings[idx] = from;
                    thenOutgoings[idx] = to;
                    idx++;
                    thenCount--;
                }

                idx = 0;

                while (elseCount > 0) {
                    int to = s.readInt();
                    int from = s.readInt();
                    elseIncomings[idx] = from;
                    elseOutgoings[idx] = to;
                    idx++;
                    elseCount--;
                }

                return new CondBranchOpCode(condRegIndex,
                        thenTarget, elseTarget,
                        thenIncomings, elseIncomings, thenOutgoings, elseOutgoings);
            }
            case null, default -> throw new IllegalStateException("Unexpected value: " + opCode);
        }
        return null;
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
        TruffleString nameStr = NomContext.constants.GetString(nameId).Value();
        boolean isFinal = s.readBoolean();
        int regCount = s.readInt();
        TruffleString qNameStr = TruffleString.fromJavaStringUncached(cls.GetName().toString() + "." + nameStr, TruffleString.Encoding.UTF_8);
        NomMethod meth = cls.AddMethod(nameStr, qNameStr, typeArgs, returnType, argTypes, regCount, isFinal);
        long instructionCount = s.readLong();
        args.clear();
        opCodes.clear();
        while (instructionCount > 0) {
            TransformedOpCode code = ReadInstruction(meth, s, constants, debug);
            instructionCount--;
            if(code != null)
                opCodes.add(code);
        }
        meth.AddBytecode(opCodes.toArray(new TransformedOpCode[0]));
    }

    public static void ReadStaticMethod(LittleEndianDataInputStream s, NomClass cls, Map<Long, Long> constants, boolean debug) throws Exception {
        if (s.read() != BytecodeInternalElementType.StaticMethod.getValue()) {
            throw new IllegalArgumentException("Expected static method, but did not encounter static method marker");
        }
        long nameId = GetGlobalId(constants, s.readLong());
        long typeArgs = GetGlobalId(constants, s.readLong());//type that contains the method
        long returnType = GetGlobalId(constants, s.readLong());
        long argTypes = GetGlobalId(constants, s.readLong());//type of the argument
        TruffleString nameStr = NomContext.constants.GetString(nameId).Value();
        int regCount = s.readInt();
        TruffleString qNameStr = TruffleString.fromJavaStringUncached(cls.GetName().toString() + "." + nameStr, TruffleString.Encoding.UTF_8);
        NomStaticMethod meth = cls.AddStaticMethod(nameStr, qNameStr, typeArgs, returnType, argTypes, regCount);
        long instructionCount = s.readLong();
        args.clear();
        opCodes.clear();
        while (instructionCount > 0) {
            TransformedOpCode code = ReadInstruction(meth, s, constants, debug);
            instructionCount--;
            if(code != null)
                opCodes.add(code);
        }
        meth.AddBytecode(opCodes.toArray(new TransformedOpCode[0]));
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
        opCodes.clear();
        while (preInstructionCount > 0) {
            TransformedOpCode code = ReadInstruction(ctor, s, constants, debug);
            preInstructionCount--;
            if(code != null)
                opCodes.add(code);
        }
        ctor.AddBytecode(opCodes.toArray(new TransformedOpCode[0]));

        args.clear();
        opCodes.clear();
        if (superArgCount > 0) {
            while (superArgCount > 0) {
                args.add(s.readInt());
                superArgCount--;
            }
            if (!args.isEmpty()) {
                ctor.AddBytecode(new TransformedOpCode[]{
                        new InvokeCtorOpCode(ctor.GetArgCount(),
                                ctor.declaringClass.SuperClass,
                                args.stream().mapToInt(i -> i).toArray(), true)
                });
                args.clear();
            }
        }

        if (instructionCount > 0) {
            args.clear();
            opCodes.clear();
            while (instructionCount > 0) {
                TransformedOpCode code = ReadInstruction(ctor, s, constants, debug);
                instructionCount--;
                if(code != null)
                    opCodes.add(code);
            }
            ctor.AddBytecode(opCodes.toArray(new TransformedOpCode[0]));
        }
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
