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

public class NomDeclidentImpl extends ASTWrapperPsiElement implements NomDeclident {

  public NomDeclidentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NomVisitor visitor) {
    visitor.visitDeclident(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof NomVisitor) accept((NomVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public NomIdent getIdent() {
    return findNotNullChildByClass(NomIdent.class);
  }

  @Override
  @NotNull
  public List<NomTypeargspec> getTypeargspecList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomTypeargspec.class);
  }

}
