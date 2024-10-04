/*
 * Copyright (c) 2012, 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.nom.graalnom.runtime.nodes;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.builtins.NomBuiltinNode;
import com.nom.graalnom.runtime.nodes.expression.*;
import com.nom.graalnom.runtime.nodes.controlflow.*;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The root of all Nom execution trees. It is a Truffle requirement that the tree root extends the
 * class {@link RootNode}. This class is used for both builtin and user-defined functions. For
 * builtin functions, the {@link #bodyNode} is a subclass of {@link com.nom.graalnom.runtime.builtins.NomBuiltinNode}. For user-defined
 * functions, the {@link #bodyNode} is a {@link NomBasicBlockNode}.
 */
@NodeInfo(language = "Nom", description = "The root of all Nom execution trees")
public class NomRootNode extends RootNode {
    /**
     * The function body that is executed, and specialized during execution.
     */
    @Child
    private NomExpressionNode bodyNode;

    /**
     * The name of the function, for printing purposes only.
     */
    private final String name;

    private final int argCount;

    public NomRootNode(NomLanguage language, FrameDescriptor frameDescriptor, NomExpressionNode bodyNode, String name, int argCount) {
        super(language, frameDescriptor);
        this.bodyNode = bodyNode;
        this.name = name;
        this.argCount = argCount;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return bodyNode.executeGeneric(frame);
    }

    public NomExpressionNode getBodyNode() {
        return bodyNode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder arg = new StringBuilder("(");
        for (int i = 0; i < argCount; i++) {
            arg.append("arg").append(i);
            if (i < argCount - 1) {
                arg.append(", ");
            }
        }
        arg.append(")");
        return "func " + name + arg +
                ":\n{" + bodyNode.toString() + "}";
    }

