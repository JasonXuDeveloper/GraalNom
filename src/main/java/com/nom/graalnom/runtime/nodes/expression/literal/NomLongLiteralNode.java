package com.nom.graalnom.runtime.nodes.expression.literal;

import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

/**
 * Constant literal for a primitive {@code long} value. The unboxed value can be returned when the
 * parent expects a long value and calls {@link NomLongLiteralNode#executeLong}. In the generic case,
 * the primitive value is automatically boxed by Java.
 */
@NodeInfo(shortName = "const")
public final class NomLongLiteralNode extends NomExpressionNode {
    private final long value;

    public NomLongLiteralNode(long value) {
        this.value = value;
    }

    @Override
    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}
