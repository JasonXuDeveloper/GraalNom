// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomInstancedecl extends PsiElement {

  @NotNull
  List<NomArgdecl> getArgdeclList();

  @NotNull
  List<NomDefaultexpr> getDefaultexprList();

  @NotNull
  List<NomIdent> getIdentList();

  @NotNull
  List<NomInstanceassignment> getInstanceassignmentList();

  @Nullable
  NomVisibility getVisibility();

}
