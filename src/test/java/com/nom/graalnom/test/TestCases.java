package com.nom.graalnom.test;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.nom.graalnom.runtime.reflections.NomInterface;
import com.nom.graalnom.test.java.*;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.TypeLiteral;
import org.graalvm.polyglot.Value;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestCases {
    private static final String testPath = "src/test/monnom/";
    private static final String compiledManifestPath = testPath + "Test.manifest";
    private static final String configManifestPath = testPath + "Test.mnp";
    private static String configManifestVal;

    @BeforeAll
    public static void SetUp() throws Exception {
        //collect test files
        List<String> testFiles = new ArrayList<>();
        List<String> testNames = new ArrayList<>();
        for (Method m : TestCases.class.getMethods()) {
            if (m.isAnnotationPresent(MonNomTest.class)) {
                MonNomTest source = m.getAnnotation(MonNomTest.class);
                testFiles.add(source.filename());
                testNames.add(m.getName());
            }
        }
        if (testFiles.isEmpty()) {
            throw new Exception("No test files found");
        }
        String mainClassName = testNames.getFirst();
        //inject config manifest
        TestUtil.InjectFiles(configManifestPath, mainClassName, testFiles.toArray(new String[0]));
        //compile bytecode
        TestUtil.Compile(testPath);
        //load bytecode
        context = Context.create();
        context.eval(NomLanguage.ID,
                GetTestString("", false, true, true));
        configManifestVal = Files.readString(Paths.get(configManifestPath));
    }

    @BeforeEach
    public void BeforeEach() throws IOException {
        //restore config manifest
        Files.writeString(Paths.get(configManifestPath), configManifestVal);
    }

    @AfterAll
    public static void TearDown() throws Exception {
        SetUp();
        for (NomInterface cls : NomContext.classes.values()) {
            TestUtil.ExportClassMethodsDotGraphs(cls,
                    Paths.get(testPath, "graphs").toString());
        }
        context.close();
    }

    private static Context context;

    private static String GetTestString(String mainClassName, boolean invokeMain,
                                        boolean debug, boolean ignoreErrorBytecode) {
        JSONObject jo = new JSONObject();
        jo.put("manifestPath", Paths.get(compiledManifestPath).toAbsolutePath());
        jo.put("mainClass", mainClassName);
        jo.put("invokeMain", invokeMain);
        jo.put("debug", debug);
        jo.put("ignoreErrorBytecode", ignoreErrorBytecode);
        return jo.toString(1);
    }

    private static Value RunTest() {
        String nameofCurrMethod = Thread.currentThread()
                .getStackTrace()[2]
                .getMethodName();
        boolean debug;
        try {
            debug = TestCases.class.getMethod(nameofCurrMethod)
                    .isAnnotationPresent(ByteCodeDebug.class);
            if (debug) {
                //inject config manifest
                TestUtil.InjectFiles(configManifestPath, nameofCurrMethod,
                        TestCases.class.getMethod(nameofCurrMethod).getAnnotation(MonNomTest.class).filename());
                //compile bytecode
                TestUtil.Compile(testPath);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(nameofCurrMethod + " output:");
        Value ret = context.eval(NomLanguage.ID,
                GetTestString(nameofCurrMethod, true, debug, !debug));
        System.out.println();
        System.out.println("Returned value:");
        System.out.println(ret);
        if (debug) {
            try {
                SetUp();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return ret;
    }

    @MonNomTest(filename = "simple")
    public void SimpleTest() {
        Value ret = RunTest();
        assert ret.isNumber();
        assert ret.asLong() == 30;
    }

    @MonNomTest(filename = "branch")
    public void BranchTest() {
        Value ret = RunTest();
        assert ret.isBoolean();
        assert ret.asBoolean() == new BranchTestJavaImpl().BranchTestMainJava();
    }

    @MonNomTest(filename = "while")
    public void WhileTest() {
        Value ret = RunTest();
        assert ret.isBoolean();
        assert ret.asBoolean() == new WhileTestJavaImpl().WhileTestMainJava();
    }

    @MonNomTest(filename = "mini")
    public void MiniTest() {
        Value ret = RunTest();
        assert ret.isNull();
    }

    @MonNomTest(filename = "object")
    public void ObjectTest() {
        Value ret = RunTest();
        assert ret.isNumber();
        assert ret.asLong() == 6;
    }

    @MonNomTest(filename = "sieve")
    public void SieveTest() {
        Value ret = RunTest();
    }

    @MonNomTest(filename = "dyn")
    public void DynTest() {
        Value ret = RunTest();
    }
}
