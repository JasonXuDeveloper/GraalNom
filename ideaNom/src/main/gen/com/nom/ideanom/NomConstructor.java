// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomConstructor extends PsiElement {

  @NotNull
  List<NomArgdecl> getArgdeclList();

  @NotNull
  List<NomExpr> getExprList();

  @NotNull
  List<NomStmt> getStmtList();

  @Nullable
  NomVisibility getVisibility();

}
