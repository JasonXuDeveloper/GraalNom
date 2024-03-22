// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomMethdef extends PsiElement {

  @NotNull
  List<NomArgdecl> getArgdeclList();

  @NotNull
  NomBlock getBlock();

  @NotNull
  NomDeclident getDeclident();

  @Nullable
  NomDtype getDtype();

  @NotNull
  List<NomVisibility> getVisibilityList();

}
