package com.nom.ideanom;

import java.util.ArrayList;
import java.util.List;

public class NomUtil {
    public static List<String> getMethodNames(NomClassdef classdef){
        List<String> methodNames = new ArrayList<>();
        for (NomMethdef methdef : classdef.getMethdefList()) {
            methodNames.add(methdef.getDeclident().getIdent().getText());
        }
        for (NomStaticmethdef staticmethdef : classdef.getStaticmethdefList()) {
            methodNames.add(staticmethdef.getDeclident().getIdent().getText());
        }

        return methodNames;
    }
    public static NomMethdef getMethod(NomClassdef classdef, String methodName){
        for (NomMethdef methdef : classdef.getMethdefList()) {
            if (methdef.getDeclident().getIdent().getText().equals(methodName)){
                return methdef;
            }
        }
        return null;
    }

    public static NomStaticmethdef getStaticMethod(NomClassdef classdef, String methodName){
        for (NomStaticmethdef staticmethdef : classdef.getStaticmethdefList()) {
            if (staticmethdef.getDeclident().getIdent().getText().equals(methodName)){
                return staticmethdef;
            }
        }
        return null;
    }
}
