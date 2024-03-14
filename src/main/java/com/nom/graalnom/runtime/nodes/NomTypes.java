package com.nom.graalnom.runtime.nodes;

import com.oracle.truffle.api.dsl.TypeSystem;

/**
 * The type system of MonNom. Based on the {@link TypeSystem}
 * annotation, the Truffle DSL generates the subclass {@link NomTypesGen} with type test and type
 * conversion methods for some types. In this class, we only cover types where the automatically
 * generated ones would not be sufficient.
 */
@TypeSystem({long.class, boolean.class, float.class})
public abstract class NomTypes {
}
