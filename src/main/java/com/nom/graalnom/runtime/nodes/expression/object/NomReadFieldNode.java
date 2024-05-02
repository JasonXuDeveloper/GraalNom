package com.nom.graalnom.runtime.nodes.expression.object;

import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.literal.NomStringLiteralNode;
import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObjectLibrary;

@NodeChild("receiverNode")
@NodeChild(value = "nameNode", type = NomStringLiteralNode.class)
public abstract class NomReadFieldNode extends NomExpressionNode {
    public abstract NomExpressionNode getReceiverNode();

    public abstract NomStringLiteralNode getNameNode();

    static final int LIBRARY_LIMIT = 3;

    @Specialization(limit = "LIBRARY_LIMIT")
    protected Object readNomObject(NomObject receiver, String name) {
        Object result = null;
        try {
            result = receiver.readMember(name);
        } catch (UnknownIdentifierException e) {
            throw new RuntimeException(e);
        }
        if (result == null) {
            throw new RuntimeException("Field not found: " + name);
        }
        return result;
    }

    @Override
    public String toString() {
        return getReceiverNode().toString() + "." + getNameNode().Value();
    }
}
