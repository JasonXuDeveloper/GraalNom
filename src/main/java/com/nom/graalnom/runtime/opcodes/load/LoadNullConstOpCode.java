package com.nom.graalnom.runtime.opcodes.load;

import com.nom.graalnom.runtime.datatypes.NomNull;

public class LoadNullConstOpCode extends LoadConstOpCode<NomNull> {
    public LoadNullConstOpCode(int reg) {
        super(reg, NomNull.SINGLETON);
    }
}
