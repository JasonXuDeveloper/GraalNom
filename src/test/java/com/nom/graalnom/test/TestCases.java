package com.nom.graalnom.test;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedInputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestCases {
    private void PrintConstants() {
        System.out.println();
        System.out.println("Constants:");
        for (var constant : NomContext.constants.Constants()) {
            System.out.println("Global Id: " +
                    NomContext.constants.Constants()
                            .indexOf(constant) + " " +
                    (constant != null ? constant.getClass().getSimpleName() : "null"));
        }

    }

    private static void Compile(String mainClass) throws Exception {
        Path path = Paths.get("src/tests/Test.mnp");
        //load manifest file in manifestPath
        String manifest = Files.readString(path);
        manifest = manifest.strip();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var doc = builder.parse(new InputSource(new StringReader(manifest)));
        doc.getElementsByTagName("project").item(0).getAttributes().getNamedItem("mainclass").setNodeValue(mainClass);
        //doc to string
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(path.toFile());
        transformer.transform(source, result);
        //call shell
        String[] cmd = {"sh", "compiler.sh", "-p ./src/tests", "--project Test"};
        Process process = Runtime.getRuntime().exec(cmd);
        int exitValue = process.waitFor();
        if (exitValue != 0) {
            // check for errors
            BufferedInputStream bis = new BufferedInputStream(process.getErrorStream());
            byte[] bytes = new byte[1000];
            int bytesRead;
            StringBuilder output = new StringBuilder();
            while ((bytesRead = bis.read(bytes)) > 0) {
                output.append(new String(bytes, 0, bytesRead));
            }
            System.out.println(output);
            throw new RuntimeException("execution of script failed!");
        }
    }

    @Test
    public void SimpleTest() throws Exception {
        Compile("SimpleTest");
        Path path = Paths.get("src/tests/Test.manifest");
        //to absolute path
        path = path.toAbsolutePath();
        //create a new context
        try (Context context = Context.create()) {
            Value ret = context.eval(NomLanguage.ID, path.toString());
            assert ret.isBoolean();
            assert !ret.asBoolean();
        }
    }

    @Test
    public void BranchTest() throws Exception {
        Compile("BranchTest");
        Path path = Paths.get("src/tests/Test.manifest");
        //to absolute path
        path = path.toAbsolutePath();
        //create a new context
        try (Context context = Context.create()) {
            Value ret = context.eval(NomLanguage.ID, path.toString());
            assert ret.isBoolean();
            assert ret.asBoolean() == BranchTestMainJava();
        }
    }

    private boolean BranchTestMainJava() {
        int a = 10;
        int b = 256;
        int c = 1024;
        return Branch1TestJava(a, b, c) && Branch2TestJava(a, b, c);
    }

    private boolean Branch1TestJava(int a, int b, int c) {
        if (a > b) {
            if (a > c) {
                return true;
            } else {
                return false;
            }
        } else {
            if (b > c) {
                return true;
            }
        }
        return false;
    }

    private boolean Branch2TestJava(int a, int b, int c) {
        if (a > b) {
            return !Branch1TestJava(a, b, c);
        }
        return Branch1TestJava(a, b, c);
    }
}
