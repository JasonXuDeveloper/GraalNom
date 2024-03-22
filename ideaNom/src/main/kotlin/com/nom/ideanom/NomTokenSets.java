package com.nom.ideanom;

import com.intellij.psi.tree.TokenSet;

public interface NomTokenSets {
    TokenSet IDENTIFIERS = TokenSet.create(NomTypes.KEY);

    TokenSet COMMENTS = TokenSet.create(NomTypes.COMMENT);
}
