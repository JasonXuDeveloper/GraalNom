package com.nom.graalnom.runtime.nodes.controlflow;

import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import java.util.Arrays;
import java.util.Collection;

public final class NomBranchNode extends NomEndOfBasicBlockNode {
    private int successor;
    @Children
    public NomStatementNode[] mappings;

    public NomBranchNode(NomStatementNode[] mappings, int successor) {
        this.mappings = mappings;
        this.successor = successor;
    }

    public int getSuccessor() {
        return successor;
    }
    public void setSuccessor(int successor) {
        this.successor = successor;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {

    }

    @Override
    public String toString() {
        if (mappings == null || mappings.length == 0) {
            return "jmp blk " + successor;
        }
        return String.join("\n\t",
                Arrays.stream(mappings).map(Object::toString)
                        .toArray(String[]::new)) + "\n\tjmp blk " + successor;
    }
}
