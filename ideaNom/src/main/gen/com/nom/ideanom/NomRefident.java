// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomRefident extends PsiElement {

  @NotNull
  NomIdent getIdent();

  @NotNull
  List<NomType> getTypeList();

}
