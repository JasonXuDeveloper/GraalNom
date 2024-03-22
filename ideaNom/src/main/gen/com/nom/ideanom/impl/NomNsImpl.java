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

public class NomNsImpl extends ASTWrapperPsiElement implements NomNs {

  public NomNsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NomVisitor visitor) {
    visitor.visitNs(this);
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
  public NomDeclqname getDeclqname() {
    return findNotNullChildByClass(NomDeclqname.class);
  }

  @Override
  @NotNull
  public List<NomInterfacedef> getInterfacedefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomInterfacedef.class);
  }

  @Override
  @NotNull
  public List<NomNs> getNsList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomNs.class);
  }

}
