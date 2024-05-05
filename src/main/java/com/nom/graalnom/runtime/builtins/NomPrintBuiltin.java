package com.nom.graalnom.runtime.builtins;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.datatypes.NomNull;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "Print")
public abstract class NomPrintBuiltin extends NomBuiltinNode {

    @Specialization
    protected Object doDefault(String str) {
        System.out.println(str);
        return NomNull.SINGLETON;
    }

    @Fallback
    protected Object typeError(Object str) {
        throw new RuntimeException("Type error: " + str.getClass());
    }
}
