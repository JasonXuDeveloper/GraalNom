package com.nom.graalnom.runtime.nodes.expression.object;

import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.literal.NomStringLiteralNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObjectLibrary;

public class NomReadFieldNode extends NomExpressionNode {
    @CompilerDirectives.CompilationFinal
    public NomExpressionNode receiverNode;

    public final String name;

    public NomReadFieldNode(NomExpressionNode receiverNode, String name) {
        this.receiverNode = receiverNode;
        this.name = name;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object receiver = receiverNode.executeGeneric(frame);
        if (receiver instanceof NomObject nObj) {
            return readNomObject(nObj, name);
        } else {
            throw CompilerDirectives.shouldNotReachHere("Receiver is not a NomObject");
        }
    }

    protected Object readNomObject(NomObject receiver, String name) {
        Object result;
        try {
            result = receiver.readMember(name);
        } catch (UnknownIdentifierException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public String toString() {
        return receiverNode.toString() + "." + name;
    }
}
