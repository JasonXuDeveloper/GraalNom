package com.nom.ideanom;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class NomFile extends PsiFileBase {
    public NomFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, NomLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return NomFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Nom";
    }
}
