package com.nom.graalnom.runtime.constants;


import com.nom.graalnom.runtime.NomContext;
import org.graalvm.collections.Pair;

import java.util.List;

public class NomSuperInterfacesConstant extends NomConstant {
    private final List<Pair<Long,Long>> entries;
//    private List<NomInterface> interfaces;

    public NomSuperInterfacesConstant(List<Pair<Long,Long>> entries) {
        super(NomConstantType.CTSuperInterfaces);
        this.entries = entries;
    }

    /*
    virtual void FillConstantDependencies(NOM_CONSTANT_DEPENCENCY_CONTAINER& result) override
    {
        for (auto pair : entries)
        {
            result.push_back(std::get<0>(pair));
            result.push_back(std::get<1>(pair));
        }
    }

    const llvm::ArrayRef<NomInstantiationRef<NomInterface>> NomSuperInterfacesConstant::GetSuperInterfaces(NomSubstitutionContextRef  context) const
    {
        if (ifaces.size() == 0 && entries.size() != 0)
        {
            for (auto entry : entries)
            {
                ifaces.push_back(NomInstantiationRef<NomInterface>(NomConstants::GetInterface(std::get<0>(entry))->GetInterface(), NomConstants::GetTypeList(std::get<1>(entry))->GetTypeList(context)));
            }
        }
        return ifaces;
    }
     */

    @Override
    public void Print(boolean resolve) {
        System.out.print("SuperInterfaces (");
        boolean first = true;
        for (var entry : entries) {
            if (!first) {
                System.out.print(",");
            }
            first = false;
            NomContext.constants.PrintConstant(entry.getLeft(), resolve);
            System.out.print("<");
            NomContext.constants.PrintConstant(entry.getRight(), resolve);
            System.out.print(">");
        }
        System.out.print(")");
    }
}