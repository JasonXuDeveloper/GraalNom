package com.nom.graalnom.runtime.builtins;

import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.datatypes.NomTimer;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "PrintDifference")
public abstract class NomPrintDifferenceBuiltin extends NomBuiltinNode {

    @Specialization
    @Override
    protected Object doNomObject(NomObject obj) {
        if (!(obj instanceof NomTimer t)) return super.doNomObject(obj);
        CompilerDirectives.transferToInterpreterAndInvalidate();
        long cur = System.currentTimeMillis();
        long diff = cur - t.curMs;
        NomContext.get(this).getOutput().println("Time difference: " + diff + "ms");
        return 0;
    }

    @Fallback
    protected Object typeError(Object str) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        throw CompilerDirectives.shouldNotReachHere("Type error: " + str.getClass());
    }
}
