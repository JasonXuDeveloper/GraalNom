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

public class NomStaticfielddeclImpl extends ASTWrapperPsiElement implements NomStaticfielddecl {

  public NomStaticfielddeclImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NomVisitor visitor) {
    visitor.visitStaticfielddecl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof NomVisitor) accept((NomVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public NomDefaultexpr getDefaultexpr() {
    return findChildByClass(NomDefaultexpr.class);
  }

  @Override
  @Nullable
  public NomDtype getDtype() {
    return findChildByClass(NomDtype.class);
  }

  @Override
  @NotNull
  public NomIdent getIdent() {
    return findNotNullChildByClass(NomIdent.class);
  }

  @Override
  @Nullable
  public NomVisibility getVisibility() {
    return findChildByClass(NomVisibility.class);
  }

}
