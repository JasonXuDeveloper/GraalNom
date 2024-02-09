package com.nom.graalnom.runtime.constants;

public enum NomConstantType {
    CTString(103),
    CTInteger(104),
    CTFloat(105),
    CTRecord(110),
    CTClass(111),
    CTInterface(112),
    CTTypeParameter(113),
    CTLambda(114),
    CTIntersection(115),
    CTUnion(116),
    CTBottom(117),
    CTDynamic(118),
    CTMaybe(119),
    CTClassType(121),
    CTTypeVar(122),
    CTMethod(131),
    CTStaticMethod(132),
    CTConstructor(133),
    CTSuperClass(141),
    CTSuperInterfaces(142),
    CTTypeList(191),
    CTSuperInterfaceList(192),
    CTClassTypeList(193),
    CTTypeParameters(194);

    private final int value;

    NomConstantType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
