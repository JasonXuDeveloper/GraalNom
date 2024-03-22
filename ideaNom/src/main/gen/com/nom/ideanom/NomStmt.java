// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomStmt extends PsiElement {

  @Nullable
  NomArgdecl getArgdecl();

  @NotNull
  List<NomBlock> getBlockList();

  @Nullable
  NomDtype getDtype();

  @NotNull
  List<NomExpr> getExprList();

  @Nullable
  NomIdent getIdent();

  @Nullable
  NomType getType();

  @Nullable
  PsiElement getInt();

  @Nullable
  PsiElement getString();

}
