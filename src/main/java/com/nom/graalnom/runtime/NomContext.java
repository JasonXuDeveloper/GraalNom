package com.nom.graalnom.runtime;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.builtins.*;
import com.nom.graalnom.runtime.constants.*;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.nodes.NomRootNode;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.nom.graalnom.runtime.reflections.NomInterface;
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
    public static NomConstants constants = new NomConstants();

    public static final Map<TruffleString, NomInterface> classes = new java.util.HashMap<>();

    public static final Map<NomInterface, Map<TruffleString, NomFunction>> functionsObject = new HashMap<>();
    public static final Map<TruffleString, Map<Integer, NomFunction>> ctorFunctions = new HashMap<>();
    public static final Map<TruffleString, NomFunction> builtinFunctions = new HashMap<>();

    public static void clear() {
        classes.clear();
        functionsObject.clear();
    }

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
        installBuiltin(NomPrintDifferenceBuiltinFactory.getInstance());
    }

    public void installBuiltin(NodeFactory<? extends NomBuiltinNode> factory) {
        /* Register the builtin function in our function registry. */
        RootCallTarget target = language.lookupBuiltin(factory);
        NomFunction function = new NomFunction(TruffleString.fromJavaStringUncached(target.getRootNode().getName(), TruffleString.Encoding.UTF_8), null, target,0);
        builtinFunctions.put(function.getName(), function);
    }

    /**
     * Return the current Truffle environment.
     */
    public TruffleLanguage.Env getEnv() {
        return env;
    }


    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public static NomFunction getMethod(NomMethodConstant method) {
        TruffleString methName = method.MethodName();
        if (method.Class() != null && NomContext.functionsObject.containsKey(method.Class())) {
            Map<TruffleString, NomFunction> clsFunctions = NomContext.functionsObject.get(method.Class());
            if (clsFunctions.containsKey(methName)) {
                return clsFunctions.get(methName);
            }
        } else if (NomContext.builtinFunctions.containsKey(methName)) {
            return NomContext.builtinFunctions.get(methName);
        }

        return null;
    }
}
