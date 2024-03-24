package com.nom.graalnom.runtime.nodes.expression.object;

import com.nom.graalnom.runtime.datatypes.ExtendedObject;
import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.literal.NomStringLiteralNode;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObjectLibrary;
import com.oracle.truffle.api.strings.TruffleString;


@NodeChild("receiverNode")
@NodeChild(value = "nameNode", type = NomStringLiteralNode.class)
@NodeChild("valueNode")
public abstract class NomWriteFieldNode extends NomExpressionNode {
    public abstract NomStringLiteralNode getNameNode();

    public abstract NomExpressionNode getReceiverNode();

    public abstract NomExpressionNode getValueNode();

    static final int LIBRARY_LIMIT = 3;


    @Specialization(limit = "LIBRARY_LIMIT")
    protected static Object writeNomObject(ExtendedObject receiver, TruffleString name, Object value,
                                           @Bind("this") Node node,
                                           @CachedLibrary("receiver") DynamicObjectLibrary objectLibrary) {
        objectLibrary.put(receiver, name, value);
        return value;
    }

    @Override
    public String toString() {
        return getReceiverNode().toString() + "." + getNameNode().Value().toString() + " = " + getValueNode().toString();
    }
}
