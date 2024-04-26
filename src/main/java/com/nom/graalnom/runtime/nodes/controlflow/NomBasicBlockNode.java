package com.nom.graalnom.runtime.nodes.controlflow;

import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.NomInvokeNode;
import com.nom.graalnom.runtime.nodes.local.NomReadRegisterNode;
import com.nom.graalnom.runtime.nodes.local.NomWriteRegisterNode;
import com.oracle.truffle.api.dsl.GenerateInline;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.*;

import java.util.Arrays;
import java.util.HashSet;

/**
 * A {@link NomStatementNode} that contains a basic block of other nodes to execute
 */
@NodeInfo(shortName = "block", description = "The node implementing a block of instructions")
@GenerateInline
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
            while (!(cur instanceof NomEndOfBasicBlockNode)) {
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


    public void optimiseFCP() {
        if (this.bodyNodes.length < 2) {
            return;
        }

        for (int i = 0; i < this.bodyNodes.length - 2; i++) {
            NomStatementNode curNode = bodyNodes[i];
            if (!(curNode instanceof NomWriteRegisterNode writeRegisterNode)) continue;
            int regIndex = writeRegisterNode.getRegIndex();
            NomStatementNode nextNode = bodyNodes[i + 1];
            if (!(nextNode instanceof NomInvokeNode<?> invokeNode)) continue;
            NomStatementNode nextNextNode = bodyNodes[i + 2];
            if (!(nextNextNode instanceof NomReturnNode)) continue;
            int cnt = 0;
            for (NomExpressionNode expr : invokeNode.argumentNodes) {
                if (expr instanceof NomReadRegisterNode readRegisterNode && readRegisterNode.getRegIndex() == regIndex) {
                    cnt++;
                }
            }
            if (cnt != 1) continue;
            NomExpressionNode[] argumentNodes = invokeNode.argumentNodes;
            for (int j = 0, argumentNodesLength = argumentNodes.length; j < argumentNodesLength; j++) {
                NomExpressionNode expr = argumentNodes[j];
                if (expr instanceof NomReadRegisterNode readRegisterNode && readRegisterNode.getRegIndex() == regIndex) {
                    invokeNode.argumentNodes[j] = writeRegisterNode.getValueNode();
                }
            }

            for(int j = i;j<bodyNodes.length - 1;j++){
                NomStatementNode temp = this.bodyNodes[j+1];
                this.bodyNodes[j + 1] = this.bodyNodes[j];
                this.bodyNodes[j] = temp;
            }

            this.bodyNodes = Arrays.copyOf(this.bodyNodes, this.bodyNodes.length - 1);
        }
    }

    public void optimiseTail() {
        if (this.bodyNodes.length < 2) {
            return;
        }
        NomStatementNode secondLastNode = this.bodyNodes[this.bodyNodes.length - 2];
        NomStatementNode lastNode = getTerminatingNode();
        if (lastNode instanceof NomReturnNode retNode && retNode.valueNode instanceof NomInvokeNode<?> retTailNode) {
            if (secondLastNode instanceof NomWriteRegisterNode writeRegisterNode) {
                if (writeRegisterNode.getValueNode() instanceof NomInvokeNode<?> invokeNode) {
                    int regIndex = writeRegisterNode.getRegIndex();
                    int len = retTailNode.argumentNodes.length;
                    int cnt = 0;
                    for (int i = 0; i < len; i++) {
                        if (retTailNode.argumentNodes[i] instanceof NomReadRegisterNode readRegisterNode &&
                                readRegisterNode.getRegIndex() == regIndex) {
                            cnt++;
                        }
                    }
                    //only replace 1
                    if (cnt == 1) {
                        for (int i = 0; i < len; i++) {
                            if (retTailNode.argumentNodes[i] instanceof NomReadRegisterNode readRegisterNode &&
                                    readRegisterNode.getRegIndex() == regIndex) {
                                retTailNode.argumentNodes[i] = invokeNode;
                            }
                        }
                        this.bodyNodes[this.bodyNodes.length - 2] = this.bodyNodes[this.bodyNodes.length - 1];
                        this.bodyNodes = Arrays.copyOf(this.bodyNodes, this.bodyNodes.length - 1);
                    }
                }
            }
        }
    }
}
