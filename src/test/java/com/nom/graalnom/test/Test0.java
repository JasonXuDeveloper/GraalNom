package com.nom.graalnom.test;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.datatypes.NomNull;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
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
        try(Context context = Context.create()){
            Value ret = context.eval(NomLanguage.ID, path.toString());
            assert ret.isNull();
            assert ret.hashCode() == NomNull.SINGLETON.hashCode();
        }
    }
}
