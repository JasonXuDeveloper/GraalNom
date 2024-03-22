package com.nom.ideanom;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

final class NomColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keywords", NomSyntaxHighlighter.KEYWORDS),
            new AttributesDescriptor("Operator", NomSyntaxHighlighter.OPERATOR),
            new AttributesDescriptor("Identifier", NomSyntaxHighlighter.IDENTIFIER),
            new AttributesDescriptor("Comma", NomSyntaxHighlighter.COMMA),
            new AttributesDescriptor("ClassName", NomSyntaxHighlighter.ClassName),
            new AttributesDescriptor("Comment", NomSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Constant", NomSyntaxHighlighter.CONSTANTS),
            new AttributesDescriptor("String", NomSyntaxHighlighter.STRING),
            new AttributesDescriptor("Bad value", NomSyntaxHighlighter.BAD_CHARACTER)
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new NomSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "class HelloWorld\n" +
                "{\n" +
                "    public static fun Main() : Void\n" +
                "    {\n" +
                "        \"Hello World\".Print();\n" +
                "    }\n" +
                "}";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Nom";
    }
}
