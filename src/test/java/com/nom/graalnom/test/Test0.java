package com.nom.graalnom.test;

import com.nom.graalnom.NomLanguage;
import org.graalvm.polyglot.Context;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Test0 {
    @Test
    public void Parse() {
        Path path = Paths.get("src/tests/test0/Test.manifest");
        //to absolute path
        path = path.toAbsolutePath();
        //create a new context
        Context context = Context.create();
        context.eval(NomLanguage.ID, path.toString());
    }
}
