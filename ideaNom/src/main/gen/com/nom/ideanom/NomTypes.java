// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.nom.ideanom.impl.*;

public interface NomTypes {

  IElementType PROPERTY = new NomElementType("PROPERTY");

  IElementType COMMENT = new NomTokenType("COMMENT");
  IElementType CRLF = new NomTokenType("CRLF");
  IElementType KEY = new NomTokenType("KEY");
  IElementType SEPARATOR = new NomTokenType("SEPARATOR");
  IElementType VALUE = new NomTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == PROPERTY) {
        return new NomPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
