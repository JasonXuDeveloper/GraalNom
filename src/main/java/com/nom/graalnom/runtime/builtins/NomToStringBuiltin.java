package com.nom.graalnom.runtime.builtins;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;


@NodeInfo(shortName = "ToString")
public abstract class NomToStringBuiltin extends NomBuiltinNode {

    @Specialization
    protected Object doDefault(long num) {
        return Long.toString(num);
    }

    @Specialization
    protected Object doDefault(double num) {
        return Double.toString(num);
    }

    @Specialization
    protected Object doDefault(boolean bool) {
        return Boolean.toString(bool);
    }

    @Specialization
    protected Object doDefault(String str) {
        return str;
    }

    @Specialization
    protected Object doDefault(Object obj) {
        return obj.toString();
    }
}
