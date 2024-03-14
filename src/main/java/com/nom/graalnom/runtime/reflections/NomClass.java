package com.nom.graalnom.runtime.reflections;

import com.nom.graalnom.runtime.NomContext;

import java.util.List;

public class NomClass extends NomInterface {
    public List<NomTypedField> Fields;
    public List<NomTypedField> AllFields;
    public long SuperClass;
    public NomClass SuperClassRef;

    public List<NomStaticMethod> StaticMethods;

/*
    std::vector<NomLambda*> Lambdas;
    std::vector<NomRecord*> Structs;

    std::vector<NomConstructor*> Constructors;

    void AddStaticMethod(NomStaticMethod* method)
    {
        StaticMethods.push_back(method);
    }
    void AddConstructor(NomConstructor* constructor)
    {
        Constructors.push_back(constructor);
    }
    void AddLambda(NomLambda* lambda)
    {
        Lambdas.push_back(lambda);
    }
    void AddStruct(NomRecord* strct)
    {
        Structs.push_back(strct);
    }
 */

    /*
    NomClassLoaded::NomClassLoaded(const ConstantID name, ConstantID typeArgs, ConstantID superClass, const ConstantID superInterfaces, const NomMemberContext* parent) : NomInterface(NomConstants::GetString(name)->GetText()->ToStdString()), NomInterfaceLoaded(name, typeArgs, superInterfaces, parent), superClass(superClass)
    {
        RegisterClass(NomConstants::GetString(name)->GetText(), this);
    }
     */
    public NomClass(long name, long typeArgs, long superClass, long superInterfaces) {
        super(name);
        this.SuperClass = superClass;
        this.Fields = new java.util.ArrayList<>();
        this.AllFields = new java.util.ArrayList<>();
        this.StaticMethods = new java.util.ArrayList<>();
    }

    public static void RegisterClass(String name, NomClass cls) {
        NomContext.classes.put(name, cls);
    }

    public static NomClass getClass(String name) {
        return NomContext.classes.get(name);
    }


    public NomTypedField AddField(long name, long type, Visibility visibility, boolean isReadOnly, boolean isVolatile) {
        NomTypedField field = new NomTypedField(this, name, type, visibility, isReadOnly, isVolatile);
        Fields.add(field);
        return field;
    }

    public NomStaticMethod AddStaticMethod(String name, String qname, long typeArgs, long returnType, long argTypes, int regCount) {
        NomStaticMethod staticMethod = new NomStaticMethod(name, this, qname, returnType, typeArgs, argTypes, regCount, false);
        StaticMethods.add(staticMethod);
        return staticMethod;
    }

    /*
    virtual void PushDependencies(std::set<ConstantID>& set) const override
			{
				NomInterfaceLoaded::PushDependencies(set);
				set.insert(superClass);

				for (auto method : StaticMethods)
				{
					dynamic_cast<NomCallableLoaded*>(method)->PushDependencies(set);
				}
				for (auto cnstr : Constructors)
				{
					dynamic_cast<NomCallableLoaded*>(cnstr)->PushDependencies(set);
				}
				for (auto lambda : Lambdas)
				{
					dynamic_cast<NomCallableLoaded*>(lambda)->PushDependencies(set);
				}
				for (auto strct : Structs)
				{
					dynamic_cast<NomCallableLoaded*>(strct)->PushDependencies(set);
				}
				for (auto field : Fields)
				{
					field->PushDependencies(set);
				}
			}
     */
    public void PushDependencies(List<Long> set) {
        set.add(SuperClass);
        for (NomTypedField field : Fields) {
            field.PushDependencies(set);
        }
    }
}
