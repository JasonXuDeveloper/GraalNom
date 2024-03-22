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

public class NomExprPrimeImpl extends ASTWrapperPsiElement implements NomExprPrime {

  public NomExprPrimeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NomVisitor visitor) {
    visitor.visitExprPrime(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof NomVisitor) accept((NomVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public NomAddsubop getAddsubop() {
    return findChildByClass(NomAddsubop.class);
  }

  @Override
  @Nullable
  public NomDivmultop getDivmultop() {
    return findChildByClass(NomDivmultop.class);
  }

  @Override
  @NotNull
  public List<NomExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomExpr.class);
  }

  @Override
  @Nullable
  public NomRefident getRefident() {
    return findChildByClass(NomRefident.class);
  }

}
