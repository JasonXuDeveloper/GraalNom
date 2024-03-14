package com.nom.graalnom.runtime;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.constants.*;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.datatypes.NomString;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.strings.TruffleString;
import org.graalvm.collections.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NomContext {
    public static final NomConstants constants = new NomConstants();

    public static final Map<String, NomClass> classes = new java.util.HashMap<>();

    public static final Map<NomClass, Map<String, NomFunction>> functionsObject = new HashMap<>();
    private static final TruffleLanguage.ContextReference<NomContext> REFERENCE = TruffleLanguage.ContextReference.create(NomLanguage.class);

    public static NomContext get(Node node) {
        return REFERENCE.get(node);
    }

    private final NomLanguage language;
    @CompilerDirectives.CompilationFinal
    private TruffleLanguage.Env env;

    public NomContext(NomLanguage language, TruffleLanguage.Env env) {
        this.language = language;
        this.env = env;
    }
}
