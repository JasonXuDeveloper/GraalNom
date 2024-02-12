package com.nom.graalnom.runtime.reflections;

public class NomType {
    /*
    static bool PointwiseSubtype(TypeList l, TypeList r, bool optimistic=false);

    static const NomTopType * const Anything;
    static const NomBottomType* const Nothing;
    static const NomDynamicType* const Dynamic;
    static NomTypeRef AnythingRef;
    static NomTypeRef NothingRef;
    static NomTypeRef DynamicRef;
    NomType();
    virtual ~NomType();


    virtual bool ContainsVariables() const = 0;
    virtual bool ContainsVariableIndex(int index) const = 0;
    virtual llvm::Value * GenerateRTInstantiation(NomBuilder &builder, CompileEnv* env) const = 0;

    virtual size_t GetHashCode() const = 0;
    virtual TypeReferenceType GetTypeReferenceType() const = 0;

    virtual bool IsSubtype(NomTypeRef other, bool optimistic = false) const = 0;
    virtual bool IsSubtype(NomBottomTypeRef other, bool optimistic = false) const = 0;
    virtual bool IsSubtype(NomDynamicTypeRef other, bool optimistic = false) const = 0;
    virtual bool IsSubtype(NomClassTypeRef other, bool optimistic = false) const = 0;
    virtual bool IsSubtype(NomTopTypeRef other, bool optimistic = false) const = 0;
    virtual bool IsSubtype(NomTypeVarRef other, bool optimistic = false) const = 0;
    virtual bool IsSubtype(NomMaybeTypeRef other, bool optimistic = false) const = 0;
    virtual bool IsSupertype(NomTypeRef other, bool optimistic = false) const = 0;
    virtual bool IsSupertype(NomDynamicTypeRef other, bool optimistic = false) const = 0;
    virtual bool IsSupertype(NomBottomTypeRef other, bool optimistic = false) const = 0;
    virtual bool IsSupertype(NomClassTypeRef other, bool optimistic = false) const = 0;
    virtual bool IsSupertype(NomTopTypeRef other, bool optimistic = false) const = 0;
    virtual bool IsSupertype(NomTypeVarRef other, bool optimistic = false) const = 0;
    virtual bool IsSupertype(NomMaybeTypeRef other, bool optimistic = false) const = 0;

    virtual bool IsDisjoint(NomTypeRef other) const = 0;
    virtual bool IsDisjoint(NomBottomTypeRef other) const = 0;
    virtual bool IsDisjoint(NomClassTypeRef other) const = 0;
    virtual bool IsDisjoint(NomTopTypeRef other) const = 0;
    virtual bool IsDisjoint(NomTypeVarRef other) const = 0;
    virtual bool IsDisjoint(NomMaybeTypeRef other) const = 0;

    //Returns true iff type can represent a primitive
    virtual bool PossiblyPrimitive() const = 0;
    //Returns true iff type can represent a primitive, but doesn't have to
    virtual bool UncertainlyPrimitive() const = 0;

    virtual NomTypeRef SubstituteSubtyping(const NomSubstitutionContext* context) const = 0;

    virtual llvm::Type * GetLLVMType() const = 0;
    virtual const std::string GetSymbolRep() const = 0;


    virtual TypeKind GetKind() const = 0;

    virtual intptr_t GetRTElement() const = 0;
    virtual NomClassTypeRef GetClassInstantiation(const NomNamed *named) const = 0;
     */
}
