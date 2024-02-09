package com.nom.graalnom.test;

import com.nom.graalnom.parser.ByteCodeReader;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomConstant;
import org.junit.Test;

import java.io.*;
import java.util.List;

public class Test0 {
    @Test
    public void Parse() {
        try {
            ByteCodeReader.ReadBytecodeFile("src/tests/Test_0.mnil");
        } catch (Exception e) {
            List<NomConstant> constants = NomContext.constants;
            for (int i = 0; i < constants.size(); i++) {
                var c = constants.get(i);
                if (c == null) {
                    continue;
                }
                System.out.print("globalId = " + i + ", val = ");
                c.Print(true);
                System.out.println();
            }

            throw e;
        }
    }
}
