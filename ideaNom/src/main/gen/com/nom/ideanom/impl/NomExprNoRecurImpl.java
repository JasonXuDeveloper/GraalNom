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

public class NomExprNoRecurImpl extends ASTWrapperPsiElement implements NomExprNoRecur {

  public NomExprNoRecurImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NomVisitor visitor) {
    visitor.visitExprNoRecur(this);
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
  @Nullable
  public NomCtype getCtype() {
    return findChildByClass(NomCtype.class);
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
  public NomLambdabody getLambdabody() {
    return findChildByClass(NomLambdabody.class);
  }

  @Override
  @NotNull
  public List<NomMethdef> getMethdefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomMethdef.class);
  }

  @Override
  @Nullable
  public NomRangeexpr getRangeexpr() {
    return findChildByClass(NomRangeexpr.class);
  }

  @Override
  @Nullable
  public NomRefqname getRefqname() {
    return findChildByClass(NomRefqname.class);
  }

  @Override
  @Nullable
  public NomStructassignments getStructassignments() {
    return findChildByClass(NomStructassignments.class);
  }

  @Override
  @Nullable
  public NomStructfielddecl getStructfielddecl() {
    return findChildByClass(NomStructfielddecl.class);
  }

  @Override
  @Nullable
  public NomStructfielddecls getStructfielddecls() {
    return findChildByClass(NomStructfielddecls.class);
  }

  @Override
  @NotNull
  public List<NomType> getTypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NomType.class);
  }

  @Override
  @Nullable
  public PsiElement getFloat() {
    return findChildByType(FLOAT);
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
