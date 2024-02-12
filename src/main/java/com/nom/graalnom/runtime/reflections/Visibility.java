package com.nom.graalnom.runtime.reflections;

public enum Visibility {
    Private(1),
    Protected(3),
    Internal(7),
    ProtectedInternal(15),
    Public(31);

    private final int value;

    Visibility(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Visibility fromValue(int value) {
        for (Visibility v : Visibility.values()) {
            if (v.getValue() == value) {
                return v;
            }
        }
        return null;
    }
}
