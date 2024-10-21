package com.nom.graalnom.runtime.nodes;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.*;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.datatypes.NomNull;
import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.opcodes.*;
import com.nom.graalnom.runtime.opcodes.binOp.*;
import com.nom.graalnom.runtime.opcodes.common.*;
import com.nom.graalnom.runtime.opcodes.control.*;
import com.nom.graalnom.runtime.opcodes.load.*;
import com.nom.graalnom.runtime.opcodes.method.*;
import com.nom.graalnom.runtime.opcodes.unOp.*;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.nom.graalnom.runtime.reflections.NomInterface;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.HostCompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.nodes.BytecodeOSRNode;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.strings.TruffleString;


public class BytecodeDispatchNode extends Node {
    @CompilerDirectives.CompilationFinal
    TransformedOpCode[] bytecode;
    @CompilerDirectives.CompilationFinal
    private int argCount;


    public BytecodeDispatchNode(TransformedOpCode[] bytecode, int argCount) {
        this.bytecode = bytecode;
        this.argCount = argCount;
    }

    private Object getFromFrame(VirtualFrame frame, int regIndex) {
        return frame.getValue(regIndex);
    }

    private void writeLongToFrame(VirtualFrame frame, int regIndex, long value) {
        frame.setLong(regIndex, value);
    }

    private void writeDoubleToFrame(VirtualFrame frame, int regIndex, double value) {
        frame.setDouble(regIndex, value);
    }

    private void writeBooleanToFrame(VirtualFrame frame, int regIndex, boolean value) {
        frame.setBoolean(regIndex, value);
    }

    private void writeNullToFrame(VirtualFrame frame, int regIndex) {
        frame.setObject(regIndex, NomNull.SINGLETON);
    }

    private void writeStringToFrame(VirtualFrame frame, int regIndex, TruffleString value) {
        frame.setObject(regIndex, value);
    }

    private void writeToFrame(VirtualFrame frame, int regIndex, Object value) {
        switch (value) {
            case Long l -> writeLongToFrame(frame, regIndex, l);
            case Double d -> writeDoubleToFrame(frame, regIndex, d);
            case Boolean b -> writeBooleanToFrame(frame, regIndex, b);
            case NomNull ignored -> writeNullToFrame(frame, regIndex);
            case TruffleString s -> writeStringToFrame(frame, regIndex, s);
            default -> frame.setObject(regIndex, value);
        }
    }

    private boolean isTail(int bci) {
        if (bci >= bytecode.length) {
            return false;
        }

        if (bci == bytecode.length - 1) {
            return true;
        }

        int nextBCI = bci + 1;
        switch (bytecode[bci]) {
            case ReturnOpCode ret -> {
                return true;
            }
            case ReturnVoidOpCode voidOpCode -> {
                return true;
            }
            case null -> {
                return false;
            }
            default -> {
                switch (bytecode[nextBCI]) {
                    case ReturnOpCode ret -> {
                        return true;
                    }
                    case ReturnVoidOpCode returnVoidOpCode -> {
                        return true;
                    }
                    case null, default -> {
                        return false;
                    }
                }
            }
        }
    }

