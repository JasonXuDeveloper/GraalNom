// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.nom.ideanom.impl.*;

public interface NomTypes {

  IElementType ADDSUBOP = new NomElementType("ADDSUBOP");
  IElementType ARGDECL = new NomElementType("ARGDECL");
  IElementType BLOCK = new NomElementType("BLOCK");
  IElementType CLASSDEF = new NomElementType("CLASSDEF");
  IElementType CONSTRUCTOR = new NomElementType("CONSTRUCTOR");
  IElementType CTYPE = new NomElementType("CTYPE");
  IElementType CTYPE_2 = new NomElementType("CTYPE_2");
  IElementType DECLIDENT = new NomElementType("DECLIDENT");
  IElementType DECLQNAME = new NomElementType("DECLQNAME");
  IElementType DEFAULTEXPR = new NomElementType("DEFAULTEXPR");
  IElementType DIVMULTOP = new NomElementType("DIVMULTOP");
  IElementType DTYPE = new NomElementType("DTYPE");
  IElementType EXPR = new NomElementType("EXPR");
  IElementType EXPR_NO_RECUR = new NomElementType("EXPR_NO_RECUR");
  IElementType EXPR_PRIME = new NomElementType("EXPR_PRIME");
  IElementType FIELDDECL = new NomElementType("FIELDDECL");
  IElementType IDENT = new NomElementType("IDENT");
  IElementType INHERITANCEDECL = new NomElementType("INHERITANCEDECL");
  IElementType INSTANCEASSIGNMENT = new NomElementType("INSTANCEASSIGNMENT");
  IElementType INSTANCEDECL = new NomElementType("INSTANCEDECL");
  IElementType INTERFACEDEF = new NomElementType("INTERFACEDEF");
  IElementType LAMBDABODY = new NomElementType("LAMBDABODY");
  IElementType METHDECL = new NomElementType("METHDECL");
  IElementType METHDEF = new NomElementType("METHDEF");
  IElementType NS = new NomElementType("NS");
  IElementType RANGEEXPR = new NomElementType("RANGEEXPR");
  IElementType REFIDENT = new NomElementType("REFIDENT");
  IElementType REFQNAME = new NomElementType("REFQNAME");
  IElementType STATICFIELDDECL = new NomElementType("STATICFIELDDECL");
  IElementType STATICMETHDEF = new NomElementType("STATICMETHDEF");
  IElementType STMT = new NomElementType("STMT");
  IElementType STRUCTASSIGNMENT = new NomElementType("STRUCTASSIGNMENT");
  IElementType STRUCTASSIGNMENTS = new NomElementType("STRUCTASSIGNMENTS");
  IElementType STRUCTFIELDDECL = new NomElementType("STRUCTFIELDDECL");
  IElementType STRUCTFIELDDECLS = new NomElementType("STRUCTFIELDDECLS");
  IElementType TYPE = new NomElementType("TYPE");
  IElementType TYPEARG = new NomElementType("TYPEARG");
  IElementType TYPEARGSPEC = new NomElementType("TYPEARGSPEC");
  IElementType TYPEIDENT = new NomElementType("TYPEIDENT");
  IElementType TYPEIDENT_2 = new NomElementType("TYPEIDENT_2");
  IElementType TYPEQNAME = new NomElementType("TYPEQNAME");
  IElementType TYPEQNAME_2 = new NomElementType("TYPEQNAME_2");
  IElementType TYPE_CONST = new NomElementType("TYPE_CONST");
  IElementType TYPE_PRIME = new NomElementType("TYPE_PRIME");
  IElementType USING = new NomElementType("USING");
  IElementType VISIBILITY = new NomElementType("VISIBILITY");

