package com.nom.ideanom;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class NomAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder holder) {

        if (psiElement instanceof NomClassdef classDef) {
            //ClassName
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(classDef.getId())
                    .textAttributes(NomSyntaxHighlighter.ClassName).create();
            //Inheritance
            if (!classDef.getInheritancedeclList().isEmpty()) {
                for (NomInheritancedecl inheritancedecl : classDef.getInheritancedeclList()) {
                    for (NomRefident refIdent : inheritancedecl.getRefqname().getRefidentList()) {
                        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                                .range(refIdent.getIdent())
                                .textAttributes(NomSyntaxHighlighter.TypeSource).create();
                    }
                }
            }
        }

        if (psiElement instanceof NomStaticmethdef staticMethDef) {
            //MethodName
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(staticMethDef.getDeclident().getIdent())
                    .textAttributes(NomSyntaxHighlighter.MethodName).create();
            //Arguments
            for (NomArgdecl argdecl : staticMethDef.getArgdeclList()) {
                NomDtype dtype = argdecl.getDtype();
                if (dtype != null) {
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                            .range(dtype)
                            .textAttributes(NomSyntaxHighlighter.TypeSource).create();
                }
            }
            //Return type
            NomType type = staticMethDef.getType();
            if (type != null) {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(type)
                        .textAttributes(NomSyntaxHighlighter.TypeSource).create();
            }
        }

        if (psiElement instanceof NomExpr expr) {
            //method call of a local class method
            if (!expr.getExprPrimeList().isEmpty()) {
                NomExprPrime exprPrime = expr.getExprPrimeList().get(0);
                if (exprPrime.getFirstChild().textMatches("(")) {
                    NomIdent nameEle = expr.getExprNoRecur().getIdent();
                    assert nameEle != null;
                    String methName = nameEle.getText();
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                            .range(nameEle)
                            .textAttributes(NomSyntaxHighlighter.MethodName).create();

                    //get classdef this expr is in
                    PsiElement classDef = expr;
                    while (!(classDef instanceof NomClassdef clsDef)) {
                        classDef = classDef.getParent();
                    }
                    //get method
                    String clsName = ((NomClassdef) classDef).getId().getText();
                    NomMethdef methdef = NomUtil.getMethod(clsDef, methName);
                    NomStaticmethdef staticmethdef = NomUtil.getStaticMethod(clsDef, methName);
                    //check existence
                    if (methdef == null && staticmethdef == null) {
                        holder.newAnnotation(HighlightSeverity.ERROR,
                                        "Method " + methName + " not found in class " + clsName)
                                .range(nameEle)
                                .create();
                    }
                    if (methdef != null || staticmethdef != null) {
                        //check arg count
                        int argCnt;
                        if (methdef != null) {
                            argCnt = methdef.getArgdeclList().size();
                        } else {
                            argCnt = staticmethdef.getArgdeclList().size();
                        }

                        if (exprPrime.getExprList().size() != argCnt) {
                            holder.newAnnotation(HighlightSeverity.ERROR,
                                            "Method " + clsName + "." + methName + " expects " + argCnt + " arguments")
                                    .range(exprPrime)
                                    .create();
                        }
                    }
                }
            }
            //method invoke chain
            for (NomExprPrime exprPrime : expr.getExprPrimeList()) {
                if (exprPrime.getRefident() != null) {
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                            .range(exprPrime.getRefident().getIdent())
                            .textAttributes(NomSyntaxHighlighter.InvokeFunction).create();
                }
            }
        }

        //assignment
        if (psiElement instanceof NomStmt nomStmt) {
            if (nomStmt.getDtype() != null) {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(nomStmt.getDtype())
                        .textAttributes(NomSyntaxHighlighter.TypeSource).create();
            }
        }
    }
}
