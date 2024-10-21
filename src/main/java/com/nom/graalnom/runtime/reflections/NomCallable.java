package com.nom.graalnom.runtime.reflections;

import com.nom.graalnom.NomLanguage;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.NomTypeListConstant;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.nodes.BytecodeDispatchNode;
import com.nom.graalnom.runtime.nodes.NomRootNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomFunctionBodyNode;
import com.nom.graalnom.runtime.opcodes.TransformedOpCode;
import com.nom.graalnom.runtime.opcodes.common.CastOpCode;
import com.nom.graalnom.runtime.opcodes.control.*;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.strings.TruffleString;

import java.util.ArrayList;
import java.util.List;

public class NomCallable {
    protected boolean declOnly;
    protected TruffleString name;
    protected TruffleString qName;
    protected short regCount;
    protected long argTypesId;
    protected long typeArgsId;//type parameters
    private final List<TransformedOpCode[]> bytecodes = new ArrayList<>();
    private NomFunction function;
    private NomFunction dynFunction;

    public NomCallable(TruffleString name, TruffleString qName, long regCount, long typeArgsId, long argTypesId, boolean declOnly) {
        this.name = name;
        this.qName = qName;
        this.regCount = (short) regCount;
        this.argTypesId = argTypesId;
        this.typeArgsId = typeArgsId;
        this.declOnly = declOnly;
    }

    public TruffleString GetName() {
        return name;
    }

    public TruffleString GetQName() {
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

    public void AddBytecode(TransformedOpCode[] bytecode) {
        for (TransformedOpCode code : bytecode) {
            if (code instanceof BranchOpCode branchOpCode) {
                //find the index of the n'th phi bytecode in TransformedOpCode[] bytecode
                int index = -1;
                for (int i = 0; i < bytecode.length; i++) {
                    if (bytecode[i] instanceof PhiOpCode) {
                        index++;
                        if (index == branchOpCode.getTarget()) {
                            branchOpCode.setTarget(i);
                            break;
                        }
                    }
                }

            } else if (code instanceof CondBranchOpCode condBranchOpCode) {
                int index = -1;
                for (int i = 0; i < bytecode.length; i++) {
                    if (bytecode[i] instanceof PhiOpCode) {
                        index++;
                        if (index == condBranchOpCode.getTrueTarget()) {
                            condBranchOpCode.setTrueTarget(i);
                            break;
                        }
                    }
                }

                index = -1;
                for (int i = 0; i < bytecode.length; i++) {
                    if (bytecode[i] instanceof PhiOpCode) {
                        index++;
                        if (index == condBranchOpCode.getFalseTarget()) {
                            condBranchOpCode.setFalseTarget(i);
                            break;
                        }
                    }
                }
            }
        }
        bytecodes.add(bytecode);
    }

    public NomFunction GetFunction(NomLanguage language) {
        if (function == null) {
            BytecodeDispatchNode[] bytecodeNodes = new BytecodeDispatchNode[this.bytecodes.size()];
            for (int i = 0; i < this.bytecodes.size(); i++) {
                bytecodeNodes[i] = new BytecodeDispatchNode(this.bytecodes.get(i), GetArgCount());
            }
            NomFunctionBodyNode body = new NomFunctionBodyNode(bytecodeNodes, regCount);
            FrameDescriptor.Builder builder = FrameDescriptor.newBuilder();
            builder.addSlots(100, FrameSlotKind.Illegal);
            NomRootNode root = new NomRootNode(language, builder.build(), body, qName, GetArgCount());
            function = new NomFunction(qName, body, root.getCallTarget(), regCount);
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

            List<TransformedOpCode> castOpCodes = new ArrayList<>();
            List<Long> types = typeListConstant.types;
            for (int j = 0; j < types.size(); j++) {
                long i = types.get(j);
                int typeId = (int) i;
                castOpCodes.add(new CastOpCode(1 + j, 1 + j, typeId));
            }

            BytecodeDispatchNode[] bytecodeNodes = new BytecodeDispatchNode[this.bytecodes.size() + 1];
            bytecodeNodes[0] = new BytecodeDispatchNode(castOpCodes.toArray(new TransformedOpCode[0]), GetArgCount());
            for (int i = 0; i < this.bytecodes.size(); i++) {
                bytecodeNodes[i + 1] = new BytecodeDispatchNode(this.bytecodes.get(i), GetArgCount());
            }

            NomFunctionBodyNode body = new NomFunctionBodyNode(bytecodeNodes, regCount);
            TruffleString dynName = TruffleString.fromJavaStringUncached(qName.toString() + "_dyn", TruffleString.Encoding.UTF_8);
            FrameDescriptor.Builder builder = FrameDescriptor.newBuilder();
            builder.addSlots(100, FrameSlotKind.Illegal);
            NomRootNode root = new NomRootNode(language, builder.build(), body, dynName, GetArgCount());
            dynFunction = new NomFunction(dynName, body, root.getCallTarget(), regCount);
        }

        return dynFunction;
    }
}
