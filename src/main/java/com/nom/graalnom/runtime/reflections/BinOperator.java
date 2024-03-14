package com.nom.graalnom.runtime.reflections;

public enum BinOperator {
    Equals,
    RefEquals,
    Add,
    Subtract,
    Multiply,
    Divide,
    Power,
    Mod,
    Concat,
    And,
    Or,
    BitAND,
    BitOR,
    BitXOR,
    ShiftLeft,
    ShiftRight,
    LessThan,
    GreaterThan,
    LessOrEqualTo,
    GreaterOrEqualTo;

    public static BinOperator fromValue(int value) {
        return BinOperator.values()[value];
    }
}