    @HostCompilerDirectives.BytecodeInterpreterSwitch
    public Object execute(VirtualFrame frame) {
        int bci = 0;
        //copy arguments to frame
        if (argCount == frame.getArguments().length) {
            for (int i = 0; i < argCount; i++) {
                frame.setObject(i, frame.getArguments()[i]);
            }
        }
        while (bci < bytecode.length) {
            int nextBCI = bci + 1;
            if (bytecode[bci] instanceof ReturnOpCode ret) {
                return getFromFrame(frame, ret.getRegIndex());
            } else if (bytecode[bci] instanceof ReturnVoidOpCode) {
                return 0;
            } else if (bytecode[bci] instanceof LoadIntConstOpCode loadIntConst) {
                writeLongToFrame(frame, loadIntConst.getRegIndex(), loadIntConst.getValue());
            } else if (bytecode[bci] instanceof LoadFloatConstOpCode loadFloatConst) {
                writeDoubleToFrame(frame, loadFloatConst.getRegIndex(), loadFloatConst.getValue());
            } else if (bytecode[bci] instanceof LoadBoolConstOpCode loadBoolConst) {
                writeBooleanToFrame(frame, loadBoolConst.getRegIndex(), loadBoolConst.getValue());
            } else if (bytecode[bci] instanceof LoadNullConstOpCode loadNullConst) {
                writeNullToFrame(frame, loadNullConst.getRegIndex());
            } else if (bytecode[bci] instanceof LoadStringConstOpCode loadStringConst) {
                writeStringToFrame(frame, loadStringConst.getRegIndex(), loadStringConst.getValue());
            } else if (bytecode[bci] instanceof InvokeInstanceMethodOpCode invokeInstanceMethod) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                Object instance = getFromFrame(frame, invokeInstanceMethod.getReceiverIdx());
                int[] argIdxs = invokeInstanceMethod.getArgIdxs();
                Object[] args = new Object[argIdxs.length + 1];
                args[0] = instance;
                for (int i = 0; i < argIdxs.length; i++) {
                    args[i + 1] = getFromFrame(frame, argIdxs[i]);
                }
                NomFunction function = getInstanceMethod(invokeInstanceMethod, instance, args);
                if (isTail(bci) && function.functionBodyNode != null) {
                    //copy arguments to frame
                    for (int i = 0; i < args.length; i++) {
                        frame.setObject(i, args[i]);
                    }
                    Object methRet = function.functionBodyNode.executeGeneric(frame);
                    writeToFrame(frame, invokeInstanceMethod.getRegIdx(), methRet);
                } else {
                    Object methRet = invokeInstanceMethod(invokeInstanceMethod, instance, args);
                    writeToFrame(frame, invokeInstanceMethod.getRegIdx(), methRet);
                }
            } else if (bytecode[bci] instanceof InvokeStaticMethodOpCode invokeStaticMethod) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                int[] argIdxs = invokeStaticMethod.getArgIdxs();
                Object[] args = new Object[argIdxs.length];
                for (int i = 0; i < argIdxs.length; i++) {
                    args[i] = getFromFrame(frame, argIdxs[i]);
                }
                NomFunction function = getStaticMethod(invokeStaticMethod);
                //TODO some reason this makes frame value wrong, currently assume this is due to graal's cache
                if (isTail(bci) && function.functionBodyNode != null) {
                    //copy arguments to frame
                    for (int i = 0; i < args.length; i++) {
                        frame.setObject(i, args[i]);
                    }
                    Object ret = function.functionBodyNode.executeGeneric(frame);
                    writeToFrame(frame, invokeStaticMethod.getRegIdx(), ret);
                } else
                {
                    Object ret = invokeStaticMethod(invokeStaticMethod, args);
                    writeToFrame(frame, invokeStaticMethod.getRegIdx(), ret);
                }
            } else if (bytecode[bci] instanceof InvokeCtorOpCode invokeCtorOpCode) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                int[] argIdxs = invokeCtorOpCode.getArgIdxs();
                NomSuperClassConstant superClassConstant = NomContext.constants.GetSuperClass(invokeCtorOpCode.getNameId());

                NomObject object;
                TruffleString name = superClassConstant.GetSuperClass().GetName();
                if (name.equals(TruffleString.fromConstant("Timer_0", TruffleString.Encoding.UTF_8))) {//TODO avoid hardcode builtin types
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    object = NomLanguage.createTimer();
                } else {
                    Object[] args = new Object[argIdxs.length + 1];
                    if (!invokeCtorOpCode.getHasInstance()) {
                        CompilerDirectives.transferToInterpreterAndInvalidate();
                        object = NomLanguage.createObject((NomClass) NomContext.classes.get(name));
                    } else {
                        object = (NomObject) getFromFrame(frame, 0);
                    }
                    args[0] = object;
                    for (int i = 0; i < argIdxs.length; i++) {
                        args[i + 1] = getFromFrame(frame, argIdxs[i]);
                    }

                    invokeConstructor(invokeCtorOpCode, args);
                }

