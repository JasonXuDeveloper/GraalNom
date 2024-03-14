package com.nom.graalnom.runtime.reflections;

public enum OpCode {
    Noop(0),
    PhiNode(1),
    Return(11),
    ReturnVoid(12),
    Argument(13),
    EnsureCheckedMethod(14),
    EnsureDynamicMethod(15),
    LoadStringConstant(21),
    LoadIntConstant(22),
    LoadFloatConstant(23),
    LoadBoolConstant(24),
    LoadNullConstant(25),
    CallCheckedStatic(101),
    CallConstructor(102),
    CreateClosure(103),
    ConstructStruct(104),
    InvokeCheckedInstance(111),
    CallDispatchBest(112),
    CallFinal(113),
    WriteField(51),
    ReadField(52),
    Cast(61),
    Branch(31),
    CondBranch(32),
    UnaryOp(41),
    BinOp(42),
    Debug(121),
    RTCmd(122),
    Error(123);

    private final int value;

    OpCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OpCode fromValue(int value) {
        for (OpCode v : OpCode.values()) {
            if (v.getValue() == value) {
                return v;
            }
        }
        return null;
    }
    }