  IElementType ABSTRACT = new NomTokenType("abstract");
  IElementType AND = new NomTokenType("&&");
  IElementType APPEND = new NomTokenType("++");
  IElementType ARROW = new NomTokenType("->");
  IElementType BANG = new NomTokenType("!");
  IElementType BIGARROW = new NomTokenType("=>");
  IElementType BITAND = new NomTokenType("&");
  IElementType BITOR = new NomTokenType("|");
  IElementType BITXOR = new NomTokenType("^");
  IElementType BREAK = new NomTokenType("break");
  IElementType CALLTARGET = new NomTokenType("calltarget");
  IElementType CLASS = new NomTokenType("class");
  IElementType COLON = new NomTokenType(":");
  IElementType COLONCOLON = new NomTokenType("::");
  IElementType COMMA = new NomTokenType(",");
  IElementType COMMENT = new NomTokenType("COMMENT");
  IElementType CONSTRUCT = new NomTokenType("constructor");
  IElementType CONTINUE = new NomTokenType("continue");
  IElementType DBG = new NomTokenType("DEBUG");
  IElementType DEFAULT = new NomTokenType("default");
  IElementType DIVEQ = new NomTokenType("/=");
  IElementType DOT = new NomTokenType(".");
  IElementType DOTDOT = new NomTokenType("..");
  IElementType DYN = new NomTokenType("dyn");
  IElementType ELSE = new NomTokenType("else");
  IElementType ELSEIF = new NomTokenType("elseif");
  IElementType EQ = new NomTokenType("=");
  IElementType EQEQ = new NomTokenType("==");
  IElementType EQEQEQ = new NomTokenType("===");
  IElementType ERR = new NomTokenType("ERROR");
  IElementType EXPANDO = new NomTokenType("expando");
  IElementType EXTENDS = new NomTokenType("extends");
  IElementType FALSE = new NomTokenType("false");
  IElementType FINAL = new NomTokenType("final");
  IElementType FLOAT = new NomTokenType("FLOAT");
  IElementType FOR = new NomTokenType("for");
  IElementType FOREACH = new NomTokenType("foreach");
  IElementType FUN = new NomTokenType("fun");
  IElementType GEQ = new NomTokenType(">=");
  IElementType ID = new NomTokenType("ID");
  IElementType IF = new NomTokenType("if");
  IElementType IFNULL = new NomTokenType("ifnull");
  IElementType IFOBJ = new NomTokenType("ifobj");
  IElementType IMPLEMENTS = new NomTokenType("implements");
  IElementType IN = new NomTokenType("in");
  IElementType INOUT = new NomTokenType("inout");
  IElementType INSTANCE = new NomTokenType("instance");
  IElementType INT = new NomTokenType("INT");
  IElementType INTERFACE = new NomTokenType("interface");
  IElementType INTERNAL = new NomTokenType("internal");
  IElementType INTERNAL_PROT = new NomTokenType("INTERNAL_PROT");
  IElementType LANGLE = new NomTokenType("<");
  IElementType LBRACE = new NomTokenType("{");
  IElementType LBRACKET = new NomTokenType("[");
  IElementType LEQ = new NomTokenType("<=");
  IElementType LET = new NomTokenType("let");
  IElementType LETVAR = new NomTokenType("letvar");
  IElementType LPAREN = new NomTokenType("(");
  IElementType MATERIAL = new NomTokenType("material");
  IElementType MINUS = new NomTokenType("-");
  IElementType MINUSEQ = new NomTokenType("-=");
  IElementType MULTI = new NomTokenType("multi");
  IElementType NAMESPACE = new NomTokenType("namespace");
  IElementType NEQ = new NomTokenType("!=");
  IElementType NEW = new NomTokenType("new");
  IElementType NUL = new NomTokenType("null");
  IElementType OR = new NomTokenType("||");
  IElementType OUT = new NomTokenType("out");
  IElementType OVERRIDE = new NomTokenType("override");
  IElementType PARTIAL = new NomTokenType("partial");
  IElementType PERCENT = new NomTokenType("%");
  IElementType PLUS = new NomTokenType("+");
  IElementType PLUSEQ = new NomTokenType("+=");
  IElementType POW = new NomTokenType("**");
  IElementType PRIVATE = new NomTokenType("private");
  IElementType PROTECTED = new NomTokenType("protected");
  IElementType PUBLIC = new NomTokenType("public");
  IElementType QMARK = new NomTokenType("?");
  IElementType RANGLE = new NomTokenType(">");
  IElementType RBRACE = new NomTokenType("}");
  IElementType RBRACKET = new NomTokenType("]");
  IElementType READONLY = new NomTokenType("readonly");
  IElementType RETURN = new NomTokenType("return");
  IElementType RPAREN = new NomTokenType(")");
  IElementType RUNTIMECMD = new NomTokenType("RUNTIMECMD");
  IElementType SEMICOLON = new NomTokenType(";");
  IElementType SHAPE = new NomTokenType("shape");
  IElementType SLASH = new NomTokenType("/");
  IElementType STATIC = new NomTokenType("static");
  IElementType STRING = new NomTokenType("STRING");
  IElementType SUPER = new NomTokenType("super");
  IElementType TIMES = new NomTokenType("*");
  IElementType TIMESEQ = new NomTokenType("*=");
  IElementType TRUE = new NomTokenType("true");
  IElementType TYPE_PRIME_3_0 = new NomTokenType("type_prime_3_0");
  IElementType USING_TKN = new NomTokenType("using");
  IElementType VIRTUAL = new NomTokenType("virtual");
  IElementType WHILE = new NomTokenType("while");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ADDSUBOP) {
        return new NomAddsubopImpl(node);
      }
      else if (type == ARGDECL) {
        return new NomArgdeclImpl(node);
      }
      else if (type == BLOCK) {
        return new NomBlockImpl(node);
      }
      else if (type == CLASSDEF) {
        return new NomClassdefImpl(node);
      }
      else if (type == CONSTRUCTOR) {
        return new NomConstructorImpl(node);
      }
      else if (type == CTYPE) {
        return new NomCtypeImpl(node);
      }
      else if (type == CTYPE_2) {
        return new NomCtype2Impl(node);
      }
      else if (type == DECLIDENT) {
        return new NomDeclidentImpl(node);
      }
      else if (type == DECLQNAME) {
        return new NomDeclqnameImpl(node);
      }
      else if (type == DEFAULTEXPR) {
        return new NomDefaultexprImpl(node);
      }
      else if (type == DIVMULTOP) {
        return new NomDivmultopImpl(node);
      }
      else if (type == DTYPE) {
        return new NomDtypeImpl(node);
      }
      else if (type == EXPR) {
        return new NomExprImpl(node);
      }
      else if (type == EXPR_NO_RECUR) {
        return new NomExprNoRecurImpl(node);
      }
      else if (type == EXPR_PRIME) {
        return new NomExprPrimeImpl(node);
      }
      else if (type == FIELDDECL) {
        return new NomFielddeclImpl(node);
      }
      else if (type == IDENT) {
        return new NomIdentImpl(node);
      }
      else if (type == INHERITANCEDECL) {
        return new NomInheritancedeclImpl(node);
      }
      else if (type == INSTANCEASSIGNMENT) {
        return new NomInstanceassignmentImpl(node);
      }
      else if (type == INSTANCEDECL) {
        return new NomInstancedeclImpl(node);
      }
      else if (type == INTERFACEDEF) {
        return new NomInterfacedefImpl(node);
      }
      else if (type == LAMBDABODY) {
        return new NomLambdabodyImpl(node);
      }
      else if (type == METHDECL) {
        return new NomMethdeclImpl(node);
      }
      else if (type == METHDEF) {
        return new NomMethdefImpl(node);
      }
      else if (type == NS) {
        return new NomNsImpl(node);
      }
      else if (type == RANGEEXPR) {
        return new NomRangeexprImpl(node);
      }
      else if (type == REFIDENT) {
        return new NomRefidentImpl(node);
      }
      else if (type == REFQNAME) {
        return new NomRefqnameImpl(node);
      }
      else if (type == STATICFIELDDECL) {
        return new NomStaticfielddeclImpl(node);
      }
      else if (type == STATICMETHDEF) {
        return new NomStaticmethdefImpl(node);
      }
      else if (type == STMT) {
        return new NomStmtImpl(node);
      }
      else if (type == STRUCTASSIGNMENT) {
        return new NomStructassignmentImpl(node);
      }
      else if (type == STRUCTASSIGNMENTS) {
        return new NomStructassignmentsImpl(node);
      }
      else if (type == STRUCTFIELDDECL) {
        return new NomStructfielddeclImpl(node);
      }
      else if (type == STRUCTFIELDDECLS) {
        return new NomStructfielddeclsImpl(node);
      }
      else if (type == TYPE) {
        return new NomTypeImpl(node);
      }
      else if (type == TYPEARG) {
        return new NomTypeargImpl(node);
      }
      else if (type == TYPEARGSPEC) {
        return new NomTypeargspecImpl(node);
      }
      else if (type == TYPEIDENT) {
        return new NomTypeidentImpl(node);
      }
      else if (type == TYPEIDENT_2) {
        return new NomTypeident2Impl(node);
      }
      else if (type == TYPEQNAME) {
        return new NomTypeqnameImpl(node);
      }
      else if (type == TYPEQNAME_2) {
        return new NomTypeqname2Impl(node);
      }
      else if (type == TYPE_CONST) {
        return new NomTypeConstImpl(node);
      }
      else if (type == TYPE_PRIME) {
        return new NomTypePrimeImpl(node);
      }
      else if (type == USING) {
        return new NomUsingImpl(node);
      }
      else if (type == VISIBILITY) {
        return new NomVisibilityImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
