package com.nom.graalnom.runtime.nodes.local;

import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.strings.TruffleString;

/**
 * Node to write a local variable to a function's {@link VirtualFrame frame}. The Truffle frame API
 * allows to store primitive values of all Java primitive types, and Object values.
 */
@NodeChild("valueNode")
@NodeField(name = "regIndex", type = int.class)
public abstract class NomWriteRegisterNode extends NomExpressionNode {

    /**
     * Returns the descriptor of the accessed local variable. The implementation of this method is
     * created by the Truffle DSL based on the {@link NodeField} annotation on the class.
     */
    public abstract int getRegIndex();

    public abstract NomExpressionNode getValueNode();

    public final TruffleString getRegName() {
        return (TruffleString) getRootNode().getFrameDescriptor().getSlotName(getRegIndex());
    }

    /**
     * Specialized method to write a primitive {@code long} value. This is only possible if the
     * local variable also has currently the type {@code long} or was never written before,
     * therefore a Truffle DSL {@link #isLongOrIllegal(VirtualFrame) custom guard} is specified.
     */
    @Specialization(guards = "isLongOrIllegal(frame)")
    protected long writeLong(VirtualFrame frame, long value) {
        /* Initialize type on first write of the local variable. No-op if kind is already Long. */
        frame.getFrameDescriptor().setSlotKind(getRegIndex(), FrameSlotKind.Long);

        frame.setLong(getRegIndex(), value);
        return value;
    }

    @Specialization(guards = "isBooleanOrIllegal(frame)")
    protected boolean writeBoolean(VirtualFrame frame, boolean value) {
        /* Initialize type on first write of the local variable. No-op if kind is already Long. */
        frame.getFrameDescriptor().setSlotKind(getRegIndex(), FrameSlotKind.Boolean);

        frame.setBoolean(getRegIndex(), value);
        return value;
    }

    @Specialization(guards = "isDoubleOrIllegal(frame)")
    protected double writeDouble(VirtualFrame frame, float value) {
        /* Initialize type on first write of the local variable. No-op if kind is already Long. */
        frame.getFrameDescriptor().setSlotKind(getRegIndex(), FrameSlotKind.Double);

        frame.setFloat(getRegIndex(), value);
        return value;
    }

    /**
     * Generic write method that works for all possible types.
     * <p>
     * Why is this method annotated with {@link Specialization} and not {@link Fallback}? For a
     * {@link Fallback} method, the Truffle DSL generated code would try all other specializations
     * first before calling this method. We know that all these specializations would fail their
     * guards, so there is no point in calling them. Since this method takes a value of type
     * {@link Object}, it is guaranteed to never fail, i.e., once we are in this specialization the
     * node will never be re-specialized.
     */
    @Specialization(replaces = {"writeLong", "writeBoolean", "writeDouble"})
    protected Object write(VirtualFrame frame, Object value) {
        /*
         * Regardless of the type before, the new and final type of the local variable is Object.
         * Changing the slot kind also discards compiled code, because the variable type is
         * important when the compiler optimizes a method.
         *
         * No-op if kind is already Object.
         */
        frame.getFrameDescriptor().setSlotKind(getRegIndex(), FrameSlotKind.Object);

        frame.setObject(getRegIndex(), value);
        return value;
    }

    public abstract void executeWrite(VirtualFrame frame, Object value);

    /**
     * Guard function that the local variable has the type {@code long}.
     *
     * @param frame The parameter seems unnecessary, but it is required: Without the parameter, the
     *              Truffle DSL would not check the guard on every execution of the specialization.
     *              Guards without parameters are assumed to be pure, but our guard depends on the
     *              slot kind which can change.
     */
    protected boolean isLongOrIllegal(VirtualFrame frame) {
        final FrameSlotKind kind = frame.getFrameDescriptor().getSlotKind(getRegIndex());
        return kind == FrameSlotKind.Long || kind == FrameSlotKind.Illegal;
    }

    protected boolean isBooleanOrIllegal(VirtualFrame frame) {
        final FrameSlotKind kind = frame.getFrameDescriptor().getSlotKind(getRegIndex());
        return kind == FrameSlotKind.Boolean || kind == FrameSlotKind.Illegal;
    }

    protected boolean isDoubleOrIllegal(VirtualFrame frame) {
        final FrameSlotKind kind = frame.getFrameDescriptor().getSlotKind(getRegIndex());
        return kind == FrameSlotKind.Double || kind == FrameSlotKind.Illegal;
    }

    @Override
    public String toString() {
        return "reg[" + getRegIndex() + "] = " + getValueNode().toString();
    }
}
