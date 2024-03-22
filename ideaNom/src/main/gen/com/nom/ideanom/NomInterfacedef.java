// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomInterfacedef extends PsiElement {

  @NotNull
  NomDeclident getDeclident();

  @NotNull
  List<NomInheritancedecl> getInheritancedeclList();

  @NotNull
  List<NomInterfacedef> getInterfacedefList();

  @NotNull
  List<NomMethdecl> getMethdeclList();

  @NotNull
  List<NomVisibility> getVisibilityList();

}
