package com.nom.graalnom;

import com.nom.graalnom.runtime.NomContext;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.strings.TruffleString;

@TruffleLanguage.Registration(id = NomLanguage.ID, name = "GraalNom",
        defaultMimeType = NomLanguage.MIME_TYPE,
        characterMimeTypes = NomLanguage.MIME_TYPE,
        contextPolicy = TruffleLanguage.ContextPolicy.SHARED,
        fileTypeDetectors = NomFileDetector.class)
@ProvidedTags({StandardTags.CallTag.class, StandardTags.StatementTag.class, StandardTags.RootTag.class, StandardTags.RootBodyTag.class, StandardTags.ExpressionTag.class, DebuggerTags.AlwaysHalt.class,
        StandardTags.ReadVariableTag.class, StandardTags.WriteVariableTag.class})
public class NomLanguage extends TruffleLanguage<NomContext> {
    public static final TruffleString.Encoding STRING_ENCODING = TruffleString.Encoding.UTF_16LE;
    public static final String ID = "nom";
    public static final String MIME_TYPE = "application/xml";

    @Override
    protected NomContext createContext(Env env) {
        return new NomContext(this, env);
    }

    @Override
    protected CallTarget parse(ParsingRequest request) {
        return null;
    }
}
