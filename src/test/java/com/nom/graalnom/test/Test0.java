package com.nom.graalnom.test;

import com.nom.graalnom.parser.ByteCodeReader;
import org.junit.Test;

import java.io.*;

public class Test0 {
    @Test
    public void Parse(){
        ByteCodeReader.ReadBytecodeFile("src/tests/Test_0.mnil");
    }
}
