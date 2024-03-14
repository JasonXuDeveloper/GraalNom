package com.nom.graalnom.runtime.builtins;

import com.nom.graalnom.runtime.datatypes.NomString;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.strings.TruffleString;


@NodeInfo(shortName = "ToString")
public abstract class NomToStringBuiltin extends NomBuiltinNode {

    @Specialization
    protected Object doDefault(long num) {
        return NomString.create(Long.toString(num));
    }

    @Specialization
    protected Object doDefault(double num) {
        return NomString.create(Double.toString(num));
    }

    @Specialization
    protected Object doDefault(boolean bool) {
        return NomString.create(Boolean.toString(bool));
    }

    @Specialization
    protected Object doDefault(TruffleString str) {
        return str;
    }

    @Specialization
    protected Object doDefault(Object obj) {
        return NomString.create(obj.toString());
    }
}
