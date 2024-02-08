package com.nom.graalnom.parser;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import com.google.common.io.LittleEndianDataInputStream;
import com.nom.graalnom.misc.Util;

public class ByteCodeReader {
    public static int BYTECODE_VERSION = 2;
    public static void ReadBytecodeFile(String filename){
        if (filename == null || !Files.exists(Paths.get(filename))){
            throw new IllegalArgumentException("file not found");
        }

        try(FileInputStream fs = new FileInputStream(filename)){
            LittleEndianDataInputStream s = new LittleEndianDataInputStream(fs);
            int ver = s.readInt();
            if (ver > BYTECODE_VERSION){
                throw new IllegalArgumentException("file" +filename +" (ver"+ver+") is too new");
            }

            HashMap<Long, String> stringConstants = new HashMap<>();
            while(s.available() > 0){
                int b  = s.read();//need to use int to get 0-255, I HATE JAVA
                BytecodeTopElementType nextType = BytecodeTopElementType.fromValue(b);
                System.out.println(nextType);
                long lcId, cnId, tlId;
                switch (nextType){
                    case StringConstant:
                        long id = s.readLong();
                        String str = Util.ReadUtf16(s);
                        System.out.println("id: " + id + " str: " + str);
                        stringConstants.put(id, str);
                        break;
                    case ClassConstant:
                        long libId = s.readLong();
                        cnId = s.readLong();
                        lcId = s.readLong();//no clue what is this
                        System.out.println("class const lib(id="+libId+"): " + stringConstants.get(libId) + " cnId(id="+cnId+"): "
                                + stringConstants.get(cnId) + " lcId(id="+lcId+"): "+ stringConstants.get(lcId));
                        break;
                    case SuperClass:
                        lcId = s.readLong();
                        cnId = s.readLong();
                        tlId = s.readLong();
                        System.out.println("class const lcId(id="+lcId+"): " + stringConstants.get(lcId) + " cnId(id="+cnId+"): "
                                + stringConstants.get(cnId) + " tlId(id="+tlId+"): "+ stringConstants.get(tlId));
                        break;
                    case null:
                    default:
                        throw new IllegalArgumentException("unknown type ("+b+"): " +nextType);
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
