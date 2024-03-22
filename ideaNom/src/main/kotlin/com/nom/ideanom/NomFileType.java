package com.nom.ideanom;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NomFileType extends LanguageFileType {
    public static final NomFileType INSTANCE = new NomFileType();

    private NomFileType() {
        super(NomLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Nom";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Nom language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "mn";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }
}
