package com.nom.graalnom.runtime.builtins;

import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.datatypes.NomTimer;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.lang.management.ManagementFactory;

@NodeInfo(shortName = "PrintDifference")
public abstract class NomPrintDifferenceBuiltin extends NomBuiltinNode {

    @Specialization
    @Override
    protected Object doNomObject(NomObject obj) {
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
        throw new RuntimeException("Type error: " + str.getClass());
    }
}
