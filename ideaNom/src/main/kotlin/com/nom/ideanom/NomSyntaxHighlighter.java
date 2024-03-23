package com.nom.ideanom;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.tree.IElementType;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class NomSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey OPERATOR =
            createTextAttributesKey("Operator", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey KEYWORDS =
            createTextAttributesKey("Keywords", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey IDENTIFIER =
            createTextAttributesKey("Identifier", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey COMMA =
            createTextAttributesKey("Comma", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey ClassName =
            createTextAttributesKey("ClassName", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey TypeSource =
            createTextAttributesKey("TypeSource", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey MethodName =
            createTextAttributesKey("MethodName", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    public static final TextAttributesKey InvokeFunction =
            createTextAttributesKey("InvokeFunction", DefaultLanguageHighlighterColors.CONSTANT);
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("Comment", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey STRING =
            createTextAttributesKey("String", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBERS =
            createTextAttributesKey("Number", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("Bad Character", HighlighterColors.BAD_CHARACTER);


    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] IDENTIFIER_KEYS = new TextAttributesKey[]{IDENTIFIER};
    private static final TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[]{COMMA};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] KEYWORDS_KEYS = new TextAttributesKey[]{KEYWORDS};
    private static final TextAttributesKey[] NUMBERS_KEYS = new TextAttributesKey[]{NUMBERS};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new NomLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(NomTypes.COMMENT)) {
            return COMMENT_KEYS;
        }
        if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        }
        if (tokenType.equals(NomTypes.IDENT)) {
            return IDENTIFIER_KEYS;
        }
        if (tokenType.equals(NomTypes.COMMA)) {
            return COMMA_KEYS;
        }
        if (tokenType.equals(NomTypes.STRING)) {
            return STRING_KEYS;
        }
        if (tokenType.equals(NomTypes.INT) ||
                tokenType.equals(NomTypes.FLOAT)
        ) {
            return NUMBERS_KEYS;
        }
        if (tokenType.equals(NomTypes.PUBLIC) ||
                tokenType.equals(NomTypes.PRIVATE) ||
                tokenType.equals(NomTypes.PROTECTED) ||
                tokenType.equals(NomTypes.INTERNAL) ||
                tokenType.equals(NomTypes.INTERNAL_PROT) ||
                tokenType.equals(NomTypes.STATIC) ||
                tokenType.equals(NomTypes.ABSTRACT) ||
                tokenType.equals(NomTypes.PARTIAL) ||
                tokenType.equals(NomTypes.EXPANDO) ||
                tokenType.equals(NomTypes.FINAL) ||
                tokenType.equals(NomTypes.VIRTUAL) ||
                tokenType.equals(NomTypes.READONLY) ||
                tokenType.equals(NomTypes.OVERRIDE) ||
                tokenType.equals(NomTypes.INTERFACE) ||
                tokenType.equals(NomTypes.CLASS) ||
                tokenType.equals(NomTypes.NUL) ||
                tokenType.equals(NomTypes.FUN) ||
                tokenType.equals(NomTypes.RETURN) ||
                tokenType.equals(NomTypes.IFNULL) ||
                tokenType.equals(NomTypes.IFOBJ) ||
                tokenType.equals(NomTypes.IF) ||
                tokenType.equals(NomTypes.ELSE) ||
                tokenType.equals(NomTypes.ELSEIF) ||
                tokenType.equals(NomTypes.WHILE) ||
                tokenType.equals(NomTypes.FOR) ||
                tokenType.equals(NomTypes.FOREACH) ||
                tokenType.equals(NomTypes.BREAK) ||
                tokenType.equals(NomTypes.CONTINUE) ||
                tokenType.equals(NomTypes.SUPER) ||
                tokenType.equals(NomTypes.CONSTRUCT) ||
                tokenType.equals(NomTypes.NEW) ||
                tokenType.equals(NomTypes.LET) ||
                tokenType.equals(NomTypes.LETVAR) ||
                tokenType.equals(NomTypes.TRUE) ||
                tokenType.equals(NomTypes.FALSE)
        ) {
            return KEYWORDS_KEYS;
        }

        return EMPTY_KEYS;
    }
}
