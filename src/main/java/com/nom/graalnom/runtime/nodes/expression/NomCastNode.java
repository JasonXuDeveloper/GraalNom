package com.nom.graalnom.runtime.nodes.expression;

import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.constants.*;
import com.nom.graalnom.runtime.datatypes.NomNull;
import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.nodes.expression.unary.NomUnaryNode;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.nom.graalnom.runtime.reflections.NomInterface;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "(CAST)")
public class NomCastNode extends NomExpressionNode {

    public NomConstant type;
    @Node.Child
    private NomExpressionNode valueNode;

    public NomCastNode(NomExpressionNode valueNode, int type) {
        this.type = NomContext.constants.Get(type);
        this.valueNode = valueNode;
    }

    public Object executeGeneric(VirtualFrame frame) {
        Object value = valueNode.executeGeneric(frame);
        if (type instanceof NomClassTypeConstant ct) {
            String cName;
            if (ct.GetClass() == null) {
                NomInterfaceConstant inter = ct.GetInterface();
                if (inter == null) {
                    throw new RuntimeException("Constant is wrong");
                }
                cName = inter.GetName();
            } else {
                cName = ct.GetClass().GetName();
            }
            if (cName.equals("Int_0")) {
                if (value instanceof Long) {
                    return value;
                }
            } else if (cName.equals("Float_0")) {
                if (value instanceof Double) {
                    return value;
                }
            } else if (cName.equals("Bool_0")) {
                if (value instanceof Boolean) {
                    return value;
                }
            } else if (cName.equals("String_0")) {
                if (value instanceof String) {
                    return value;
                }
            } else if (cName.equals("Null_0")) {
                if (value instanceof NomNull) {
                    return value;
                }
            } else {
                if (!(value instanceof NomObject nomObj)) {
                    throw new RuntimeException("Unsupported cast");
                }

                NomClass cls = nomObj.GetClass();
                NomClassConstant cc = ct.GetClass();
                String name;
                if (cc == null) {
                    NomInterfaceConstant inter = ct.GetInterface();
                    if (inter == null) {
                        throw new RuntimeException("Constant is wrong");
                    }
                    name = inter.GetName();
                } else {
                    name = cc.GetName();
                }

                NomInterface inter = NomContext.classes.get(name);
                if (inter == null) {
                    throw new RuntimeException(name + " not found");
                }

                if (inter instanceof NomClass nc) {
                    if (!cls.superClasses.contains(nc)) {
                        throw new RuntimeException(cls.GetName() + " is not a subclass of " + nc.GetName());
                    }

                    return value;
                } else {
                    if (!cls.superInterfaces.contains(inter)) {
                        throw new RuntimeException(cls.GetName() + " is not a subclass of " + inter.GetName());
                    }

                    return value;
                }

            }
        }
        if (type instanceof NomDynamicTypeConstant) {
            return value;
        }

        throw new RuntimeException("Unsupported cast");
    }

    @Override
    public String toString() {
        if (type instanceof NomDynamicTypeConstant) return "(dyn) " + valueNode.toString();
        if (!(type instanceof NomClassTypeConstant ct)) {
            return "(INVALID CAST)";
        }
        String cName;
        if (ct.GetClass() == null) {
            NomInterfaceConstant inter = ct.GetInterface();
            if (inter == null) {
                throw new RuntimeException("Constant is wrong");
            }
            cName = inter.GetName();
        } else {
            cName = ct.GetClass().GetName();
        }
        return "(" + cName + ") " + valueNode.toString();
    }
}
