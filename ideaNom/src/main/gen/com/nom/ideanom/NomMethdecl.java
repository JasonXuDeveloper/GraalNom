// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomMethdecl extends PsiElement {

  @NotNull
  List<NomArgdecl> getArgdeclList();

  @NotNull
  NomDeclident getDeclident();

  @Nullable
  NomType getType();

  @Nullable
  NomVisibility getVisibility();

}
