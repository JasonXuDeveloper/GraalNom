package com.nom.graalnom.test;

import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.nodes.NomRootNode;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.nom.graalnom.runtime.reflections.NomInterface;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestUtil {
    public static void PrintConstants() {
        System.out.println();
        System.out.println("Constants:");
        for (var constant : NomContext.constants.Constants()) {
            System.out.println("Global Id: " +
                    NomContext.constants.Constants()
                            .indexOf(constant) + " " +
                    (constant != null ? constant.getClass().getSimpleName() : "null"));
        }
    }

    public static void Compile(String nomProjPath) throws Exception {
        //call shell
        ExecuteShell("sh", "compiler.sh", "-p " + nomProjPath, "--project Test");
    }

    public static void ExecuteShell(String... cmd) throws Exception {
        //call shell
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

    public static void InjectFiles(String manifestPath, String mainClass, String... files) throws Exception {
        Path path = Paths.get(manifestPath);
        //load manifest file in manifestPath
        String manifest = Files.readString(path);
        manifest = manifest.strip();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var doc = builder.parse(new InputSource(new StringReader(manifest)));
        doc.getElementsByTagName("project").item(0).getAttributes().getNamedItem("mainclass").setNodeValue(mainClass);
        var filesNode = doc.getElementsByTagName("files")
                .item(0);
        filesNode.setTextContent("");
        for (var file : files) {
            var node = doc.createElement("file");
            String f = file;
            if (!f.endsWith(".mn"))
                f += ".mn";
            node.setAttribute("name", f);
            filesNode.appendChild(node);
        }
        //doc to string
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(path.toFile());
        transformer.transform(source, result);
    }
}
