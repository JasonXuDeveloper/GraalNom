package com.nom.graalnom.runtime.builtins;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;


@NodeInfo(shortName = "ToString")
public abstract class NomToStringBuiltin extends NomBuiltinNode {

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected Object doDefault(long num) {
        return Long.toString(num);
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected Object doDefault(double num) {
        return Double.toString(num);
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected Object doDefault(boolean bool) {
        return Boolean.toString(bool);
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected Object doDefault(String str) {
        return str;
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    protected Object doDefault(Object obj) {
        return obj.toString();
    }
}
