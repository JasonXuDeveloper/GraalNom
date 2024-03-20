package com.nom.graalnom.runtime.nodes.controlflow;

import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.CountingConditionProfile;

@NodeInfo(shortName = "if", description = "The node implementing a conditonal statement")
public final class NomIfNode extends NomStatementNode {

    /**
     * The condition of the {@code if}. This in a {@link NomExpressionNode} because we require a
     * result value. We do not have a node type that can only return a {@code boolean} value, so
     * {@link #evaluateCondition executing the condition} can lead to a type error.
     */
    @Node.Child
    private NomExpressionNode conditionNode;

    /**
     * Statement (or {@link NomBlockNode block}) executed when the condition is true.
     */
    @Node.Child
    private NomBlockNode thenPartNode;

    /**
     * Statement (or {@link NomBlockNode block}) executed when the condition is false.
     */
    @Node.Child
    private NomBlockNode elsePartNode;

    /**
     * Profiling information, collected by the interpreter, capturing the profiling information of
     * the condition. This allows the compiler to generate better code for conditions that are
     * always true or always false. Additionally the {@link CountingConditionProfile} implementation
     * (as opposed to {@link BinaryConditionProfile} implementation) transmits the probability of
     * the condition to be true to the compiler.
     */
    private final CountingConditionProfile condition = CountingConditionProfile.create();

    public NomIfNode(NomExpressionNode conditionNode, NomBlockNode thenPartNode, NomBlockNode elsePartNode) {
        this.conditionNode = conditionNode;
        this.thenPartNode = thenPartNode;
        this.elsePartNode = elsePartNode;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        /*
         * In the interpreter, record profiling information that the condition was executed and with
         * which outcome.
         */
        if (condition.profile(evaluateCondition(frame))) {
            /* Execute the then-branch. */
            thenPartNode.executeVoid(frame);
        } else {
            /* Execute the else-branch (which is optional according to the SL syntax). */
            if (elsePartNode != null) {
                elsePartNode.executeVoid(frame);
            }
        }
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

    @Override
    public String toString() {
        String thenStr = thenPartNode.toString();
        //split with new line and add a tab
        thenStr = "\t" + thenStr.replaceAll("\n", "\n\t");
        String elseStr = elsePartNode.toString();
        //split with new line and add a tab
        elseStr = "\t" + elseStr.replaceAll("\n", "\n\t");
        return "if(" + conditionNode + "){\n\t" + thenStr +
                "\n\telse{\n\t" + elseStr + "\n\t}";
    }
}
