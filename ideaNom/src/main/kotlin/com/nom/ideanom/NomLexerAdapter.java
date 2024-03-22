package com.nom.ideanom;

import com.intellij.lexer.FlexAdapter;

public class NomLexerAdapter extends FlexAdapter {
    public NomLexerAdapter() {
        super(new _NomLexer(null));
    }
}
