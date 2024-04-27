package com.nom.graalnom.runtime.nodes.expression.object;

import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.literal.NomStringLiteralNode;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObjectLibrary;


@NodeChild("receiverNode")
@NodeChild(value = "nameNode", type = NomStringLiteralNode.class)
@NodeChild("valueNode")
public abstract class NomWriteFieldNode extends NomExpressionNode {
    public abstract NomStringLiteralNode getNameNode();

    public abstract NomExpressionNode getReceiverNode();

    public abstract NomExpressionNode getValueNode();

    static final int LIBRARY_LIMIT = 3;


    @Specialization(limit = "LIBRARY_LIMIT")
    protected static Object writeNomObject(NomObject receiver, String name, Object value,
                                           @Bind("this") Node node,
                                           @CachedLibrary("receiver") DynamicObjectLibrary objectLibrary) {
        receiver.writeMember(name, value, objectLibrary);
        return receiver;
    }

    @Override
    public String toString() {
        return getReceiverNode().toString() + "." + getNameNode().Value() + " = " + getValueNode().toString();
    }
}
