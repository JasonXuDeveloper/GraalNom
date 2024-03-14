package com.nom.graalnom.runtime.constants;

import com.nom.graalnom.runtime.NomContext;

public class NomClassConstant extends NomConstant{
    private final long Library;
    private final long Name;

    //public NomClass cls;

    public NomClassConstant(long library, long name) {
        super(NomConstantType.CTClass);
        this.Library = library;
        this.Name = name;
    }

    public String GetName() {
        return NomContext.constants.GetString(Name).GetText().toString();
    }

    /*
    const NomClass* NomClassConstant::GetClass()
		{
			if (cls == nullptr)
			{
				throw new std::exception();
				////cls = NomClass::getClass(((NomStringConstant*)NomConstants::Get(Name))->GetText());
				//auto libname = NomConstants::GetString(this->Library)->GetText()->ToStdString();
				//auto clsname = NomConstants::GetString(this->Name)->GetText();
				//cls = Loader::GetInstance()->GetLibrary(&libname)->GetClass(clsname);
			}
			return cls;
		}

		void NomClassConstant::EnsureClassLoaded(NomModule* mod)
		{
			if (cls == nullptr)
			{
				//cls = NomClass::getClass(((NomStringConstant*)NomConstants::Get(Name))->GetText());
				auto libname = NomConstants::GetString(this->Library)->GetText()->ToStdString();
				auto clsname = NomConstants::GetString(this->Name)->GetText();
				cls = Loader::GetInstance()->GetLibrary(&libname)->GetClass(clsname, mod);
			}
		}

		virtual void FillConstantDependencies(NOM_CONSTANT_DEPENCENCY_CONTAINER& result) override
        {
            result.push_back(Library);
            result.push_back(Name);
        }
     */

    @Override
    public void Print(boolean resolve) {
        System.out.print("Class ");
        NomContext.constants.PrintConstant(Library, resolve);
        System.out.print("::");
        NomContext.constants.PrintConstant(Name, resolve);
    }
}
