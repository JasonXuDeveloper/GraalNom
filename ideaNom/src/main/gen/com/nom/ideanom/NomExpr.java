// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomExpr extends PsiElement {

  @NotNull
  NomExprNoRecur getExprNoRecur();

  @NotNull
  List<NomExprPrime> getExprPrimeList();

}
