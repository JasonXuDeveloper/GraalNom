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

public class NomMethdeclImpl extends ASTWrapperPsiElement implements NomMethdecl {

  public NomMethdeclImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NomVisitor visitor) {
    visitor.visitMethdecl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof NomVisitor) accept((NomVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<NomArgdecl> getArgdeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomArgdecl.class);
  }

  @Override
  @NotNull
  public NomDeclident getDeclident() {
    return findNotNullChildByClass(NomDeclident.class);
  }

  @Override
  @Nullable
  public NomType getType() {
    return findChildByClass(NomType.class);
  }

  @Override
  @Nullable
  public NomVisibility getVisibility() {
    return findChildByClass(NomVisibility.class);
  }

}
