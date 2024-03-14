package com.nom.graalnom.runtime;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.builtins.NomBuiltinNode;
import com.nom.graalnom.runtime.builtins.NomPrintBuiltin;
import com.nom.graalnom.runtime.builtins.NomPrintBuiltinFactory;
import com.nom.graalnom.runtime.builtins.NomToStringBuiltinFactory;
import com.nom.graalnom.runtime.constants.*;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.datatypes.NomString;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.strings.TruffleString;
import org.graalvm.collections.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NomContext {
    public static final NomConstants constants = new NomConstants();

    public static final Map<String, NomClass> classes = new java.util.HashMap<>();

    public static final Map<NomClass, Map<String, NomFunction>> functionsObject = new HashMap<>();
    public static final Map<String, NomFunction> builtinFunctions = new HashMap<>();
    private static final TruffleLanguage.ContextReference<NomContext> REFERENCE = TruffleLanguage.ContextReference.create(NomLanguage.class);

    public static NomContext get(Node node) {
        return REFERENCE.get(node);
    }

    private final NomLanguage language;
    @CompilerDirectives.CompilationFinal
    private TruffleLanguage.Env env;
    private final BufferedReader input;
    private final PrintWriter output;

    public NomContext(NomLanguage language, TruffleLanguage.Env env) {
        this.language = language;
        this.env = env;
        this.input = new BufferedReader(new InputStreamReader(env.in()));
        this.output = new PrintWriter(env.out(), true);
        installBuiltins();
    }


    private void installBuiltins() {
        installBuiltin(NomToStringBuiltinFactory.getInstance());
        installBuiltin(NomPrintBuiltinFactory.getInstance());
    }

    public void installBuiltin(NodeFactory<? extends NomBuiltinNode> factory) {
        /* Register the builtin function in our function registry. */
        RootCallTarget target = language.lookupBuiltin(factory);
        NomFunction function = new NomFunction(NomString.create(target.getRootNode().getName()), target);
        builtinFunctions.put(function.getName().toString(), function);
    }

    /**
     * Return the current Truffle environment.
     */
    public TruffleLanguage.Env getEnv() {
        return env;
    }


    /**
     * Returns the default input, i.e., the source for the {@link SLReadlnBuiltin}. To allow unit
     * testing, we do not use {@link System#in} directly.
     */
    public BufferedReader getInput() {
        return input;
    }

    /**
     * The default default, i.e., the output for the {@link SLPrintlnBuiltin}. To allow unit
     * testing, we do not use {@link System#out} directly.
     */
    public PrintWriter getOutput() {
        return output;
    }
}
