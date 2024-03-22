// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NomClassdef extends PsiElement {

  @NotNull
  List<NomClassdef> getClassdefList();

  @NotNull
  List<NomConstructor> getConstructorList();

  @NotNull
  List<NomFielddecl> getFielddeclList();

  @NotNull
  List<NomInheritancedecl> getInheritancedeclList();

  @NotNull
  List<NomInstancedecl> getInstancedeclList();

  @NotNull
  List<NomInterfacedef> getInterfacedefList();

  @NotNull
  List<NomMethdef> getMethdefList();

  @NotNull
  List<NomStaticfielddecl> getStaticfielddeclList();

  @NotNull
  List<NomStaticmethdef> getStaticmethdefList();

  @NotNull
  List<NomVisibility> getVisibilityList();

  @NotNull
  PsiElement getId();

}
