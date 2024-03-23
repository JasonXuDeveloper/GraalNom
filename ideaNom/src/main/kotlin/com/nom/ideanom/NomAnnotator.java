package com.nom.ideanom;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class NomAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder holder) {

        if (psiElement instanceof NomClassdef classDef) {
            //ClassName
            renderNode(classDef.getId(), holder, NomSyntaxHighlighter.CLASSNAME);
            //Inheritance
            if (!classDef.getInheritancedeclList().isEmpty()) {
                classDef.getInheritancedeclList()
                        .forEach(inheritancedecl -> inheritancedecl.getRefqname().getRefidentList().forEach(refIdent -> {
                            renderNode(refIdent.getIdent(), holder, NomSyntaxHighlighter.CLASSNAME);
                            if (!refIdent.getTypeList().isEmpty()) {
                                refIdent.getTypeList().forEach(type -> renderNode(type, holder, NomSyntaxHighlighter.TYPEARG));
                            }
                        }));
            }
            //Fields
            classDef.getFielddeclList().forEach(fielddecl -> renderNode(fielddecl.getDtype(), holder, NomSyntaxHighlighter.TYPEARG));
            //Constructors
            //Arguments
            classDef.getConstructorList().forEach(constrdef -> constrdef.getArgdeclList()
                    .forEach(argdecl -> renderNode(argdecl.getDtype(), holder, NomSyntaxHighlighter.TYPEARG)));
            //StaticFields
            classDef.getStaticfielddeclList().forEach(staticfielddecl -> renderNode(staticfielddecl.getDtype(), holder, NomSyntaxHighlighter.TYPEARG));
            //StaticMethods
            for (NomStaticmethdef staticmethdef : classDef.getStaticmethdefList()) {
                //MethodName
                renderNode(staticmethdef.getDeclident().getIdent(), holder, NomSyntaxHighlighter.METHODNAME);
                //Arguments
                staticmethdef.getArgdeclList().forEach(argdecl -> renderNode(argdecl.getDtype(), holder, NomSyntaxHighlighter.TYPEARG));
                //Return type
                renderNomType(staticmethdef.getType(), holder);
            }
            //Methods
            for (NomMethdef methdef : classDef.getMethdefList()) {
                //MethodName
                renderNode(methdef.getDeclident().getIdent(), holder, NomSyntaxHighlighter.METHODNAME);
                //Arguments
                methdef.getArgdeclList().forEach(argdecl -> renderNode(argdecl.getDtype(), holder, NomSyntaxHighlighter.TYPEARG));
                //Return type
                renderNomDtype(methdef.getDtype(), holder);
            }
        }

        if (psiElement instanceof NomInterfacedef interfacedef) {
            //ClassName
            renderNode(interfacedef.getDeclident().getIdent(), holder, NomSyntaxHighlighter.CLASSNAME);
            //TypeArgument
            if (!interfacedef.getDeclident().getTypeargspecList().isEmpty()) {
                for (NomTypeargspec type : interfacedef.getDeclident().getTypeargspecList()) {
                    renderNode(type.getIdent(), holder, NomSyntaxHighlighter.TYPEARG);
                }
            }
            //Methods
            for (NomMethdecl methdecl : interfacedef.getMethdeclList()) {
                //MethodName
                renderNode(methdecl.getDeclident().getIdent(), holder, NomSyntaxHighlighter.METHODNAME);
                //Arguments
                methdecl.getArgdeclList().forEach(argdecl -> renderNode(argdecl.getDtype(), holder, NomSyntaxHighlighter.TYPEARG));
                //Return type
                renderNomType(methdecl.getType(), holder);
            }
        }

        if (psiElement instanceof NomExpr expr) {
            //new
            if (expr.getExprNoRecur().getRefqname() != null) {
                expr.getExprNoRecur().getRefqname().getRefidentList().forEach(refIdent -> {
                    renderNode(refIdent.getIdent(), holder, NomSyntaxHighlighter.TYPEARG);
                    if (!refIdent.getTypeList().isEmpty()) {
                        refIdent.getTypeList().forEach(type -> renderNomType(type, holder));
                    }
                });
            }
            if (expr.getExprNoRecur().getStructfielddecls() != null) {
                expr.getExprNoRecur().getStructfielddecls().getStructfielddeclList().forEach(structfielddecl -> {
                    renderNomStructfielddecl(structfielddecl, holder);
                });
            }
            //structfielddecl
            if (expr.getExprNoRecur().getStructfielddecl() != null) {
                renderNomStructfielddecl(expr.getExprNoRecur().getStructfielddecl(), holder);
            }
            //method call of a local class method
            if (!expr.getExprPrimeList().isEmpty()) {
                NomExprPrime exprPrime = expr.getExprPrimeList().get(0);
                if (exprPrime.getFirstChild().textMatches("(")) {
                    NomIdent nameEle = expr.getExprNoRecur().getIdent();
                    assert nameEle != null;
                    String methName = nameEle.getText();
                    renderNode(nameEle, holder, NomSyntaxHighlighter.METHODNAME);

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
                        //could be expr in expr
                        if (expr.getParent() instanceof NomExprNoRecur exprNoRecur) {
                            if (exprNoRecur.getStructfielddecl() != null) {
                                return;
                            }
                        }
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
                    renderNode(exprPrime.getRefident().getIdent(), holder, NomSyntaxHighlighter.INVOKEFUNCTION);
                }
            }
        }

        //assignment
        if (psiElement instanceof NomStmt nomStmt) {
            renderNomDtype(nomStmt.getDtype(), holder);
        }
    }

    private void renderNode(PsiElement node, AnnotationHolder holder, TextAttributesKey key) {
        if (node == null) return;
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(node)
                .textAttributes(key).create();
    }

    private void renderNomStructfielddecl(NomStructfielddecl node, AnnotationHolder holder) {
        renderNode(node.getIdent(), holder, NomSyntaxHighlighter.METHODNAME);
        renderNomDtype(node.getDtype(), holder);
    }

    private void renderNomType(NomType node, AnnotationHolder holder) {
        if (node == null) return;
        NomTypeConst typeConst = null;
        if (node.getTypeConst() != null) {
            typeConst = node.getTypeConst();
        } else if (node.getTypePrime() != null) {
            NomTypePrime typePrime = node.getTypePrime();
            typeConst = typePrime.getTypeConst();
        }
        if (typeConst == null) return;
        renderNomCType(typeConst.getCtype(), holder);
        renderNomType(typeConst.getType(), holder);
    }

    private void renderNomDtype(NomDtype node, AnnotationHolder holder) {
        if (node == null) return;
        renderNomType(node.getType(), holder);
    }

    private void renderNomCType(NomCtype node, AnnotationHolder holder) {
        if (node == null) return;
        List<NomTypeident> lst = node.getTypeqname().getTypeidentList();
        for (NomTypeident typeident : lst) {
            renderNomTypeident(typeident, holder);
        }
    }

    private void renderNomTypeident(NomTypeident node, AnnotationHolder holder) {
        if (node == null) return;
        renderNode(node.getIdent(), holder, NomSyntaxHighlighter.TYPEARG);
        for (NomTypearg typeident : node.getTypeargList()) {
            renderNomType(typeident.getType(), holder);
        }
    }
}
