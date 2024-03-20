package com.nom.graalnom.test;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestCases {
    @Test
    public void Test0() {
        Path path = Paths.get("src/tests/Test.manifest");
        //to absolute path
        path = path.toAbsolutePath();
        //create a new context
        try (Context context = Context.create()) {
            Value ret = context.eval(NomLanguage.ID, path.toString());

            System.out.println();
            System.out.println("Constants:");
            for (var constant : NomContext.constants.Constants()) {
                System.out.println("Global Id: " +
                        NomContext.constants.Constants()
                                .indexOf(constant) + " " +
                        (constant != null ? constant.getClass().getSimpleName() : "null"));
            }

            assert ret.isNumber();
            assert ret.asInt() == 100;
        }
    }
}
