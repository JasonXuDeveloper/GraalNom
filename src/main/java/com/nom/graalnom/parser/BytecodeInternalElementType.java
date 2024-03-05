package com.nom.graalnom.parser;
public enum BytecodeInternalElementType {
    Field(201),
    StaticMethod(202),
    Method(203),
    Constructor(204),
    Lambda(205),
    Record(206);
    private final int value;

    BytecodeInternalElementType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BytecodeInternalElementType fromValue(int value){
        for (BytecodeInternalElementType e : BytecodeInternalElementType.values()){
            if (e.getValue() == value){
                return e;
            }
        }
        return null;
    }
}