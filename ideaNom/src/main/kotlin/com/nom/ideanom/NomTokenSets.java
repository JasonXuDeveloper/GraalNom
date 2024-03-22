package com.nom.ideanom;

import com.intellij.psi.tree.TokenSet;

public interface NomTokenSets {
    TokenSet IDENTIFIERS = TokenSet.create(NomTypes.IDENT);

    TokenSet COMMENTS = TokenSet.create(NomTypes.COMMENT);
}
