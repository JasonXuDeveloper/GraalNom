package com.nom.graalnom.test;

import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.reflections.NomClass;

import java.io.BufferedInputStream;
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

    public static NomClass GetClass(String name) {
        return NomContext.classes.values().stream().filter(c -> c.GetName().toString().equals(name + "_0")).findFirst().orElse(null);
    }

    public static void PrintClassMethods(NomClass cls) {
        System.out.println();
        System.out.println(cls.GetName().toString() + " Class methods:");
        Map<String, NomFunction> map = NomContext.functionsObject.get(cls);
        for (var method : map.values()) {
            System.out.println(method.getCallTarget().getRootNode().toString());
            System.out.println();
        }
    }

    public static void Compile() throws Exception {
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

}
