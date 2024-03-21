package com.nom.graalnom.test;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.test.java.*;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class TestCases {
    @BeforeAll
    public static void SetUp() throws Exception {
        TestUtil.Compile();
        context = Context.create();
        context.eval(NomLanguage.ID, GetTestString("", false));
    }

    @AfterAll
    public static void TearDown() {
        context.close();
    }

    private static Context context;

    private static String GetTestString(String mainClassName, boolean invokeMain) {
        return Paths.get("src/tests/Test.manifest").toAbsolutePath()
                + "|" + mainClassName + "_0|" + invokeMain;
    }

    private static Value RunTest() {
        String nameofCurrMethod = Thread.currentThread()
                .getStackTrace()[2]
                .getMethodName();
        System.out.println(nameofCurrMethod + " output:");
        Value ret = context.eval(NomLanguage.ID, GetTestString(nameofCurrMethod, true));
        TestUtil.PrintClassMethods(TestUtil.GetClass(nameofCurrMethod));
        System.out.println();
        System.out.println("Returned value:");
        System.out.println(ret);
        return ret;
    }

    @Test
    public void SimpleTest() {
        Value ret = RunTest();
        assert ret.isNumber();
        assert ret.asLong() == 30;
    }

    @Test
    public void BranchTest() {
        Value ret = RunTest();
        assert ret.isBoolean();
        assert ret.asBoolean() == new BranchTestJavaImpl().BranchTestMainJava();
    }

    @Test
    public void WhileTest() {
        Value ret = RunTest();
        assert ret.isBoolean();
        assert ret.asBoolean() == new WhileTestJavaImpl().WhileTestMainJava();
    }
}