                writeToFrame(frame, invokeCtorOpCode.getRegIdx(), object);
            } else if (bytecode[bci] instanceof CastOpCode castOpCode) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                int regIdx = castOpCode.getRegIdx();
                Object value = getFromFrame(frame, castOpCode.getValIdx());
                NomConstant type = NomContext.constants.Get((int) castOpCode.getTypeId());
                if (type instanceof NomClassTypeConstant ct) {
                    TruffleString cName;
                    if (ct.GetClass() == null) {
                        NomInterfaceConstant inter = ct.GetInterface();
                        if (inter == null) {
                            throw CompilerDirectives.shouldNotReachHere("Constant is wrong");
                        }
                        cName = inter.GetName();
                    } else {
                        cName = ct.GetClass().GetName();
                    }
                    switch (cName.toString()) {
                        case "Int_0" -> {
                            if (value instanceof Long) {
                                writeLongToFrame(frame, regIdx, (long) value);
                                break;
                            }

                            throw CompilerDirectives.shouldNotReachHere("Unsupported cast");
                        }
                        case "Float_0" -> {
                            if (value instanceof Double) {
                                writeDoubleToFrame(frame, regIdx, (double) value);
                                break;
                            }

                            throw CompilerDirectives.shouldNotReachHere("Unsupported cast");
                        }
                        case "Bool_0" -> {
                            if (value instanceof Boolean) {
                                writeBooleanToFrame(frame, regIdx, (boolean) value);
                                break;
                            }

                            throw CompilerDirectives.shouldNotReachHere("Unsupported cast");
                        }
                        case "String_0" -> {
                            if (value instanceof TruffleString) {
                                writeStringToFrame(frame, regIdx, (TruffleString) value);
                                break;
                            }

                            throw CompilerDirectives.shouldNotReachHere("Unsupported cast");
                        }
                        case "Null_0" -> {
                            if (value instanceof NomNull) {
                                writeNullToFrame(frame, regIdx);
                                break;
                            }

                            throw CompilerDirectives.shouldNotReachHere("Unsupported cast");
                        }
                        default -> {
                            if (!(value instanceof NomObject nomObj)) {
                                throw CompilerDirectives.shouldNotReachHere("Unsupported cast");
                            }

                            NomClass cls = nomObj.GetClass();
                            NomClassConstant cc = ct.GetClass();
                            TruffleString name;
                            if (cc == null) {
                                NomInterfaceConstant inter = ct.GetInterface();
                                if (inter == null) {
                                    throw CompilerDirectives.shouldNotReachHere("Constant is wrong");
                                }
                                name = inter.GetName();
                            } else {
                                name = cc.GetName();
                            }

                            NomInterface inter = NomContext.classes.get(name);
                            if (inter == null) {
                                throw CompilerDirectives.shouldNotReachHere(name + " not found");
                            }

                            if (inter instanceof NomClass nc) {
                                if (!cls.superClasses.contains(nc)) {
                                    throw CompilerDirectives.shouldNotReachHere(cls.GetName() + " is not a subclass of " + nc.GetName());
                                }

                                writeToFrame(frame, regIdx, value);
                            } else {
                                if (!cls.superInterfaces.contains(inter)) {
                                    throw CompilerDirectives.shouldNotReachHere(cls.GetName() + " is not a subclass of " + inter.GetName());
                                }

                                writeToFrame(frame, regIdx, value);
                            }
                        }
                    }
                }
                if (type instanceof NomDynamicTypeConstant) {
                    writeToFrame(frame, regIdx, value);
                }
            } else if (bytecode[bci] instanceof AddOpCode addOpCode) {
                Object left = getFromFrame(frame, addOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, addOpCode.getRightRegIdx());
                int regIdx = addOpCode.getRegIdx();
                switch (left) {
                    case Long l1 when right instanceof Long l2 -> writeLongToFrame(frame, regIdx, l1 + l2);
                    case Double d1 when right instanceof Double d2 -> writeDoubleToFrame(frame, regIdx, d1 + d2);
                    case Long l when right instanceof Double d -> writeDoubleToFrame(frame, regIdx, l + d);
                    case Double d when right instanceof Long l -> writeDoubleToFrame(frame, regIdx, d + l);
                    case null, default -> throw CompilerDirectives.shouldNotReachHere();
                }
            } else if (bytecode[bci] instanceof SubOpCode subOpCode) {
                Object left = getFromFrame(frame, subOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, subOpCode.getRightRegIdx());
                int regIdx = subOpCode.getRegIdx();
                switch (left) {
                    case Long l1 when right instanceof Long l2 -> writeLongToFrame(frame, regIdx, l1 - l2);
                    case Double d1 when right instanceof Double d2 -> writeDoubleToFrame(frame, regIdx, d1 - d2);
                    case Long l when right instanceof Double d -> writeDoubleToFrame(frame, regIdx, l - d);
                    case Double d when right instanceof Long l -> writeDoubleToFrame(frame, regIdx, d - l);
                    case null, default -> throw CompilerDirectives.shouldNotReachHere();
                }
            } else if (bytecode[bci] instanceof MulOpCode mulOpCode) {
                Object left = getFromFrame(frame, mulOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, mulOpCode.getRightRegIdx());
                int regIdx = mulOpCode.getRegIdx();
                switch (left) {
                    case Long l1 when right instanceof Long l2 -> writeLongToFrame(frame, regIdx, l1 * l2);
                    case Double d1 when right instanceof Double d2 -> writeDoubleToFrame(frame, regIdx, d1 * d2);
                    case Long l when right instanceof Double d -> writeDoubleToFrame(frame, regIdx, l * d);
                    case Double d when right instanceof Long l -> writeDoubleToFrame(frame, regIdx, d * l);
                    case null, default -> throw CompilerDirectives.shouldNotReachHere();
                }
            } else if (bytecode[bci] instanceof DivOpCode divOpCode) {
                Object left = getFromFrame(frame, divOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, divOpCode.getRightRegIdx());
                int regIdx = divOpCode.getRegIdx();
                switch (left) {
                    case Long l1 when right instanceof Long l2 -> writeLongToFrame(frame, regIdx, l1 / l2);
                    case Double d1 when right instanceof Double d2 -> writeDoubleToFrame(frame, regIdx, d1 / d2);
                    case Long l when right instanceof Double d -> writeDoubleToFrame(frame, regIdx, l / d);
                    case Double d when right instanceof Long l -> writeDoubleToFrame(frame, regIdx, d / l);
                    case null, default -> throw CompilerDirectives.shouldNotReachHere();
                }
            } else if (bytecode[bci] instanceof ModOpCode modOpCode) {
                Object left = getFromFrame(frame, modOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, modOpCode.getRightRegIdx());
                int regIdx = modOpCode.getRegIdx();
                switch (left) {
                    case Long l1 when right instanceof Long l2 -> writeLongToFrame(frame, regIdx, l1 % l2);
                    case Double d1 when right instanceof Double d2 -> writeDoubleToFrame(frame, regIdx, d1 % d2);
                    case Long l when right instanceof Double d -> writeDoubleToFrame(frame, regIdx, l % d);
                    case Double d when right instanceof Long l -> writeDoubleToFrame(frame, regIdx, d % l);
                    case null, default -> throw CompilerDirectives.shouldNotReachHere(
                            "method name: " + this.getRootNode().getName() +" bci: " + bci
                            + " left: " + left + " right: " + right
                    );
                }
            } else if (bytecode[bci] instanceof AndOpCode andOpCode) {
                Object left = getFromFrame(frame, andOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, andOpCode.getRightRegIdx());
                int regIdx = andOpCode.getRegIdx();
                if (left instanceof Boolean l1 && right instanceof Boolean l2) {
                    writeBooleanToFrame(frame, regIdx, l1 && l2);
                } else {
                    throw CompilerDirectives.shouldNotReachHere();
                }
            } else if (bytecode[bci] instanceof OrOpCode orOpCode) {
                Object left = getFromFrame(frame, orOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, orOpCode.getRightRegIdx());
                int regIdx = orOpCode.getRegIdx();
                if (left instanceof Boolean l1 && right instanceof Boolean l2) {
                    writeBooleanToFrame(frame, regIdx, l1 || l2);
                } else {
                    throw CompilerDirectives.shouldNotReachHere();
                }
            } else if (bytecode[bci] instanceof GTOpCode gtOpCode) {
                Object left = getFromFrame(frame, gtOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, gtOpCode.getRightRegIdx());
                int regIdx = gtOpCode.getRegIdx();
                switch (left) {
                    case Long l1 when right instanceof Long l2 -> writeBooleanToFrame(frame, regIdx, l1 > l2);
                    case Double d1 when right instanceof Double d2 -> writeBooleanToFrame(frame, regIdx, d1 > d2);
                    case Long l when right instanceof Double d -> writeBooleanToFrame(frame, regIdx, l > d);
                    case Double d when right instanceof Long l -> writeBooleanToFrame(frame, regIdx, d > l);
                    case null, default -> throw CompilerDirectives.shouldNotReachHere();
                }
            } else if (bytecode[bci] instanceof GTEOpCode gteOpCode) {
                Object left = getFromFrame(frame, gteOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, gteOpCode.getRightRegIdx());
                int regIdx = gteOpCode.getRegIdx();
                switch (left) {
                    case Long l1 when right instanceof Long l2 -> writeBooleanToFrame(frame, regIdx, l1 >= l2);
                    case Double d1 when right instanceof Double d2 -> writeBooleanToFrame(frame, regIdx, d1 >= d2);
                    case Long l when right instanceof Double d -> writeBooleanToFrame(frame, regIdx, l >= d);
                    case Double d when right instanceof Long l -> writeBooleanToFrame(frame, regIdx, d >= l);
                    case null, default -> throw CompilerDirectives.shouldNotReachHere();
                }
            } else if (bytecode[bci] instanceof LTOpCode ltOpCode) {
                Object left = getFromFrame(frame, ltOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, ltOpCode.getRightRegIdx());
                int regIdx = ltOpCode.getRegIdx();
                switch (left) {
                    case Long l1 when right instanceof Long l2 -> writeBooleanToFrame(frame, regIdx, l1 < l2);
                    case Double d1 when right instanceof Double d2 -> writeBooleanToFrame(frame, regIdx, d1 < d2);
                    case Long l when right instanceof Double d -> writeBooleanToFrame(frame, regIdx, l < d);
                    case Double d when right instanceof Long l -> writeBooleanToFrame(frame, regIdx, d < l);
                    case null, default -> throw CompilerDirectives.shouldNotReachHere();
                }
            } else if (bytecode[bci] instanceof LTEOpCode lteOpCode) {
                Object left = getFromFrame(frame, lteOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, lteOpCode.getRightRegIdx());
                int regIdx = lteOpCode.getRegIdx();
                switch (left) {
                    case Long l1 when right instanceof Long l2 -> writeBooleanToFrame(frame, regIdx, l1 <= l2);
                    case Double d1 when right instanceof Double d2 -> writeBooleanToFrame(frame, regIdx, d1 <= d2);
                    case Long l when right instanceof Double d -> writeBooleanToFrame(frame, regIdx, l <= d);
                    case Double d when right instanceof Long l -> writeBooleanToFrame(frame, regIdx, d <= l);
                    case null, default -> throw CompilerDirectives.shouldNotReachHere();
                }
            } else if (bytecode[bci] instanceof EQOpCode eqOpCode) {
                Object left = getFromFrame(frame, eqOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, eqOpCode.getRightRegIdx());
                int regIdx = eqOpCode.getRegIdx();
                writeBooleanToFrame(frame, regIdx, left == right);
            } else if (bytecode[bci] instanceof RefEqOpCode refEqOpCode) {
                Object left = getFromFrame(frame, refEqOpCode.getLeftRegIdx());
                Object right = getFromFrame(frame, refEqOpCode.getRightRegIdx());
                int regIdx = refEqOpCode.getRegIdx();
                writeBooleanToFrame(frame, regIdx, left == right);
            } else if (bytecode[bci] instanceof NegateOpCode negateOpCode) {
                Object value = getFromFrame(frame, negateOpCode.getReceiverRegIdx());
                int regIdx = negateOpCode.getRegIdx();
                switch (value) {
                    case Long l -> writeLongToFrame(frame, regIdx, -l);
                    case Double d -> writeDoubleToFrame(frame, regIdx, -d);
                    case null, default -> throw CompilerDirectives.shouldNotReachHere();
                }
            } else if (bytecode[bci] instanceof NotOpCode notOpCode) {
                Object value = getFromFrame(frame, notOpCode.getReceiverRegIdx());
                int regIdx = notOpCode.getRegIdx();
                if (value instanceof Boolean b) {
                    writeBooleanToFrame(frame, regIdx, !b);
                } else {
                    throw CompilerDirectives.shouldNotReachHere("Unsupported not operation");
                }
            } else if (bytecode[bci] instanceof WriteFieldOpCode writeFieldOpCode) {
                Object receiver = getFromFrame(frame, writeFieldOpCode.getReceiverRegIdx());
                if (!(receiver instanceof NomObject nomObject)) {
                    throw CompilerDirectives.shouldNotReachHere("Receiver is not an object");
                }
                Object value = getFromFrame(frame, writeFieldOpCode.getArgRegIdx());
                CompilerDirectives.transferToInterpreterAndInvalidate();
                TruffleString fieldName = NomContext.constants.GetString(writeFieldOpCode.getNameId()).Value();
                nomObject.writeMember(fieldName, value);
            } else if (bytecode[bci] instanceof ReadFieldOpCode readFieldOpCode) {
                Object receiver = getFromFrame(frame, readFieldOpCode.getReceiverRegIdx());
                if (!(receiver instanceof NomObject nomObject)) {
                    throw CompilerDirectives.shouldNotReachHere("Receiver is not an object");
                }
                CompilerDirectives.transferToInterpreterAndInvalidate();
                TruffleString fieldName = NomContext.constants.GetString(readFieldOpCode.getNameId()).Value();
                Object result;
                try {
                    result = nomObject.readMember(fieldName);
                } catch (UnknownIdentifierException e) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    throw CompilerDirectives.shouldNotReachHere(e);
                }
                if (result == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    throw CompilerDirectives.shouldNotReachHere("Field not found: " + fieldName);
                }
                writeToFrame(frame, readFieldOpCode.getRegIdx(), result);
            } else if (bytecode[bci] instanceof BranchOpCode branchOpCode) {
                int exchangeCnt = branchOpCode.getIncomings().length;
                for (int i = 0; i < exchangeCnt; i++) {
                    int src = branchOpCode.getIncomings()[i];
                    int dst = branchOpCode.getOutgoings()[i];
                    if (src >= argCount) {
                        frame.copy(src - argCount, dst - argCount);
                    } else {
                        writeToFrame(frame, dst, getFromFrame(frame, src));
                    }
                }

                nextBCI = branchOpCode.getTarget();
            } else if (bytecode[bci] instanceof CondBranchOpCode condBranchOpCode) {
                Object conditionObj = getFromFrame(frame, condBranchOpCode.getCondRegIdx());
                if (!(conditionObj instanceof Boolean condition)) {
                    throw CompilerDirectives.shouldNotReachHere("Condition is not a boolean");
                }
                if (condition) {
                    int exchangeCnt = condBranchOpCode.getTrueIncomings().length;
                    for (int i = 0; i < exchangeCnt; i++) {
                        int src = condBranchOpCode.getTrueIncomings()[i];
                        int dst = condBranchOpCode.getTrueOutgoings()[i];
                        if (src >= argCount) {
                            frame.copy(src - argCount, dst - argCount);
                        } else {
                            writeToFrame(frame, dst, getFromFrame(frame, src));
                        }
                    }

                    nextBCI = condBranchOpCode.getTrueTarget();
                } else {
                    int exchangeCnt = condBranchOpCode.getFalseIncomings().length;
                    for (int i = 0; i < exchangeCnt; i++) {
                        int src = condBranchOpCode.getFalseIncomings()[i];
                        int dst = condBranchOpCode.getFalseOutgoings()[i];
                        if (src >= argCount) {
                            frame.copy(src - argCount, dst - argCount);
                        } else {
                            writeToFrame(frame, dst, getFromFrame(frame, src));
                        }
                    }

                    nextBCI = condBranchOpCode.getFalseTarget();
                }
            } else if (bytecode[bci] instanceof PhiOpCode) {
            } else {
                CompilerDirectives.shouldNotReachHere();
            }

            bci = nextBCI;
        }

        return null;
    }

    @CompilerDirectives.TruffleBoundary
    private void invokeConstructor(InvokeCtorOpCode invokeCtorOpCode, Object[] args) {
        NomSuperClassConstant superClassConstant = NomContext.constants.GetSuperClass(invokeCtorOpCode.getNameId());
        TruffleString name = superClassConstant.GetSuperClass().GetName();
        NomFunction function = NomContext.ctorFunctions
                .get(name).get(args.length);
        assert function != null;

        function.getCallTarget().call(args);
    }

    @CompilerDirectives.TruffleBoundary
    private Object invokeStaticMethod(InvokeStaticMethodOpCode invokeStaticMethod, Object[] args) {
        NomFunction function = getStaticMethod(invokeStaticMethod);
        assert function != null;
        return function.getCallTarget().call(args);
    }

    private NomFunction getStaticMethod(InvokeStaticMethodOpCode invokeStaticMethod) {
        NomMethodConstant method = NomContext.constants.GetStaticMethod(invokeStaticMethod.getNameId());
        return NomContext.getMethod(method);
    }

    @CompilerDirectives.TruffleBoundary
    private Object invokeInstanceMethod(InvokeInstanceMethodOpCode invokeInstanceMethod,
                                        Object instance, Object[] args) {
        NomFunction function = getInstanceMethod(invokeInstanceMethod, instance, args);
        if (function == null) {
            throw CompilerDirectives.shouldNotReachHere("Method not found");
        }
        return function.getCallTarget().call(args);
    }

    private NomFunction getInstanceMethod(InvokeInstanceMethodOpCode invokeInstanceMethod,
                                          Object instance, Object[] args) {
        NomMethodConstant method = NomContext.constants.GetMethod(invokeInstanceMethod.getNameId());
        NomFunction function = NomContext.getMethod(method);
        if_branch:
        if (function == null) {
            if (instance instanceof NomObject obj) {
                if (method.MethodName().toString().endsWith(".")) {
                    function = obj.thisFunction;
                    break if_branch;
                }
                function = obj.GetFunction(method.MethodName());
                if (function == null && obj.GetClass() != null) {
                    Object member;
                    try {
                        member = obj.readMember(method.MethodName());
                    } catch (UnknownIdentifierException e) {
                        CompilerDirectives.transferToInterpreterAndInvalidate();
                        throw CompilerDirectives.shouldNotReachHere(e);
                    }
                    if (member instanceof NomObject memObj) {
                        if (memObj.thisFunction != null) {
                            function = memObj.thisFunction;
                            args[0] = memObj;
                        }
                    }
                }
            }
        }
        return function;
    }


    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (TransformedOpCode node : bytecode) {
            ret.append(node.toString()).append("\n");
        }
        return ret.toString();
    }
}
