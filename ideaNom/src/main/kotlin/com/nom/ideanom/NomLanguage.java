package com.nom.ideanom;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class NomLanguage extends Language {
    public static final NomLanguage INSTANCE = new NomLanguage();

    private NomLanguage() {
        super("Nom");
    }
}
