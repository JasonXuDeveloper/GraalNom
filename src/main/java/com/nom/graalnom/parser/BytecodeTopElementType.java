package com.nom.graalnom.parser;
public enum BytecodeTopElementType {
    None(0),
    Class(1),
    Interface(2),
    StringConstant(103),
    IntegerConstant(104),
    FloatConstant(105),
    CTRecord(110),
    ClassConstant(111),
    InterfaceConstant(112),
    CTTypeParameter(113),
    LambdaConstant(114),
    CTIntersection(115),
    CTUnion(116),
    CTBottom(117),
    CTDynamic(118),
    CTMaybe(119),
    ClassTypeConstant(121),
    TypeVarConstant(122),
    CTConstructor(133),
    MethodConstant(131),
    StaticMethodConstant(132),
    SuperClass(141),
    SuperInterfaces(142),
    TypeListConstant(191),
    CTSuperInterfaceList(192),
    CTClassTypeList(193),
    CTTypeParameters(194);

    private final int value;

    BytecodeTopElementType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BytecodeTopElementType fromValue(int value){
        for (BytecodeTopElementType e : BytecodeTopElementType.values()){
            if (e.getValue() == value){
                return e;
            }
        }
        return null;
    }
}