    public String toDotGraph() {
        if (!(bodyNode instanceof NomFunctionBodyNode funcBodyNode)) {
            throw new RuntimeException("RootNode bodyNode is not a NomFunctionBodyNode");
        }

        StringBuilder dot = new StringBuilder("digraph {\n");
        dot.append("  rankdir=TB;\n");

        NomBasicBlockNode[] bodyNodes = funcBodyNode.bodyNodes;
        for (int i = 0; i < bodyNodes.length; i++) {
            NomBasicBlockNode basicBlockNode = bodyNodes[i];
            List<String> nodesInBlock = new ArrayList<>();
            List<String> nodeNamesInBlock = new ArrayList<>();
            StringBuilder nodeBuilder = new StringBuilder();
            // all instructions in the block
            nodeBuilder.append('b').append(i).append("_l0 [label=\"");
            NomStatementNode[] nodes = basicBlockNode.bodyNodes;
            for (int j = 0; j < nodes.length - 1; j++) {
                NomStatementNode node = nodes[j];
                nodeBuilder.append(node.toString()).append("\\").append("n");
            }
            nodeBuilder.append("\", shape=box]\n");

            boolean hasL0 = !nodeBuilder.toString().contains("label=\"\"");
            if (hasL0) {
                nodeNamesInBlock.add("b" + i + "_l0");
                nodesInBlock.add(nodeBuilder.toString());
            }

            //branch
            if (basicBlockNode.getTerminatingNode() instanceof NomBranchNode branchNode) {
                if (!hasL0) {
                    nodeBuilder = new StringBuilder();
                    nodeBuilder.append('b')
                            .append(i).append("_l0 [label=\"placeholder\", shape=box]\n");
                    nodesInBlock.add(nodeBuilder.toString());
                    nodeNamesInBlock.add("b" + i + "_l0");
                }
                //mappings
                if (branchNode.mappings != null && branchNode.mappings.length > 0) {
                    dot.append("  ")
                            .append(nodeNamesInBlock.getLast()).append(" -> b")
                            .append(i).append("_jmp\n");
                    nodeBuilder = new StringBuilder();
                    nodeBuilder.append('b')
                            .append(i).append("_jmp [label=\"");
                    for (NomStatementNode node : branchNode.mappings) {
                        nodeBuilder.append(node.toString()).append("\\").append("n");
                    }
                    nodeBuilder.append("\", shape=box]\n");
                    nodesInBlock.add(nodeBuilder.toString());
                    nodeNamesInBlock.add("b" + i + "_jmp");
                }

                //last node jumps
                dot.append("  ")
                        .append(nodeNamesInBlock.getLast()).append(" -> b")
                        .append(branchNode.getSuccessor()).append("_l0\n");
            }
            //condBranch
            else if (basicBlockNode.getTerminatingNode() instanceof NomIfNode ifNode) {
                //cond
                if (hasL0) {
                    dot.append("  ")
                            .append(nodeNamesInBlock.getLast()).append(" -> b")
                            .append(i).append("_cond\n");
                    dot.append("  b")
                            .append(i).append("_cond [label=\"")
                            .append(ifNode.conditionNode.toString()).append("\", shape=diamond]\n");
                    nodeNamesInBlock.add("b" + i + "_cond");
                } else {
                    dot.append("  b")
                            .append(i).append("_l0 [label=\"")
                            .append(ifNode.conditionNode.toString()).append("\", shape=diamond]\n");
                    nodeNamesInBlock.add("b" + i + "_l0");
                }
                //true branch
                NomBranchNode branchNode = ifNode.trueBranch;
                //mappings
                if (branchNode.mappings != null && branchNode.mappings.length > 0) {
                    dot.append("  ")
                            .append(nodeNamesInBlock.getLast()).append(" -> b")
                            .append(i).append("_true [label=\"true\"]\n");
                    nodeBuilder = new StringBuilder();
                    nodeBuilder.append('b')
                            .append(i).append("_true [label=\"");
                    for (NomStatementNode node : branchNode.mappings) {
                        nodeBuilder.append(node.toString()).append("\\").append("n");
                    }
                    nodeBuilder.append("\", shape=box]\n");
                    nodesInBlock.add(nodeBuilder.toString());
                    nodeNamesInBlock.add("b" + i + "_true");

                    //last node jumps
                    dot.append("  b")
                            .append(i).append("_true -> b")
                            .append(branchNode.getSuccessor()).append("_l0 \n");
                } else {
                    dot.append("  ")
                            .append(nodeNamesInBlock.getLast()).append(" -> b")
                            .append(branchNode.getSuccessor()).append("_l0 [label=\"true\"]\n");
                }

                //false branch
                branchNode = ifNode.falseBranch;
                //mappings
                if (branchNode.mappings != null && branchNode.mappings.length > 0) {
                    dot.append("  ")
                            .append(nodeNamesInBlock.getLast()).append(" -> b")
                            .append(i).append("_false [label=\"false\"]\n");
                    nodeBuilder = new StringBuilder();
                    nodeBuilder.append('b')
                            .append(i).append("_false [label=\"");
                    for (NomStatementNode node : branchNode.mappings) {
                        nodeBuilder.append(node.toString()).append("\\").append("n");
                    }
                    nodeBuilder.append("\", shape=box]\n");
                    nodesInBlock.add(nodeBuilder.toString());
                    nodeNamesInBlock.add("b" + i + "_false");

                    //last node jumps
                    dot.append("  b")
                            .append(i).append("_false -> b")
                            .append(branchNode.getSuccessor()).append("_l0 \n");
                } else {
                    dot.append("  ")
                            .append(nodeNamesInBlock.getLast()).append(" -> b")
                            .append(branchNode.getSuccessor()).append("_l0 [label=\"false\"]\n");
                }
            } else if (basicBlockNode.getTerminatingNode() instanceof NomReturnNode returnNode) {
                if (hasL0) {
                    dot.append("  ")
                            .append(nodeNamesInBlock.getLast()).append(" -> b")
                            .append(i).append("_ret\n");
                    nodeBuilder = new StringBuilder();
                    nodeBuilder.append('b')
                            .append(i).append("_ret [label=\"")
                            .append(returnNode).append("\"]\n");
                    nodesInBlock.add(nodeBuilder.toString());
                    nodeNamesInBlock.add("b" + i + "_ret");
                } else {
                    nodeBuilder = new StringBuilder();
                    nodeBuilder.append('b')
                            .append(i).append("_l0 [label=\"")
                            .append(returnNode).append("\"]\n");
                    nodesInBlock.add(nodeBuilder.toString());
                    nodeNamesInBlock.add("b" + i + "_l0");
                }
            }

            //add nodes
            for (String node : nodesInBlock) {
                dot.append("  ").append(node);
            }

            //add cluster
            dot.append("  subgraph cluster_b").append(i).append(" {\n");
            dot.append("    label = \"").append(basicBlockNode.blockName).append("\";\n");
            for (String nodeName : nodeNamesInBlock) {
                dot.append("    ").append(nodeName).append(";\n");
            }
            dot.append("  }\n");
        }

        //method name
        dot.append("  ").append("labelloc=\"t\"\n");
        dot.append("  ").append("label=\"").append(name).append("\"\n");

        dot.append("}");
        return dot.toString();
    }
}
