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

public class NomStmtImpl extends ASTWrapperPsiElement implements NomStmt {

  public NomStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NomVisitor visitor) {
    visitor.visitStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof NomVisitor) accept((NomVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public NomArgdecl getArgdecl() {
    return findChildByClass(NomArgdecl.class);
  }

  @Override
  @NotNull
  public List<NomBlock> getBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomBlock.class);
  }

  @Override
  @Nullable
  public NomDtype getDtype() {
    return findChildByClass(NomDtype.class);
  }

  @Override
  @NotNull
  public List<NomExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomExpr.class);
  }

  @Override
  @Nullable
  public NomIdent getIdent() {
    return findChildByClass(NomIdent.class);
  }

  @Override
  @Nullable
  public NomType getType() {
    return findChildByClass(NomType.class);
  }

  @Override
  @Nullable
  public PsiElement getInt() {
    return findChildByType(INT);
  }

  @Override
  @Nullable
  public PsiElement getString() {
    return findChildByType(STRING);
  }

}
