package com.nom.graalnom.runtime.builtins;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.datatypes.NomNull;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.strings.TruffleString;

@NodeInfo(shortName = "Print")
public abstract class NomPrintBuiltin extends NomBuiltinNode {

    @Specialization
    protected Object doDefault(String str) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        NomContext.get(this).getOutput().println(str);
        return NomNull.SINGLETON;
    }

    @Fallback
    protected Object typeError(Object str) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        throw CompilerDirectives.shouldNotReachHere("Type error: " + str.getClass());
    }
}