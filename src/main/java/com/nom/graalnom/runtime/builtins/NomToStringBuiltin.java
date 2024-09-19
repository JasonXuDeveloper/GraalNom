package com.nom.graalnom.runtime.builtins;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.strings.TruffleString;


@NodeInfo(shortName = "ToString")
public abstract class NomToStringBuiltin extends NomBuiltinNode {

    @Specialization
    protected Object doDefault(long num) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return TruffleString.fromLongUncached(num, TruffleString.Encoding.UTF_8, true);
    }

    @Specialization
    protected Object doDefault(double num) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return TruffleString.fromJavaStringUncached(Double.toString(num), TruffleString.Encoding.UTF_8);
    }

    @Specialization
    protected Object doDefault(boolean bool) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return TruffleString.fromJavaStringUncached(Boolean.toString(bool), TruffleString.Encoding.UTF_8);
    }

    @Specialization
    protected Object doDefault(TruffleString str) {
        return str;
    }
}
