package com.nom.graalnom.runtime.builtins;

import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.datatypes.NomTimer;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.lang.management.ManagementFactory;

@NodeInfo(shortName = "PrintDifference")
public abstract class NomPrintDifferenceBuiltin extends NomBuiltinNode {

    @Specialization
    @Override
    @CompilerDirectives.TruffleBoundary
    protected Object doNomObject(NomObject obj) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        if (!(obj instanceof NomTimer t)) return super.doNomObject(obj);
        long cur = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        long diff = cur - t.curMs;
        //nano to milli
        diff /= 1000000;
        System.out.println("Time difference: " + diff + "ms");
        return 0;
    }

    @Fallback
    protected Object typeError(Object str) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        throw CompilerDirectives.shouldNotReachHere("Type error: " + str.getClass());
    }
}
