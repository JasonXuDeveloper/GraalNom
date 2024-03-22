// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomExprNoRecur extends PsiElement {

  @Nullable
  NomArgdecl getArgdecl();

  @Nullable
  NomCtype getCtype();

  @Nullable
  NomDtype getDtype();

  @NotNull
  List<NomExpr> getExprList();

  @Nullable
  NomIdent getIdent();

  @Nullable
  NomLambdabody getLambdabody();

  @NotNull
  List<NomMethdef> getMethdefList();

  @Nullable
  NomRangeexpr getRangeexpr();

  @Nullable
  NomRefqname getRefqname();

  @Nullable
  NomStructassignments getStructassignments();

  @Nullable
  NomStructfielddecl getStructfielddecl();

  @Nullable
  NomStructfielddecls getStructfielddecls();

  @NotNull
  List<NomType> getTypeList();

  @Nullable
  PsiElement getFloat();

  @Nullable
  PsiElement getInt();

  @Nullable
  PsiElement getString();

}
