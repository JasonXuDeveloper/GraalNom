package com.nom.graalnom.runtime;

import com.google.common.io.LittleEndianDataInputStream;
import com.nom.graalnom.NomLanguage;
import com.oracle.truffle.api.strings.TruffleString;

public final class NomString {
    public static TruffleString create(String string) {
        return TruffleString.fromJavaStringUncached(string, NomLanguage.STRING_ENCODING);
    }

    public static TruffleString create(LittleEndianDataInputStream s) throws Exception{
        long length = s.readLong();
        char[] chars = new char[(int)length];
        for (int i = 0; i < length; i++) {
            chars[i] = s.readChar();
        }
        return TruffleString.fromCharArrayUTF16Uncached(chars);
    }
}
