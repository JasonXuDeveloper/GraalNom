package com.nom.graalnom.runtime.nodes.expression.object;

import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.literal.NomStringLiteralNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObjectLibrary;


public class NomWriteFieldNode extends NomExpressionNode {
    @CompilerDirectives.CompilationFinal
    public NomExpressionNode receiverNode;
    @CompilerDirectives.CompilationFinal
    public NomExpressionNode valueNode;

    public final String name;

    public NomWriteFieldNode(NomExpressionNode receiverNode, NomExpressionNode valueNode, String name) {
        this.receiverNode = receiverNode;
        this.valueNode = valueNode;
        this.name = name;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object receiver = receiverNode.executeGeneric(frame);
        if (receiver instanceof NomObject nObj) {
            return writeNomObject(nObj, name, valueNode.executeGeneric(frame));
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw CompilerDirectives.shouldNotReachHere("Receiver is not a NomObject");
        }
    }

    protected static Object writeNomObject(NomObject receiver, String name, Object value) {
        receiver.writeMember(name, value);
        return receiver;
    }

    @Override
    public String toString() {
        return receiverNode.toString() + "." + name + " = " + valueNode.toString();
    }
}
