// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomFielddecl extends PsiElement {

  @Nullable
  NomDtype getDtype();

  @Nullable
  NomExpr getExpr();

  @NotNull
  NomIdent getIdent();

  @Nullable
  NomVisibility getVisibility();

}
