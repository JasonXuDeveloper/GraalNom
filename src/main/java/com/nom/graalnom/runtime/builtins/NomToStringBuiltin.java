package com.nom.graalnom.runtime.builtins;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.strings.TruffleString;


@NodeInfo(shortName = "ToString")
public abstract class NomToStringBuiltin extends NomBuiltinNode {

    @Specialization
    protected Object doDefault(long num) {
        return TruffleString.fromLongUncached(num, TruffleString.Encoding.UTF_8, true);
    }

    @Specialization
    protected Object doDefault(double num) {
        return TruffleString.fromJavaStringUncached(Double.toString(num), TruffleString.Encoding.UTF_8);
    }

    @Specialization
    protected Object doDefault(boolean bool) {
        return TruffleString.fromJavaStringUncached(Boolean.toString(bool), TruffleString.Encoding.UTF_8);
    }

    @Specialization
    protected Object doDefault(TruffleString str) {
        return str;
    }

    @Specialization
    protected Object doDefault(Object obj) {
        return TruffleString.fromJavaStringUncached(obj.toString(), TruffleString.Encoding.UTF_8);
    }
}
