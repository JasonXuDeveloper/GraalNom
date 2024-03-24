package com.nom.graalnom.runtime.nodes.expression.object;

import com.nom.graalnom.runtime.datatypes.ExtendedObject;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.literal.NomStringLiteralNode;
import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObjectLibrary;
import com.oracle.truffle.api.strings.TruffleString;

@NodeChild("receiverNode")
@NodeChild(value = "nameNode", type = NomStringLiteralNode.class)
public abstract class NomReadFieldNode extends NomExpressionNode {
    public abstract NomExpressionNode getReceiverNode();
    public abstract NomStringLiteralNode getNameNode();
    static final int LIBRARY_LIMIT = 3;

    @Specialization(limit = "LIBRARY_LIMIT")
    protected static Object readNomObject(ExtendedObject receiver, TruffleString name,
                                         @Bind("this") Node node,
                                         @CachedLibrary("receiver") DynamicObjectLibrary objectLibrary) {
        Object result = objectLibrary.getOrDefault(receiver, name, null);
        if (result == null) {
            throw new RuntimeException("Field not found: " + name);
        }
        return result;
    }

    @Override
    public String toString() {
        return getReceiverNode().toString() + "." + getNameNode().Value().toString();
    }
}
