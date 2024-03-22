package com.nom.graalnom.runtime.nodes.controlflow;

import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.CountingConditionProfile;

@NodeInfo(shortName = "if", description = "The node implementing a conditional statement")
public final class NomIfNode extends NomEndOfBasicBlockNode {
    @Child
    public NomBranchNode trueBranch;
    @Child
    public NomBranchNode falseBranch;
    @Child
    public NomExpressionNode conditionNode;

    private final CountingConditionProfile condition = CountingConditionProfile.create();

    public NomIfNode(NomExpressionNode conditionNode, NomBranchNode trueBranch, NomBranchNode falseBranch) {
        this.conditionNode = conditionNode;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    public int getTrueSuccessor() {
        return trueBranch.getSuccessor();
    }

    public int getFalseSuccessor() {
        return falseBranch.getSuccessor();
    }

    public NomExpressionNode getConditionNode() {
        return conditionNode;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {

    }

    @Override
    public String toString() {
        String thenStr = trueBranch.toString();
        //split with new line and add a tab
        thenStr = "\t" + thenStr.replaceAll("\n", "\n\t");
        String elseStr = falseBranch.toString();
        //split with new line and add a tab
        elseStr = "\t" + elseStr.replaceAll("\n", "\n\t");
        return "if(" + conditionNode + "){\n\t" + thenStr +
                "\n\telse{\n\t" + elseStr + "\n\t}";
    }

    public boolean cond(VirtualFrame frame) {
        return condition.profile(evaluateCondition(frame));
    }

    private boolean evaluateCondition(VirtualFrame frame) {
        try {
            /*
             * The condition must evaluate to a boolean value, so we call the boolean-specialized
             * execute method.
             */
            return conditionNode.executeBoolean(frame);
        } catch (UnexpectedResultException ex) {
            /*
             * The condition evaluated to a non-boolean result. This is a type error in the SL
             * program.
             */
            throw new RuntimeException("Condition did not evaluate to a boolean value");
        }
    }
}
