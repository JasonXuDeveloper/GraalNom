package com.nom.graalnom.runtime.nodes.expression.binary;

import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild("leftNode")
@NodeChild("rightNode")
public abstract class NomBinaryNode extends NomExpressionNode {
}
