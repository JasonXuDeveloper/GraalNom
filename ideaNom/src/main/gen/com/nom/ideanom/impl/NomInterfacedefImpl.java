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

public class NomInterfacedefImpl extends ASTWrapperPsiElement implements NomInterfacedef {

  public NomInterfacedefImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NomVisitor visitor) {
    visitor.visitInterfacedef(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof NomVisitor) accept((NomVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public NomDeclident getDeclident() {
    return findNotNullChildByClass(NomDeclident.class);
  }

  @Override
  @NotNull
  public List<NomInheritancedecl> getInheritancedeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomInheritancedecl.class);
  }

  @Override
  @NotNull
  public List<NomInterfacedef> getInterfacedefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomInterfacedef.class);
  }

  @Override
  @NotNull
  public List<NomMethdecl> getMethdeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomMethdecl.class);
  }

  @Override
  @NotNull
  public List<NomVisibility> getVisibilityList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomVisibility.class);
  }

}
