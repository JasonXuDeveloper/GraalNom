package com.nom.graalnom.runtime.nodes.controlflow;

import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.*;

import java.util.Arrays;

/**
 * A {@link NomStatementNode} that contains a basic block of other nodes to execute
 */
@NodeInfo(shortName = "block", description = "The node implementing a block of instructions")
public final class NomBasicBlockNode extends NomStatementNode implements BlockNode.ElementExecutor<NomStatementNode> {
    /**
     * The block of child nodes. Using the block node allows Truffle to split the block into
     * multiple groups for compilation if the method is too big. This is an optional API.
     * Alternatively, you may just use your own block node, with a
     * {@link com.oracle.truffle.api.nodes.Node.Children @Children} field. However, this prevents
     * Truffle from compiling big methods, so these methods might fail to compile with a compilation
     * bailout.
     */
    @Child
    private BlockNode<NomStatementNode> block;
    public NomStatementNode[] bodyNodes;

    public int bodyNodeCount() {
        return bodyNodes.length;
    }
    public String blockName;

    public NomBasicBlockNode(NomStatementNode[] bodyNodes, String blockName) {
        this.bodyNodes = bodyNodes;
        this.block = this.bodyNodes.length > 0 ? BlockNode.create(bodyNodes, this) : null;
        this.blockName = blockName;
    }

    public void append(NomStatementNode[] bodyNodes) {
        if (this.block == null) {
            this.bodyNodes = bodyNodes;
            this.block = this.bodyNodes.length > 0 ? BlockNode.create(bodyNodes, this) : null;
        } else {
            NomStatementNode[] nodes = new NomStatementNode[this.bodyNodes.length + bodyNodes.length];
            System.arraycopy(this.bodyNodes, 0, nodes, 0, this.bodyNodes.length);
            System.arraycopy(bodyNodes, 0, nodes, this.bodyNodes.length, bodyNodes.length);
            this.bodyNodes = nodes;
            this.block = this.bodyNodes.length > 0 ? BlockNode.create(bodyNodes, this) : null;
        }
    }

    public NomStatementNode getTerminatingNode() {
        if (this.bodyNodes.length == 0) {
            return null;
        }
        return this.bodyNodes[this.bodyNodes.length - 1];
    }

    /**
     * Execute all block statements. The block node makes sure that {@link ExplodeLoop full
     * unrolling} of the loop is triggered during compilation. This allows the
     * {@link NomStatementNode#executeVoid} method of all children to be inlined.
     */
    @Override
    public void executeVoid(VirtualFrame frame) {
        if (this.block != null) {
            this.block.executeVoid(frame, frame.getArguments().length);
        }
    }

    /**
     * Truffle nodes don't have a fixed execute signature. The {@link BlockNode.ElementExecutor} interface
     * tells the framework how block element nodes should be executed. The executor allows to add a
     * custom exception handler for each element, e.g. to handle a specific
     * {@link ControlFlowException} or to pass a customizable argument, that allows implement
     * startsWith semantics if needed.
     */
    @Override
    public void executeVoid(VirtualFrame frame, NomStatementNode node, int index, int argument) {
        node.executeVoid(frame);
    }

    public String toString() {
        String blkStr = "=== "+blockName+" ===\n\t";
        if (block != null) {
            blkStr += String.join("\n\t",
                    Arrays.stream(bodyNodes).map(Object::toString)
                            .toArray(String[]::new));
        }
        return blkStr;
    }
}
