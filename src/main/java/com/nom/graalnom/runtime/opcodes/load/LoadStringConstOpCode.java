package com.nom.graalnom.runtime.opcodes.load;

import com.oracle.truffle.api.strings.TruffleString;

public class LoadStringConstOpCode extends LoadConstOpCode<TruffleString> {
    public LoadStringConstOpCode(int regIndex, TruffleString value) {
        super(regIndex, value);
    }
}
