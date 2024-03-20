package com.nom.graalnom.runtime.reflections;

public enum UnaryOperator {
    Negate,
    Not;

    public static UnaryOperator fromValue(int value) {
        return UnaryOperator.values()[value];
    }
}