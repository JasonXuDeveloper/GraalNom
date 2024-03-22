// This is a generated file. Not intended for manual editing.
package com.nom.ideanom.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.nom.ideanom.NomTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.nom.ideanom.*;

public class NomClassdefImpl extends ASTWrapperPsiElement implements NomClassdef {

  public NomClassdefImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NomVisitor visitor) {
    visitor.visitClassdef(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof NomVisitor) accept((NomVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<NomClassdef> getClassdefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomClassdef.class);
  }

  @Override
  @NotNull
  public List<NomConstructor> getConstructorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomConstructor.class);
  }

  @Override
  @NotNull
  public List<NomFielddecl> getFielddeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomFielddecl.class);
  }

  @Override
  @NotNull
  public List<NomInheritancedecl> getInheritancedeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomInheritancedecl.class);
  }

  @Override
  @NotNull
  public List<NomInstancedecl> getInstancedeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomInstancedecl.class);
  }

  @Override
  @NotNull
  public List<NomInterfacedef> getInterfacedefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomInterfacedef.class);
  }

  @Override
  @NotNull
  public List<NomMethdef> getMethdefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomMethdef.class);
  }

  @Override
  @NotNull
  public List<NomStaticfielddecl> getStaticfielddeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomStaticfielddecl.class);
  }

  @Override
  @NotNull
  public List<NomStaticmethdef> getStaticmethdefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomStaticmethdef.class);
  }

  @Override
  @NotNull
  public List<NomVisibility> getVisibilityList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomVisibility.class);
  }

  @Override
  @NotNull
  public PsiElement getId() {
    return findNotNullChildByType(ID);
  }

}
