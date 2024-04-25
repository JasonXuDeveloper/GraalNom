package com.nom.graalnom.runtime.nodes.controlflow;

import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.local.NomReadRegisterNode;
import com.nom.graalnom.runtime.nodes.local.NomWriteRegisterNode;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.*;

import java.util.Arrays;

/**
 * A {@link NomStatementNode} that contains a basic block of other nodes to execute
 */
@NodeInfo(shortName = "block", description = "The node implementing a block of instructions")
public final class NomBasicBlockNode extends NomStatementNode {
    @Children
    public NomStatementNode[] bodyNodes;

    public int bodyNodeCount() {
        return bodyNodes.length;
    }

    public String blockName;

    public NomBasicBlockNode(NomStatementNode[] bodyNodes, String blockName) {
        this.bodyNodes = bodyNodes;
        this.blockName = blockName;
    }

    public NomEndOfBasicBlockNode getTerminatingNode() {
        if (this.bodyNodes.length == 0) {
            return null;
        }
        return (NomEndOfBasicBlockNode) this.bodyNodes[this.bodyNodes.length - 1];
    }

    /**
     * Execute all block statements. The block node makes sure that {@link ExplodeLoop full
     * unrolling} of the loop is triggered during compilation. This allows the
     * {@link NomStatementNode#executeVoid} method of all children to be inlined.
     */
    @Override
    @ExplodeLoop(kind = ExplodeLoop.LoopExplosionKind.FULL_UNROLL)
    public void executeVoid(VirtualFrame frame) {
        if (this.bodyNodes != null) {
            int index = 0;
            NomStatementNode cur = bodyNodes[index];
            while (!(cur instanceof NomEndOfBasicBlockNode)){
                cur.executeVoid(frame);
                cur = bodyNodes[++index];
            }
        }
    }


    public String toString() {
        String blkStr = "=== " + blockName + " ===\n\t";
        if (bodyNodes != null) {
            blkStr += String.join("\n\t",
                    Arrays.stream(bodyNodes).map(Object::toString)
                            .toArray(String[]::new));
        }
        return blkStr;
    }

    public void mergeEndOfBlock() {
        if (this.bodyNodes.length < 2) {
            return;
        }
        NomStatementNode secondLastNode = this.bodyNodes[this.bodyNodes.length - 2];
        boolean merged = false;
        if (secondLastNode instanceof NomWriteRegisterNode writeRegisterNode) {
            int regIdx = writeRegisterNode.getRegIndex();
            NomExpressionNode valueNode = writeRegisterNode.getValueNode();
            NomStatementNode lastNode = this.bodyNodes[this.bodyNodes.length - 1];
            if (lastNode instanceof NomReturnNode retNode) {
                if (retNode.valueNode instanceof NomReadRegisterNode readRegisterNode) {
                    if (readRegisterNode.getRegIndex() == regIdx) {
                        retNode.valueNode = valueNode;
                        merged = true;
                    }
                }
            } else if (lastNode instanceof NomIfNode ifNode) {
                if (ifNode.conditionNode instanceof NomReadRegisterNode readRegisterNode) {
                    if (readRegisterNode.getRegIndex() == regIdx) {
                        ifNode.conditionNode = valueNode;
                        merged = true;
                    }
                }
            }
        }
        if (merged) {
            this.bodyNodes[this.bodyNodes.length - 2] = this.bodyNodes[this.bodyNodes.length - 1];
            this.bodyNodes = Arrays.copyOf(this.bodyNodes, this.bodyNodes.length - 1);
        }
    }
}
