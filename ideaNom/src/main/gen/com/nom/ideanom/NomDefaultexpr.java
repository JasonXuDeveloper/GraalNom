// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomDefaultexpr extends PsiElement {

  @Nullable
  NomCtype getCtype();

  @NotNull
  List<NomDefaultexpr> getDefaultexprList();

  @Nullable
  NomIdent getIdent();

  @Nullable
  PsiElement getFloat();

  @Nullable
  PsiElement getInt();

  @Nullable
  PsiElement getString();

}
