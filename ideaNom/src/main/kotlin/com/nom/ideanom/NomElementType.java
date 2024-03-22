package com.nom.ideanom;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class NomElementType extends IElementType {
    public NomElementType(@NonNls @NotNull String debugName) {
        super(debugName, NomLanguage.INSTANCE);
    }
}
