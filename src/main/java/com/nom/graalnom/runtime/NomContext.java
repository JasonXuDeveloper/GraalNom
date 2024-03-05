package com.nom.graalnom.runtime;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.constants.*;
import com.nom.graalnom.runtime.datatypes.NomString;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.strings.TruffleString;
import org.graalvm.collections.Pair;

import java.util.List;
import java.util.Map;

public class NomContext {
    public static NomConstants constants = new NomConstants();

    public static Map<String, NomClass> classes = new java.util.HashMap<>();

    private final NomLanguage language;
    @CompilerDirectives.CompilationFinal
    private TruffleLanguage.Env env;

    public NomContext(NomLanguage language, TruffleLanguage.Env env) {
        this.language = language;
        this.env = env;
    }
}
