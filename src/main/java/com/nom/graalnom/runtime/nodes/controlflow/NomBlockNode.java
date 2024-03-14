package com.nom.graalnom.runtime.nodes.controlflow;

import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.*;

/**
 * A {@link NomStatementNode} that contains a block of other nodes to execute
 */
@NodeInfo(shortName = "block", description = "The node implementing a block of instructions")
public final class NomBlockNode extends NomStatementNode implements BlockNode.ElementExecutor<NomStatementNode> {
    /**
     * The block of child nodes. Using the block node allows Truffle to split the block into
     * multiple groups for compilation if the method is too big. This is an optional API.
     * Alternatively, you may just use your own block node, with a
     * {@link com.oracle.truffle.api.nodes.Node.Children @Children} field. However, this prevents
     * Truffle from compiling big methods, so these methods might fail to compile with a compilation
     * bailout.
     */
    @Child private BlockNode<NomStatementNode> block;

    public NomBlockNode(NomStatementNode[] bodyNodes) {
        this.block = bodyNodes.length > 0 ? BlockNode.create(bodyNodes, this) : null;
    }

    /**
     * Execute all block statements. The block node makes sure that {@link ExplodeLoop full
     * unrolling} of the loop is triggered during compilation. This allows the
     * {@link NomStatementNode#executeVoid} method of all children to be inlined.
     */
    @Override
    public void executeVoid(VirtualFrame frame) {
        if (this.block != null) {
            this.block.executeVoid(frame, BlockNode.NO_ARGUMENT);
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
        System.out.println(node.getClass());
        node.executeVoid(frame);
    }
}
