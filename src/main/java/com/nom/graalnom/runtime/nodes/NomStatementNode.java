package com.nom.graalnom.runtime.nodes;

import com.nom.graalnom.NomLanguage;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;


/**
 * Base class of all MonNom instruction nodes.
 * The {@link VirtualFrame} provides access to the local variables.
 */
@NodeInfo(language = NomLanguage.ID, description = "The abstract base node for all MonNom statements")
public abstract class NomStatementNode extends Node {
    /**
     * Execute this node as a statement, where no return value is necessary.
     */
    public abstract void executeVoid(VirtualFrame frame);
}
