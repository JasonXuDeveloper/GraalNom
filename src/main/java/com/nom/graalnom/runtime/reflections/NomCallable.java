package com.nom.graalnom.runtime.reflections;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomTypeListConstant;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.nodes.NomRootNode;
import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomBasicBlockNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomEndOfBasicBlockNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomFunctionBodyNode;
import com.nom.graalnom.runtime.nodes.expression.NomCastNode;
import com.oracle.truffle.api.frame.FrameDescriptor;

import java.util.ArrayList;
import java.util.List;

public class NomCallable {
    protected boolean declOnly;
    protected String name;
    protected String qName;
    protected short regCount;
    protected long argTypesId;
    protected long typeArgsId;//type parameters
    private final List<NomBasicBlockNode> basicBlocks;
    private NomFunction function;
    private NomFunction dynFunction;

    public NomCallable(String name, String qName, long regCount, long typeArgsId, long argTypesId, boolean declOnly) {
        this.name = name;
        this.qName = qName;
        this.regCount = (short) regCount;
        this.argTypesId = argTypesId;
        this.typeArgsId = typeArgsId;
        this.declOnly = declOnly;
        this.basicBlocks = new ArrayList<>();
    }

    public String GetName() {
        return name;
    }

    public String GetQName() {
        return qName;
    }

    public short GetRegCount() {
        return regCount;
    }

    public NomTypeListConstant GetArgTypes() {
        return NomContext.constants.GetTypeList(argTypesId);
    }

    public int GetArgCount() {
        if (GetArgTypes() != null) {
            return GetArgTypes().Count();
        }

        return 0;
    }

    public int GetBasicBlockCount() {
        return basicBlocks.size();
    }

    private final List<NomStatementNode> statementNodes = new ArrayList<>();

    public void AddInstruction(NomStatementNode stmt) {
        if (stmt == null)
            return;
        statementNodes.add(stmt);
        if (stmt instanceof NomEndOfBasicBlockNode) {
            AddBasicBlock(statementNodes.toArray(new NomStatementNode[0]));
            statementNodes.clear();
        }
    }

    private void AddBasicBlock(NomStatementNode... stmts) {
        if (stmts.length == 0)
            return;
        if (!(stmts[stmts.length - 1] instanceof NomEndOfBasicBlockNode)) {
            throw new IllegalArgumentException("Last statement in a basic block must be an end of block node");
        }
        basicBlocks.add(new NomBasicBlockNode(stmts, "blk" + basicBlocks.size()));
    }

    private final static FrameDescriptor f = new FrameDescriptor();

    public NomFunction GetFunction(NomLanguage language) {
        if (function == null) {
            NomFunctionBodyNode body = new NomFunctionBodyNode(basicBlocks.toArray(new NomBasicBlockNode[0]), regCount);
            NomRootNode root = new NomRootNode(language, f, body, qName, GetArgCount());
            function = new NomFunction(qName, root, root.getCallTarget(), regCount);
        }

        return function;
    }

    public NomFunction GetDynFunction(NomLanguage language) {
        if (dynFunction == null) {
            NomTypeListConstant typeListConstant = NomContext.constants.GetTypeList(argTypesId);
            if (typeListConstant == null) {
                dynFunction = function;
                return dynFunction;
            }

            NomBasicBlockNode firstBlock = basicBlocks.getFirst();
            NomStatementNode[] newNodes = new NomStatementNode[firstBlock.bodyNodes.length + typeListConstant.types.size()];
            List<Long> types = typeListConstant.types;
            for (int j = 0; j < types.size(); j++) {
                long i = types.get(j);
                int typeId = (int) i;
                newNodes[j] = new NomCastNode(1 + j, typeId);
            }
            //copy the rest of the nodes
            System.arraycopy(firstBlock.bodyNodes, 0, newNodes, types.size(), firstBlock.bodyNodes.length);
            basicBlocks.set(0, new NomBasicBlockNode(newNodes, firstBlock.blockName));

            NomFunctionBodyNode body = new NomFunctionBodyNode(basicBlocks.toArray(new NomBasicBlockNode[0]), regCount);
            NomRootNode root = new NomRootNode(language, f, body, qName + "_dyn", GetArgCount());
            dynFunction = new NomFunction(qName + "_dyn", root, root.getCallTarget(), regCount);
        }

        return dynFunction;
    }
}
