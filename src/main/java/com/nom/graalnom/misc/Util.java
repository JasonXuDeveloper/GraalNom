package com.nom.graalnom.misc;

import com.google.common.io.LittleEndianDataInputStream;

import java.nio.charset.StandardCharsets;

public class Util {
    public static String ReadUtf16(LittleEndianDataInputStream s) throws Exception{
        long len = s.readLong();
        byte[] bytes =  s.readNBytes((int)len *2);
        return new String(bytes, StandardCharsets.UTF_16LE);
    }
}
