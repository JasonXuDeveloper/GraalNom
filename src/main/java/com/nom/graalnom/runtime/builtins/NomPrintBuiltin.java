package com.nom.graalnom.runtime.builtins;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.strings.TruffleString;

@NodeInfo(shortName = "Print")
public abstract class NomPrintBuiltin extends NomBuiltinNode {

    @Specialization
    protected Object doDefault(TruffleString str) {
        NomContext.get(this).getOutput().println(str);
        return 0;
    }

    @Fallback
    protected Object typeError(Object str) {
        throw new RuntimeException("Type error: " + str.getClass());
    }
}
