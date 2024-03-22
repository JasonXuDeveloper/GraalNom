// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomExprPrime extends PsiElement {

  @Nullable
  NomAddsubop getAddsubop();

  @Nullable
  NomDivmultop getDivmultop();

  @NotNull
  List<NomExpr> getExprList();

  @Nullable
  NomRefident getRefident();

}
