// This is a generated file. Not intended for manual editing.
package com.nom.ideanom;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.nom.ideanom.NomTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class NomParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return nomFile(b, l + 1);
  }

  /* ********************************************************** */
  // PLUS | MINUS
  public static boolean addsubop(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "addsubop")) return false;
    if (!nextTokenIs(b, "<addsubop>", MINUS, PLUS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ADDSUBOP, "<addsubop>");
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // dtype ident
  // | ident
  public static boolean argdecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argdecl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGDECL, "<argdecl>");
    r = argdecl_0(b, l + 1);
    if (!r) r = ident(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // dtype ident
  private static boolean argdecl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argdecl_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = dtype(b, l + 1);
    r = r && ident(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LBRACE (stmt)* RBRACE
  public static boolean block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && block_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, BLOCK, r);
    return r;
  }

  // (stmt)*
  private static boolean block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!block_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "block_1", c)) break;
    }
    return true;
  }

  // (stmt)
  private static boolean block_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = stmt(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (FINAL | visibility | ABSTRACT | PARTIAL | MATERIAL | SHAPE)* CLASS ID (EXTENDS inheritancedecl)? (IMPLEMENTS inheritancedecl (COMMA inheritancedecl)*)? LBRACE (methdef | fielddecl | constructor | staticmethdef | staticfielddecl | classdef | interfacedef | instancedecl)* RBRACE
  public static boolean classdef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classdef")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASSDEF, "<classdef>");
    r = classdef_0(b, l + 1);
    r = r && consumeTokens(b, 0, CLASS, ID);
    r = r && classdef_3(b, l + 1);
    r = r && classdef_4(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && classdef_6(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (FINAL | visibility | ABSTRACT | PARTIAL | MATERIAL | SHAPE)*
  private static boolean classdef_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classdef_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!classdef_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "classdef_0", c)) break;
    }
    return true;
  }

  // FINAL | visibility | ABSTRACT | PARTIAL | MATERIAL | SHAPE
  private static boolean classdef_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classdef_0_0")) return false;
    boolean r;
    r = consumeToken(b, FINAL);
    if (!r) r = visibility(b, l + 1);
    if (!r) r = consumeToken(b, ABSTRACT);
    if (!r) r = consumeToken(b, PARTIAL);
    if (!r) r = consumeToken(b, MATERIAL);
    if (!r) r = consumeToken(b, SHAPE);
    return r;
  }

  // (EXTENDS inheritancedecl)?
  private static boolean classdef_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classdef_3")) return false;
    classdef_3_0(b, l + 1);
    return true;
  }

  // EXTENDS inheritancedecl
  private static boolean classdef_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classdef_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXTENDS);
    r = r && inheritancedecl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (IMPLEMENTS inheritancedecl (COMMA inheritancedecl)*)?
  private static boolean classdef_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classdef_4")) return false;
    classdef_4_0(b, l + 1);
    return true;
  }

  // IMPLEMENTS inheritancedecl (COMMA inheritancedecl)*
  private static boolean classdef_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classdef_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IMPLEMENTS);
    r = r && inheritancedecl(b, l + 1);
    r = r && classdef_4_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA inheritancedecl)*
  private static boolean classdef_4_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classdef_4_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!classdef_4_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "classdef_4_0_2", c)) break;
    }
    return true;
  }

  // COMMA inheritancedecl
  private static boolean classdef_4_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classdef_4_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && inheritancedecl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (methdef | fielddecl | constructor | staticmethdef | staticfielddecl | classdef | interfacedef | instancedecl)*
  private static boolean classdef_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classdef_6")) return false;
    while (true) {
      int c = current_position_(b);
      if (!classdef_6_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "classdef_6", c)) break;
    }
    return true;
  }

  // methdef | fielddecl | constructor | staticmethdef | staticfielddecl | classdef | interfacedef | instancedecl
  private static boolean classdef_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classdef_6_0")) return false;
    boolean r;
    r = methdef(b, l + 1);
    if (!r) r = fielddecl(b, l + 1);
    if (!r) r = constructor(b, l + 1);
    if (!r) r = staticmethdef(b, l + 1);
    if (!r) r = staticfielddecl(b, l + 1);
    if (!r) r = classdef(b, l + 1);
    if (!r) r = interfacedef(b, l + 1);
    if (!r) r = instancedecl(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (visibility)? CONSTRUCT LPAREN (argdecl (COMMA argdecl)*)? RPAREN LBRACE stmt* (SUPER LPAREN (expr (COMMA expr)*)? RPAREN SEMICOLON stmt*)? RBRACE
  public static boolean constructor(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONSTRUCTOR, "<constructor>");
    r = constructor_0(b, l + 1);
    r = r && consumeTokens(b, 0, CONSTRUCT, LPAREN);
    r = r && constructor_3(b, l + 1);
    r = r && consumeTokens(b, 0, RPAREN, LBRACE);
    r = r && constructor_6(b, l + 1);
    r = r && constructor_7(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (visibility)?
  private static boolean constructor_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_0")) return false;
    constructor_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean constructor_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (argdecl (COMMA argdecl)*)?
  private static boolean constructor_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_3")) return false;
    constructor_3_0(b, l + 1);
    return true;
  }

  // argdecl (COMMA argdecl)*
  private static boolean constructor_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argdecl(b, l + 1);
    r = r && constructor_3_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA argdecl)*
  private static boolean constructor_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_3_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!constructor_3_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "constructor_3_0_1", c)) break;
    }
    return true;
  }

  // COMMA argdecl
  private static boolean constructor_3_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_3_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && argdecl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // stmt*
  private static boolean constructor_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_6")) return false;
    while (true) {
      int c = current_position_(b);
      if (!stmt(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "constructor_6", c)) break;
    }
    return true;
  }

  // (SUPER LPAREN (expr (COMMA expr)*)? RPAREN SEMICOLON stmt*)?
  private static boolean constructor_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_7")) return false;
    constructor_7_0(b, l + 1);
    return true;
  }

  // SUPER LPAREN (expr (COMMA expr)*)? RPAREN SEMICOLON stmt*
  private static boolean constructor_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SUPER, LPAREN);
    r = r && constructor_7_0_2(b, l + 1);
    r = r && consumeTokens(b, 0, RPAREN, SEMICOLON);
    r = r && constructor_7_0_5(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr (COMMA expr)*)?
  private static boolean constructor_7_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_7_0_2")) return false;
    constructor_7_0_2_0(b, l + 1);
    return true;
  }

  // expr (COMMA expr)*
  private static boolean constructor_7_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_7_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && constructor_7_0_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA expr)*
  private static boolean constructor_7_0_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_7_0_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!constructor_7_0_2_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "constructor_7_0_2_0_1", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean constructor_7_0_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_7_0_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // stmt*
  private static boolean constructor_7_0_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_7_0_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!stmt(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "constructor_7_0_5", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // typeqname
  public static boolean ctype(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ctype")) return false;
    if (!nextTokenIs(b, "<ctype>", COLONCOLON, ID)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CTYPE, "<ctype>");
    r = typeqname(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // typeqname2
  public static boolean ctype2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ctype2")) return false;
    if (!nextTokenIs(b, "<ctype 2>", COLONCOLON, ID)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CTYPE_2, "<ctype 2>");
    r = typeqname2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ident (LANGLE (typeargspec (COMMA typeargspec)*)? RANGLE)?
  public static boolean declident(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declident")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ident(b, l + 1);
    r = r && declident_1(b, l + 1);
    exit_section_(b, m, DECLIDENT, r);
    return r;
  }

  // (LANGLE (typeargspec (COMMA typeargspec)*)? RANGLE)?
  private static boolean declident_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declident_1")) return false;
    declident_1_0(b, l + 1);
    return true;
  }

  // LANGLE (typeargspec (COMMA typeargspec)*)? RANGLE
  private static boolean declident_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declident_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LANGLE);
    r = r && declident_1_0_1(b, l + 1);
    r = r && consumeToken(b, RANGLE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (typeargspec (COMMA typeargspec)*)?
  private static boolean declident_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declident_1_0_1")) return false;
    declident_1_0_1_0(b, l + 1);
    return true;
  }

  // typeargspec (COMMA typeargspec)*
  private static boolean declident_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declident_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = typeargspec(b, l + 1);
    r = r && declident_1_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA typeargspec)*
  private static boolean declident_1_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declident_1_0_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!declident_1_0_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "declident_1_0_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA typeargspec
  private static boolean declident_1_0_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declident_1_0_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && typeargspec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (declident DOT)* declident
  public static boolean declqname(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declqname")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = declqname_0(b, l + 1);
    r = r && declident(b, l + 1);
    exit_section_(b, m, DECLQNAME, r);
    return r;
  }

  // (declident DOT)*
  private static boolean declqname_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declqname_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!declqname_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "declqname_0", c)) break;
    }
    return true;
  }

  // declident DOT
  private static boolean declqname_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declqname_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = declident(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // STRING | INT | MINUS INT | FLOAT | TRUE | FALSE
  // | NUL | ctype COLONCOLON ident (LBRACKET (defaultexpr (COMMA defaultexpr)*)? RBRACKET)?
  // | LBRACKET (defaultexpr (COMMA defaultexpr)*)? RBRACKET
  // | ident
  public static boolean defaultexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DEFAULTEXPR, "<defaultexpr>");
    r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = parseTokens(b, 0, MINUS, INT);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, NUL);
    if (!r) r = defaultexpr_7(b, l + 1);
    if (!r) r = defaultexpr_8(b, l + 1);
    if (!r) r = ident(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ctype COLONCOLON ident (LBRACKET (defaultexpr (COMMA defaultexpr)*)? RBRACKET)?
  private static boolean defaultexpr_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ctype(b, l + 1);
    r = r && consumeToken(b, COLONCOLON);
    r = r && ident(b, l + 1);
    r = r && defaultexpr_7_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (LBRACKET (defaultexpr (COMMA defaultexpr)*)? RBRACKET)?
  private static boolean defaultexpr_7_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_7_3")) return false;
    defaultexpr_7_3_0(b, l + 1);
    return true;
  }

  // LBRACKET (defaultexpr (COMMA defaultexpr)*)? RBRACKET
  private static boolean defaultexpr_7_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_7_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && defaultexpr_7_3_0_1(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // (defaultexpr (COMMA defaultexpr)*)?
  private static boolean defaultexpr_7_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_7_3_0_1")) return false;
    defaultexpr_7_3_0_1_0(b, l + 1);
    return true;
  }

  // defaultexpr (COMMA defaultexpr)*
  private static boolean defaultexpr_7_3_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_7_3_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = defaultexpr(b, l + 1);
    r = r && defaultexpr_7_3_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA defaultexpr)*
  private static boolean defaultexpr_7_3_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_7_3_0_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!defaultexpr_7_3_0_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "defaultexpr_7_3_0_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA defaultexpr
  private static boolean defaultexpr_7_3_0_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_7_3_0_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && defaultexpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LBRACKET (defaultexpr (COMMA defaultexpr)*)? RBRACKET
  private static boolean defaultexpr_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_8")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && defaultexpr_8_1(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // (defaultexpr (COMMA defaultexpr)*)?
  private static boolean defaultexpr_8_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_8_1")) return false;
    defaultexpr_8_1_0(b, l + 1);
    return true;
  }

  // defaultexpr (COMMA defaultexpr)*
  private static boolean defaultexpr_8_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_8_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = defaultexpr(b, l + 1);
    r = r && defaultexpr_8_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA defaultexpr)*
  private static boolean defaultexpr_8_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_8_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!defaultexpr_8_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "defaultexpr_8_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA defaultexpr
  private static boolean defaultexpr_8_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultexpr_8_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && defaultexpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TIMES | SLASH
  public static boolean divmultop(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "divmultop")) return false;
    if (!nextTokenIs(b, "<divmultop>", SLASH, TIMES)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DIVMULTOP, "<divmultop>");
    r = consumeToken(b, TIMES);
    if (!r) r = consumeToken(b, SLASH);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // type
  public static boolean dtype(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dtype")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DTYPE, "<dtype>");
    r = type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expr_no_recur expr_prime*
  public static boolean expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPR, "<expr>");
    r = expr_no_recur(b, l + 1);
    r = r && expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // expr_prime*
  private static boolean expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_prime(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // NEW refqname LPAREN (expr (COMMA expr)*)? RPAREN
  // | NEW structfielddecls LBRACE (methdef)* structassignments RBRACE
  // | LPAREN dtype RPAREN expr
  // | LPAREN expr RPAREN
  // | rangeexpr
  // | ctype COLONCOLON ident LPAREN (expr (COMMA expr)*)? RPAREN
  // | LBRACE (expr (COMMA expr)*)? RBRACE
  // | (ident? COLONCOLON)? ident LANGLE (type (COMMA type)*)? RANGLE
  // | STRING
  // | INT
  // | MINUS INT
  // | FLOAT
  // | MINUS FLOAT
  // | TRUE
  // | FALSE
  // | NUL
  // | BANG expr
  // | ctype COLONCOLON ident (LBRACKET (expr (COMMA expr)*)? RBRACKET)?
  // | LBRACKET (expr (COMMA expr)*)? RBRACKET
  // | ident
  // | LET structfielddecl IN expr
  // | LETVAR ident EQ expr IN expr
  // | argdecl BIGARROW lambdabody (COLON dtype)?
  // | LPAREN (argdecl (COMMA argdecl)*)? RPAREN BIGARROW lambdabody (COLON dtype)?
  // | MINUS expr
  public static boolean expr_no_recur(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPR_NO_RECUR, "<expr no recur>");
    r = expr_no_recur_0(b, l + 1);
    if (!r) r = expr_no_recur_1(b, l + 1);
    if (!r) r = expr_no_recur_2(b, l + 1);
    if (!r) r = expr_no_recur_3(b, l + 1);
    if (!r) r = rangeexpr(b, l + 1);
    if (!r) r = expr_no_recur_5(b, l + 1);
    if (!r) r = expr_no_recur_6(b, l + 1);
    if (!r) r = expr_no_recur_7(b, l + 1);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = parseTokens(b, 0, MINUS, INT);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = parseTokens(b, 0, MINUS, FLOAT);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, NUL);
    if (!r) r = expr_no_recur_16(b, l + 1);
    if (!r) r = expr_no_recur_17(b, l + 1);
    if (!r) r = expr_no_recur_18(b, l + 1);
    if (!r) r = ident(b, l + 1);
    if (!r) r = expr_no_recur_20(b, l + 1);
    if (!r) r = expr_no_recur_21(b, l + 1);
    if (!r) r = expr_no_recur_22(b, l + 1);
    if (!r) r = expr_no_recur_23(b, l + 1);
    if (!r) r = expr_no_recur_24(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // NEW refqname LPAREN (expr (COMMA expr)*)? RPAREN
  private static boolean expr_no_recur_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NEW);
    r = r && refqname(b, l + 1);
    r = r && consumeToken(b, LPAREN);
    r = r && expr_no_recur_0_3(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr (COMMA expr)*)?
  private static boolean expr_no_recur_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_0_3")) return false;
    expr_no_recur_0_3_0(b, l + 1);
    return true;
  }

  // expr (COMMA expr)*
  private static boolean expr_no_recur_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && expr_no_recur_0_3_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA expr)*
  private static boolean expr_no_recur_0_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_0_3_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_no_recur_0_3_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_no_recur_0_3_0_1", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean expr_no_recur_0_3_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_0_3_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NEW structfielddecls LBRACE (methdef)* structassignments RBRACE
  private static boolean expr_no_recur_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NEW);
    r = r && structfielddecls(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && expr_no_recur_1_3(b, l + 1);
    r = r && structassignments(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (methdef)*
  private static boolean expr_no_recur_1_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_1_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_no_recur_1_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_no_recur_1_3", c)) break;
    }
    return true;
  }

  // (methdef)
  private static boolean expr_no_recur_1_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_1_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = methdef(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN dtype RPAREN expr
  private static boolean expr_no_recur_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && dtype(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN expr RPAREN
  private static boolean expr_no_recur_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // ctype COLONCOLON ident LPAREN (expr (COMMA expr)*)? RPAREN
  private static boolean expr_no_recur_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ctype(b, l + 1);
    r = r && consumeToken(b, COLONCOLON);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, LPAREN);
    r = r && expr_no_recur_5_4(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr (COMMA expr)*)?
  private static boolean expr_no_recur_5_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_5_4")) return false;
    expr_no_recur_5_4_0(b, l + 1);
    return true;
  }

  // expr (COMMA expr)*
  private static boolean expr_no_recur_5_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_5_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && expr_no_recur_5_4_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA expr)*
  private static boolean expr_no_recur_5_4_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_5_4_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_no_recur_5_4_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_no_recur_5_4_0_1", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean expr_no_recur_5_4_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_5_4_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LBRACE (expr (COMMA expr)*)? RBRACE
  private static boolean expr_no_recur_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && expr_no_recur_6_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr (COMMA expr)*)?
  private static boolean expr_no_recur_6_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_6_1")) return false;
    expr_no_recur_6_1_0(b, l + 1);
    return true;
  }

  // expr (COMMA expr)*
  private static boolean expr_no_recur_6_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_6_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && expr_no_recur_6_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA expr)*
  private static boolean expr_no_recur_6_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_6_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_no_recur_6_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_no_recur_6_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean expr_no_recur_6_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_6_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ident? COLONCOLON)? ident LANGLE (type (COMMA type)*)? RANGLE
  private static boolean expr_no_recur_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr_no_recur_7_0(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, LANGLE);
    r = r && expr_no_recur_7_3(b, l + 1);
    r = r && consumeToken(b, RANGLE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ident? COLONCOLON)?
  private static boolean expr_no_recur_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_7_0")) return false;
    expr_no_recur_7_0_0(b, l + 1);
    return true;
  }

  // ident? COLONCOLON
  private static boolean expr_no_recur_7_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_7_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr_no_recur_7_0_0_0(b, l + 1);
    r = r && consumeToken(b, COLONCOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // ident?
  private static boolean expr_no_recur_7_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_7_0_0_0")) return false;
    ident(b, l + 1);
    return true;
  }

  // (type (COMMA type)*)?
  private static boolean expr_no_recur_7_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_7_3")) return false;
    expr_no_recur_7_3_0(b, l + 1);
    return true;
  }

  // type (COMMA type)*
  private static boolean expr_no_recur_7_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_7_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type(b, l + 1);
    r = r && expr_no_recur_7_3_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA type)*
  private static boolean expr_no_recur_7_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_7_3_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_no_recur_7_3_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_no_recur_7_3_0_1", c)) break;
    }
    return true;
  }

  // COMMA type
  private static boolean expr_no_recur_7_3_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_7_3_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // BANG expr
  private static boolean expr_no_recur_16(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_16")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BANG);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ctype COLONCOLON ident (LBRACKET (expr (COMMA expr)*)? RBRACKET)?
  private static boolean expr_no_recur_17(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_17")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ctype(b, l + 1);
    r = r && consumeToken(b, COLONCOLON);
    r = r && ident(b, l + 1);
    r = r && expr_no_recur_17_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (LBRACKET (expr (COMMA expr)*)? RBRACKET)?
  private static boolean expr_no_recur_17_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_17_3")) return false;
    expr_no_recur_17_3_0(b, l + 1);
    return true;
  }

  // LBRACKET (expr (COMMA expr)*)? RBRACKET
  private static boolean expr_no_recur_17_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_17_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && expr_no_recur_17_3_0_1(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr (COMMA expr)*)?
  private static boolean expr_no_recur_17_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_17_3_0_1")) return false;
    expr_no_recur_17_3_0_1_0(b, l + 1);
    return true;
  }

  // expr (COMMA expr)*
  private static boolean expr_no_recur_17_3_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_17_3_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && expr_no_recur_17_3_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA expr)*
  private static boolean expr_no_recur_17_3_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_17_3_0_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_no_recur_17_3_0_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_no_recur_17_3_0_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean expr_no_recur_17_3_0_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_17_3_0_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LBRACKET (expr (COMMA expr)*)? RBRACKET
  private static boolean expr_no_recur_18(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_18")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && expr_no_recur_18_1(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr (COMMA expr)*)?
  private static boolean expr_no_recur_18_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_18_1")) return false;
    expr_no_recur_18_1_0(b, l + 1);
    return true;
  }

  // expr (COMMA expr)*
  private static boolean expr_no_recur_18_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_18_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && expr_no_recur_18_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA expr)*
  private static boolean expr_no_recur_18_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_18_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_no_recur_18_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_no_recur_18_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean expr_no_recur_18_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_18_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LET structfielddecl IN expr
  private static boolean expr_no_recur_20(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_20")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LET);
    r = r && structfielddecl(b, l + 1);
    r = r && consumeToken(b, IN);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LETVAR ident EQ expr IN expr
  private static boolean expr_no_recur_21(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_21")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LETVAR);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, IN);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // argdecl BIGARROW lambdabody (COLON dtype)?
  private static boolean expr_no_recur_22(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_22")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argdecl(b, l + 1);
    r = r && consumeToken(b, BIGARROW);
    r = r && lambdabody(b, l + 1);
    r = r && expr_no_recur_22_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COLON dtype)?
  private static boolean expr_no_recur_22_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_22_3")) return false;
    expr_no_recur_22_3_0(b, l + 1);
    return true;
  }

  // COLON dtype
  private static boolean expr_no_recur_22_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_22_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && dtype(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN (argdecl (COMMA argdecl)*)? RPAREN BIGARROW lambdabody (COLON dtype)?
  private static boolean expr_no_recur_23(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_23")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && expr_no_recur_23_1(b, l + 1);
    r = r && consumeTokens(b, 0, RPAREN, BIGARROW);
    r = r && lambdabody(b, l + 1);
    r = r && expr_no_recur_23_5(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (argdecl (COMMA argdecl)*)?
  private static boolean expr_no_recur_23_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_23_1")) return false;
    expr_no_recur_23_1_0(b, l + 1);
    return true;
  }

  // argdecl (COMMA argdecl)*
  private static boolean expr_no_recur_23_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_23_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argdecl(b, l + 1);
    r = r && expr_no_recur_23_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA argdecl)*
  private static boolean expr_no_recur_23_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_23_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_no_recur_23_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_no_recur_23_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA argdecl
  private static boolean expr_no_recur_23_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_23_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && argdecl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COLON dtype)?
  private static boolean expr_no_recur_23_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_23_5")) return false;
    expr_no_recur_23_5_0(b, l + 1);
    return true;
  }

  // COLON dtype
  private static boolean expr_no_recur_23_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_23_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && dtype(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // MINUS expr
  private static boolean expr_no_recur_24(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_no_recur_24")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MINUS);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DOT refident
  // | LPAREN (expr (COMMA expr)*)? RPAREN
  // | POW expr
  // | divmultop expr
  // | PERCENT expr
  // | addsubop expr
  // | BITAND expr
  // | BITXOR expr
  // | BITOR expr
  // | APPEND expr
  // | LANGLE expr
  // | RANGLE expr
  // | LEQ expr
  // | GEQ expr
  // | EQEQ expr
  // | NEQ expr
  // | AND expr
  // | OR expr
  // | EQEQEQ
  public static boolean expr_prime(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPR_PRIME, "<expr prime>");
    r = expr_prime_0(b, l + 1);
    if (!r) r = expr_prime_1(b, l + 1);
    if (!r) r = expr_prime_2(b, l + 1);
    if (!r) r = expr_prime_3(b, l + 1);
    if (!r) r = expr_prime_4(b, l + 1);
    if (!r) r = expr_prime_5(b, l + 1);
    if (!r) r = expr_prime_6(b, l + 1);
    if (!r) r = expr_prime_7(b, l + 1);
    if (!r) r = expr_prime_8(b, l + 1);
    if (!r) r = expr_prime_9(b, l + 1);
    if (!r) r = expr_prime_10(b, l + 1);
    if (!r) r = expr_prime_11(b, l + 1);
    if (!r) r = expr_prime_12(b, l + 1);
    if (!r) r = expr_prime_13(b, l + 1);
    if (!r) r = expr_prime_14(b, l + 1);
    if (!r) r = expr_prime_15(b, l + 1);
    if (!r) r = expr_prime_16(b, l + 1);
    if (!r) r = expr_prime_17(b, l + 1);
    if (!r) r = consumeToken(b, EQEQEQ);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // DOT refident
  private static boolean expr_prime_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && refident(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN (expr (COMMA expr)*)? RPAREN
  private static boolean expr_prime_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && expr_prime_1_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr (COMMA expr)*)?
  private static boolean expr_prime_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_1_1")) return false;
    expr_prime_1_1_0(b, l + 1);
    return true;
  }

  // expr (COMMA expr)*
  private static boolean expr_prime_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && expr_prime_1_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA expr)*
  private static boolean expr_prime_1_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_1_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_prime_1_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_prime_1_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean expr_prime_1_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_1_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // POW expr
  private static boolean expr_prime_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, POW);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // divmultop expr
  private static boolean expr_prime_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = divmultop(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERCENT expr
  private static boolean expr_prime_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERCENT);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // addsubop expr
  private static boolean expr_prime_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = addsubop(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // BITAND expr
  private static boolean expr_prime_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BITAND);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // BITXOR expr
  private static boolean expr_prime_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BITXOR);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // BITOR expr
  private static boolean expr_prime_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_8")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BITOR);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // APPEND expr
  private static boolean expr_prime_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_9")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, APPEND);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LANGLE expr
  private static boolean expr_prime_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_10")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LANGLE);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // RANGLE expr
  private static boolean expr_prime_11(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_11")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RANGLE);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LEQ expr
  private static boolean expr_prime_12(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_12")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LEQ);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // GEQ expr
  private static boolean expr_prime_13(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_13")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, GEQ);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EQEQ expr
  private static boolean expr_prime_14(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_14")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQEQ);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NEQ expr
  private static boolean expr_prime_15(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_15")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NEQ);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // AND expr
  private static boolean expr_prime_16(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_16")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AND);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OR expr
  private static boolean expr_prime_17(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_prime_17")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OR);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (visibility)? (READONLY)? dtype ident SEMICOLON
  // | (visibility)? (READONLY)? ident SEMICOLON
  // | (visibility)? (READONLY)? dtype ident EQ expr SEMICOLON
  // | (visibility)? (READONLY)? ident EQ expr SEMICOLON
  public static boolean fielddecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELDDECL, "<fielddecl>");
    r = fielddecl_0(b, l + 1);
    if (!r) r = fielddecl_1(b, l + 1);
    if (!r) r = fielddecl_2(b, l + 1);
    if (!r) r = fielddecl_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (visibility)? (READONLY)? dtype ident SEMICOLON
  private static boolean fielddecl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = fielddecl_0_0(b, l + 1);
    r = r && fielddecl_0_1(b, l + 1);
    r = r && dtype(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (visibility)?
  private static boolean fielddecl_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_0_0")) return false;
    fielddecl_0_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean fielddecl_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (READONLY)?
  private static boolean fielddecl_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_0_1")) return false;
    consumeToken(b, READONLY);
    return true;
  }

  // (visibility)? (READONLY)? ident SEMICOLON
  private static boolean fielddecl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = fielddecl_1_0(b, l + 1);
    r = r && fielddecl_1_1(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (visibility)?
  private static boolean fielddecl_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_1_0")) return false;
    fielddecl_1_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean fielddecl_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (READONLY)?
  private static boolean fielddecl_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_1_1")) return false;
    consumeToken(b, READONLY);
    return true;
  }

  // (visibility)? (READONLY)? dtype ident EQ expr SEMICOLON
  private static boolean fielddecl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = fielddecl_2_0(b, l + 1);
    r = r && fielddecl_2_1(b, l + 1);
    r = r && dtype(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (visibility)?
  private static boolean fielddecl_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_2_0")) return false;
    fielddecl_2_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean fielddecl_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (READONLY)?
  private static boolean fielddecl_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_2_1")) return false;
    consumeToken(b, READONLY);
    return true;
  }

  // (visibility)? (READONLY)? ident EQ expr SEMICOLON
  private static boolean fielddecl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = fielddecl_3_0(b, l + 1);
    r = r && fielddecl_3_1(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (visibility)?
  private static boolean fielddecl_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_3_0")) return false;
    fielddecl_3_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean fielddecl_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_3_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (READONLY)?
  private static boolean fielddecl_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fielddecl_3_1")) return false;
    consumeToken(b, READONLY);
    return true;
  }

  /* ********************************************************** */
  // ID
  public static boolean ident(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ident")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, IDENT, r);
    return r;
  }

  /* ********************************************************** */
  // refqname
  public static boolean inheritancedecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inheritancedecl")) return false;
    if (!nextTokenIs(b, "<inheritancedecl>", COLONCOLON, ID)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INHERITANCEDECL, "<inheritancedecl>");
    r = refqname(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ident EQ defaultexpr SEMICOLON
  public static boolean instanceassignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instanceassignment")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ident(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && defaultexpr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, INSTANCEASSIGNMENT, r);
    return r;
  }

  /* ********************************************************** */
  // (visibility)? (DEFAULT)? (MULTI)? INSTANCE ident (EXTENDS ident (LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN)?)? LBRACE (instanceassignment)* RBRACE
  // | (visibility)? MULTI INSTANCE ident LPAREN (argdecl (COMMA argdecl)*)? RPAREN (EXTENDS ident (LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN)?)? LBRACE (instanceassignment)* RBRACE
  public static boolean instancedecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INSTANCEDECL, "<instancedecl>");
    r = instancedecl_0(b, l + 1);
    if (!r) r = instancedecl_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (visibility)? (DEFAULT)? (MULTI)? INSTANCE ident (EXTENDS ident (LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN)?)? LBRACE (instanceassignment)* RBRACE
  private static boolean instancedecl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = instancedecl_0_0(b, l + 1);
    r = r && instancedecl_0_1(b, l + 1);
    r = r && instancedecl_0_2(b, l + 1);
    r = r && consumeToken(b, INSTANCE);
    r = r && ident(b, l + 1);
    r = r && instancedecl_0_5(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && instancedecl_0_7(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (visibility)?
  private static boolean instancedecl_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_0")) return false;
    instancedecl_0_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean instancedecl_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (DEFAULT)?
  private static boolean instancedecl_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_1")) return false;
    consumeToken(b, DEFAULT);
    return true;
  }

  // (MULTI)?
  private static boolean instancedecl_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_2")) return false;
    consumeToken(b, MULTI);
    return true;
  }

  // (EXTENDS ident (LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN)?)?
  private static boolean instancedecl_0_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_5")) return false;
    instancedecl_0_5_0(b, l + 1);
    return true;
  }

  // EXTENDS ident (LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN)?
  private static boolean instancedecl_0_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXTENDS);
    r = r && ident(b, l + 1);
    r = r && instancedecl_0_5_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN)?
  private static boolean instancedecl_0_5_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_5_0_2")) return false;
    instancedecl_0_5_0_2_0(b, l + 1);
    return true;
  }

  // LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN
  private static boolean instancedecl_0_5_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_5_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && instancedecl_0_5_0_2_0_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // (defaultexpr (COMMA defaultexpr)*)?
  private static boolean instancedecl_0_5_0_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_5_0_2_0_1")) return false;
    instancedecl_0_5_0_2_0_1_0(b, l + 1);
    return true;
  }

  // defaultexpr (COMMA defaultexpr)*
  private static boolean instancedecl_0_5_0_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_5_0_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = defaultexpr(b, l + 1);
    r = r && instancedecl_0_5_0_2_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA defaultexpr)*
  private static boolean instancedecl_0_5_0_2_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_5_0_2_0_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!instancedecl_0_5_0_2_0_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "instancedecl_0_5_0_2_0_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA defaultexpr
  private static boolean instancedecl_0_5_0_2_0_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_5_0_2_0_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && defaultexpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (instanceassignment)*
  private static boolean instancedecl_0_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_7")) return false;
    while (true) {
      int c = current_position_(b);
      if (!instancedecl_0_7_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "instancedecl_0_7", c)) break;
    }
    return true;
  }

  // (instanceassignment)
  private static boolean instancedecl_0_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_0_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = instanceassignment(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (visibility)? MULTI INSTANCE ident LPAREN (argdecl (COMMA argdecl)*)? RPAREN (EXTENDS ident (LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN)?)? LBRACE (instanceassignment)* RBRACE
  private static boolean instancedecl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = instancedecl_1_0(b, l + 1);
    r = r && consumeTokens(b, 0, MULTI, INSTANCE);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, LPAREN);
    r = r && instancedecl_1_5(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && instancedecl_1_7(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && instancedecl_1_9(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (visibility)?
  private static boolean instancedecl_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_0")) return false;
    instancedecl_1_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean instancedecl_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (argdecl (COMMA argdecl)*)?
  private static boolean instancedecl_1_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_5")) return false;
    instancedecl_1_5_0(b, l + 1);
    return true;
  }

  // argdecl (COMMA argdecl)*
  private static boolean instancedecl_1_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argdecl(b, l + 1);
    r = r && instancedecl_1_5_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA argdecl)*
  private static boolean instancedecl_1_5_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_5_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!instancedecl_1_5_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "instancedecl_1_5_0_1", c)) break;
    }
    return true;
  }

  // COMMA argdecl
  private static boolean instancedecl_1_5_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_5_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && argdecl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (EXTENDS ident (LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN)?)?
  private static boolean instancedecl_1_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_7")) return false;
    instancedecl_1_7_0(b, l + 1);
    return true;
  }

  // EXTENDS ident (LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN)?
  private static boolean instancedecl_1_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXTENDS);
    r = r && ident(b, l + 1);
    r = r && instancedecl_1_7_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN)?
  private static boolean instancedecl_1_7_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_7_0_2")) return false;
    instancedecl_1_7_0_2_0(b, l + 1);
    return true;
  }

  // LPAREN (defaultexpr (COMMA defaultexpr)*)? RPAREN
  private static boolean instancedecl_1_7_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_7_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && instancedecl_1_7_0_2_0_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // (defaultexpr (COMMA defaultexpr)*)?
  private static boolean instancedecl_1_7_0_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_7_0_2_0_1")) return false;
    instancedecl_1_7_0_2_0_1_0(b, l + 1);
    return true;
  }

  // defaultexpr (COMMA defaultexpr)*
  private static boolean instancedecl_1_7_0_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_7_0_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = defaultexpr(b, l + 1);
    r = r && instancedecl_1_7_0_2_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA defaultexpr)*
  private static boolean instancedecl_1_7_0_2_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_7_0_2_0_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!instancedecl_1_7_0_2_0_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "instancedecl_1_7_0_2_0_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA defaultexpr
  private static boolean instancedecl_1_7_0_2_0_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_7_0_2_0_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && defaultexpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (instanceassignment)*
  private static boolean instancedecl_1_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_9")) return false;
    while (true) {
      int c = current_position_(b);
      if (!instancedecl_1_9_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "instancedecl_1_9", c)) break;
    }
    return true;
  }

  // (instanceassignment)
  private static boolean instancedecl_1_9_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instancedecl_1_9_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = instanceassignment(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (visibility | PARTIAL | MATERIAL | SHAPE)* INTERFACE declident (EXTENDS inheritancedecl (COMMA inheritancedecl)*)? LBRACE (methdecl | interfacedef)* RBRACE
  public static boolean interfacedef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interfacedef")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INTERFACEDEF, "<interfacedef>");
    r = interfacedef_0(b, l + 1);
    r = r && consumeToken(b, INTERFACE);
    r = r && declident(b, l + 1);
    r = r && interfacedef_3(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && interfacedef_5(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (visibility | PARTIAL | MATERIAL | SHAPE)*
  private static boolean interfacedef_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interfacedef_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!interfacedef_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "interfacedef_0", c)) break;
    }
    return true;
  }

  // visibility | PARTIAL | MATERIAL | SHAPE
  private static boolean interfacedef_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interfacedef_0_0")) return false;
    boolean r;
    r = visibility(b, l + 1);
    if (!r) r = consumeToken(b, PARTIAL);
    if (!r) r = consumeToken(b, MATERIAL);
    if (!r) r = consumeToken(b, SHAPE);
    return r;
  }

  // (EXTENDS inheritancedecl (COMMA inheritancedecl)*)?
  private static boolean interfacedef_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interfacedef_3")) return false;
    interfacedef_3_0(b, l + 1);
    return true;
  }

  // EXTENDS inheritancedecl (COMMA inheritancedecl)*
  private static boolean interfacedef_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interfacedef_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXTENDS);
    r = r && inheritancedecl(b, l + 1);
    r = r && interfacedef_3_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA inheritancedecl)*
  private static boolean interfacedef_3_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interfacedef_3_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!interfacedef_3_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "interfacedef_3_0_2", c)) break;
    }
    return true;
  }

  // COMMA inheritancedecl
  private static boolean interfacedef_3_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interfacedef_3_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && inheritancedecl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (methdecl | interfacedef)*
  private static boolean interfacedef_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interfacedef_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!interfacedef_5_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "interfacedef_5", c)) break;
    }
    return true;
  }

  // methdecl | interfacedef
  private static boolean interfacedef_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interfacedef_5_0")) return false;
    boolean r;
    r = methdecl(b, l + 1);
    if (!r) r = interfacedef(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // expr | block
  public static boolean lambdabody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lambdabody")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LAMBDABODY, "<lambdabody>");
    r = expr(b, l + 1);
    if (!r) r = block(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (visibility)? (CALLTARGET)? FUN declident LPAREN (argdecl (COMMA argdecl)*)? RPAREN (COLON type)? SEMICOLON
  public static boolean methdecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdecl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, METHDECL, "<methdecl>");
    r = methdecl_0(b, l + 1);
    r = r && methdecl_1(b, l + 1);
    r = r && consumeToken(b, FUN);
    r = r && declident(b, l + 1);
    r = r && consumeToken(b, LPAREN);
    r = r && methdecl_5(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && methdecl_7(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (visibility)?
  private static boolean methdecl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdecl_0")) return false;
    methdecl_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean methdecl_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdecl_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (CALLTARGET)?
  private static boolean methdecl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdecl_1")) return false;
    consumeToken(b, CALLTARGET);
    return true;
  }

  // (argdecl (COMMA argdecl)*)?
  private static boolean methdecl_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdecl_5")) return false;
    methdecl_5_0(b, l + 1);
    return true;
  }

  // argdecl (COMMA argdecl)*
  private static boolean methdecl_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdecl_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argdecl(b, l + 1);
    r = r && methdecl_5_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA argdecl)*
  private static boolean methdecl_5_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdecl_5_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!methdecl_5_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "methdecl_5_0_1", c)) break;
    }
    return true;
  }

  // COMMA argdecl
  private static boolean methdecl_5_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdecl_5_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && argdecl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COLON type)?
  private static boolean methdecl_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdecl_7")) return false;
    methdecl_7_0(b, l + 1);
    return true;
  }

  // COLON type
  private static boolean methdecl_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdecl_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (FINAL | visibility | VIRTUAL | OVERRIDE | CALLTARGET)* FUN declident LPAREN (argdecl (COMMA argdecl)*)? RPAREN (COLON dtype)? block
  public static boolean methdef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdef")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, METHDEF, "<methdef>");
    r = methdef_0(b, l + 1);
    r = r && consumeToken(b, FUN);
    r = r && declident(b, l + 1);
    r = r && consumeToken(b, LPAREN);
    r = r && methdef_4(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && methdef_6(b, l + 1);
    r = r && block(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (FINAL | visibility | VIRTUAL | OVERRIDE | CALLTARGET)*
  private static boolean methdef_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdef_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!methdef_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "methdef_0", c)) break;
    }
    return true;
  }

  // FINAL | visibility | VIRTUAL | OVERRIDE | CALLTARGET
  private static boolean methdef_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdef_0_0")) return false;
    boolean r;
    r = consumeToken(b, FINAL);
    if (!r) r = visibility(b, l + 1);
    if (!r) r = consumeToken(b, VIRTUAL);
    if (!r) r = consumeToken(b, OVERRIDE);
    if (!r) r = consumeToken(b, CALLTARGET);
    return r;
  }

  // (argdecl (COMMA argdecl)*)?
  private static boolean methdef_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdef_4")) return false;
    methdef_4_0(b, l + 1);
    return true;
  }

  // argdecl (COMMA argdecl)*
  private static boolean methdef_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdef_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argdecl(b, l + 1);
    r = r && methdef_4_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA argdecl)*
  private static boolean methdef_4_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdef_4_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!methdef_4_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "methdef_4_0_1", c)) break;
    }
    return true;
  }

  // COMMA argdecl
  private static boolean methdef_4_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdef_4_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && argdecl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COLON dtype)?
  private static boolean methdef_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdef_6")) return false;
    methdef_6_0(b, l + 1);
    return true;
  }

  // COLON dtype
  private static boolean methdef_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methdef_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && dtype(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (using)*(classdef|interfacedef|ns)*
  static boolean nomFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nomFile")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nomFile_0(b, l + 1);
    r = r && nomFile_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (using)*
  private static boolean nomFile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nomFile_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!nomFile_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "nomFile_0", c)) break;
    }
    return true;
  }

  // (using)
  private static boolean nomFile_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nomFile_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = using(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (classdef|interfacedef|ns)*
  private static boolean nomFile_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nomFile_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!nomFile_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "nomFile_1", c)) break;
    }
    return true;
  }

  // classdef|interfacedef|ns
  private static boolean nomFile_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nomFile_1_0")) return false;
    boolean r;
    r = classdef(b, l + 1);
    if (!r) r = interfacedef(b, l + 1);
    if (!r) r = ns(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // NAMESPACE declqname LBRACE (classdef|interfacedef|ns)* RBRACE
  public static boolean ns(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ns")) return false;
    if (!nextTokenIs(b, NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NAMESPACE);
    r = r && declqname(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && ns_3(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, NS, r);
    return r;
  }

  // (classdef|interfacedef|ns)*
  private static boolean ns_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ns_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ns_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ns_3", c)) break;
    }
    return true;
  }

  // classdef|interfacedef|ns
  private static boolean ns_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ns_3_0")) return false;
    boolean r;
    r = classdef(b, l + 1);
    if (!r) r = interfacedef(b, l + 1);
    if (!r) r = ns(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // LBRACKET DOTDOT expr RBRACKET
  // | LBRACKET expr DOTDOT expr RBRACKET
  // | LBRACKET DOTDOT expr COLON expr RBRACKET
  // | LBRACKET expr DOTDOT expr COLON expr RBRACKET
  public static boolean rangeexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rangeexpr")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = rangeexpr_0(b, l + 1);
    if (!r) r = rangeexpr_1(b, l + 1);
    if (!r) r = rangeexpr_2(b, l + 1);
    if (!r) r = rangeexpr_3(b, l + 1);
    exit_section_(b, m, RANGEEXPR, r);
    return r;
  }

  // LBRACKET DOTDOT expr RBRACKET
  private static boolean rangeexpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rangeexpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LBRACKET, DOTDOT);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // LBRACKET expr DOTDOT expr RBRACKET
  private static boolean rangeexpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rangeexpr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, DOTDOT);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // LBRACKET DOTDOT expr COLON expr RBRACKET
  private static boolean rangeexpr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rangeexpr_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LBRACKET, DOTDOT);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // LBRACKET expr DOTDOT expr COLON expr RBRACKET
  private static boolean rangeexpr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rangeexpr_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, DOTDOT);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ident (LANGLE (type (COMMA type)*)? RANGLE)?
  public static boolean refident(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refident")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ident(b, l + 1);
    r = r && refident_1(b, l + 1);
    exit_section_(b, m, REFIDENT, r);
    return r;
  }

  // (LANGLE (type (COMMA type)*)? RANGLE)?
  private static boolean refident_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refident_1")) return false;
    refident_1_0(b, l + 1);
    return true;
  }

  // LANGLE (type (COMMA type)*)? RANGLE
  private static boolean refident_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refident_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LANGLE);
    r = r && refident_1_0_1(b, l + 1);
    r = r && consumeToken(b, RANGLE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (type (COMMA type)*)?
  private static boolean refident_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refident_1_0_1")) return false;
    refident_1_0_1_0(b, l + 1);
    return true;
  }

  // type (COMMA type)*
  private static boolean refident_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refident_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type(b, l + 1);
    r = r && refident_1_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA type)*
  private static boolean refident_1_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refident_1_0_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!refident_1_0_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "refident_1_0_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA type
  private static boolean refident_1_0_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refident_1_0_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // COLONCOLON? (refident DOT)* refident
  public static boolean refqname(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refqname")) return false;
    if (!nextTokenIs(b, "<refqname>", COLONCOLON, ID)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, REFQNAME, "<refqname>");
    r = refqname_0(b, l + 1);
    r = r && refqname_1(b, l + 1);
    r = r && refident(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COLONCOLON?
  private static boolean refqname_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refqname_0")) return false;
    consumeToken(b, COLONCOLON);
    return true;
  }

  // (refident DOT)*
  private static boolean refqname_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refqname_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!refqname_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "refqname_1", c)) break;
    }
    return true;
  }

  // refident DOT
  private static boolean refqname_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "refqname_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = refident(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (visibility)? STATIC (READONLY)? dtype ident SEMICOLON
  // | (visibility)? STATIC (READONLY)? ident SEMICOLON
  // | (visibility)? STATIC (READONLY)? dtype ident EQ defaultexpr SEMICOLON
  // | (visibility)? STATIC (READONLY)? ident EQ defaultexpr SEMICOLON
  public static boolean staticfielddecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATICFIELDDECL, "<staticfielddecl>");
    r = staticfielddecl_0(b, l + 1);
    if (!r) r = staticfielddecl_1(b, l + 1);
    if (!r) r = staticfielddecl_2(b, l + 1);
    if (!r) r = staticfielddecl_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (visibility)? STATIC (READONLY)? dtype ident SEMICOLON
  private static boolean staticfielddecl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = staticfielddecl_0_0(b, l + 1);
    r = r && consumeToken(b, STATIC);
    r = r && staticfielddecl_0_2(b, l + 1);
    r = r && dtype(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (visibility)?
  private static boolean staticfielddecl_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_0_0")) return false;
    staticfielddecl_0_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean staticfielddecl_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (READONLY)?
  private static boolean staticfielddecl_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_0_2")) return false;
    consumeToken(b, READONLY);
    return true;
  }

  // (visibility)? STATIC (READONLY)? ident SEMICOLON
  private static boolean staticfielddecl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = staticfielddecl_1_0(b, l + 1);
    r = r && consumeToken(b, STATIC);
    r = r && staticfielddecl_1_2(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (visibility)?
  private static boolean staticfielddecl_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_1_0")) return false;
    staticfielddecl_1_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean staticfielddecl_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (READONLY)?
  private static boolean staticfielddecl_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_1_2")) return false;
    consumeToken(b, READONLY);
    return true;
  }

  // (visibility)? STATIC (READONLY)? dtype ident EQ defaultexpr SEMICOLON
  private static boolean staticfielddecl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = staticfielddecl_2_0(b, l + 1);
    r = r && consumeToken(b, STATIC);
    r = r && staticfielddecl_2_2(b, l + 1);
    r = r && dtype(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && defaultexpr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (visibility)?
  private static boolean staticfielddecl_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_2_0")) return false;
    staticfielddecl_2_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean staticfielddecl_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (READONLY)?
  private static boolean staticfielddecl_2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_2_2")) return false;
    consumeToken(b, READONLY);
    return true;
  }

  // (visibility)? STATIC (READONLY)? ident EQ defaultexpr SEMICOLON
  private static boolean staticfielddecl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = staticfielddecl_3_0(b, l + 1);
    r = r && consumeToken(b, STATIC);
    r = r && staticfielddecl_3_2(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && defaultexpr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (visibility)?
  private static boolean staticfielddecl_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_3_0")) return false;
    staticfielddecl_3_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean staticfielddecl_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_3_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (READONLY)?
  private static boolean staticfielddecl_3_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticfielddecl_3_2")) return false;
    consumeToken(b, READONLY);
    return true;
  }

  /* ********************************************************** */
  // (visibility)? STATIC FUN declident LPAREN (argdecl (COMMA argdecl)*)? RPAREN (COLON type)? block
  public static boolean staticmethdef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticmethdef")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATICMETHDEF, "<staticmethdef>");
    r = staticmethdef_0(b, l + 1);
    r = r && consumeTokens(b, 0, STATIC, FUN);
    r = r && declident(b, l + 1);
    r = r && consumeToken(b, LPAREN);
    r = r && staticmethdef_5(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && staticmethdef_7(b, l + 1);
    r = r && block(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (visibility)?
  private static boolean staticmethdef_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticmethdef_0")) return false;
    staticmethdef_0_0(b, l + 1);
    return true;
  }

  // (visibility)
  private static boolean staticmethdef_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticmethdef_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = visibility(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (argdecl (COMMA argdecl)*)?
  private static boolean staticmethdef_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticmethdef_5")) return false;
    staticmethdef_5_0(b, l + 1);
    return true;
  }

  // argdecl (COMMA argdecl)*
  private static boolean staticmethdef_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticmethdef_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argdecl(b, l + 1);
    r = r && staticmethdef_5_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA argdecl)*
  private static boolean staticmethdef_5_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticmethdef_5_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!staticmethdef_5_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "staticmethdef_5_0_1", c)) break;
    }
    return true;
  }

  // COMMA argdecl
  private static boolean staticmethdef_5_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticmethdef_5_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && argdecl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COLON type)?
  private static boolean staticmethdef_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticmethdef_7")) return false;
    staticmethdef_7_0(b, l + 1);
    return true;
  }

  // COLON type
  private static boolean staticmethdef_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "staticmethdef_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (expr DOT)? ident EQ expr SEMICOLON
  // | (expr DOT)? ident PLUSEQ expr SEMICOLON
  // | (expr DOT)? ident MINUSEQ expr SEMICOLON
  // | (expr DOT)? ident TIMESEQ expr SEMICOLON
  // | (expr DOT)? ident DIVEQ expr SEMICOLON
  // | dtype ident EQ expr SEMICOLON
  // | dtype ident SEMICOLON
  // | expr SEMICOLON
  // | DBG LPAREN STRING RPAREN SEMICOLON
  // | ERR LPAREN expr RPAREN SEMICOLON
  // | RUNTIMECMD LPAREN STRING RPAREN SEMICOLON
  // | BREAK (INT)? SEMICOLON
  // | CONTINUE (INT)? SEMICOLON
  // | RETURN expr? SEMICOLON
  // | IF LPAREN expr RPAREN block (ELSEIF LPAREN expr RPAREN block)* (ELSE block)?
  // | IFNULL LPAREN ident RPAREN block (ELSE block)?
  // | IFOBJ LPAREN ident RPAREN block (ELSE block)?
  // | IF LPAREN ident COLON type RPAREN block (ELSE block)?
  // | WHILE LPAREN expr RPAREN block
  // | FOREACH LPAREN argdecl IN expr RPAREN block
  public static boolean stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STMT, "<stmt>");
    r = stmt_0(b, l + 1);
    if (!r) r = stmt_1(b, l + 1);
    if (!r) r = stmt_2(b, l + 1);
    if (!r) r = stmt_3(b, l + 1);
    if (!r) r = stmt_4(b, l + 1);
    if (!r) r = stmt_5(b, l + 1);
    if (!r) r = stmt_6(b, l + 1);
    if (!r) r = stmt_7(b, l + 1);
    if (!r) r = parseTokens(b, 0, DBG, LPAREN, STRING, RPAREN, SEMICOLON);
    if (!r) r = stmt_9(b, l + 1);
    if (!r) r = parseTokens(b, 0, RUNTIMECMD, LPAREN, STRING, RPAREN, SEMICOLON);
    if (!r) r = stmt_11(b, l + 1);
    if (!r) r = stmt_12(b, l + 1);
    if (!r) r = stmt_13(b, l + 1);
    if (!r) r = stmt_14(b, l + 1);
    if (!r) r = stmt_15(b, l + 1);
    if (!r) r = stmt_16(b, l + 1);
    if (!r) r = stmt_17(b, l + 1);
    if (!r) r = stmt_18(b, l + 1);
    if (!r) r = stmt_19(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (expr DOT)? ident EQ expr SEMICOLON
  private static boolean stmt_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = stmt_0_0(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr DOT)?
  private static boolean stmt_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_0_0")) return false;
    stmt_0_0_0(b, l + 1);
    return true;
  }

  // expr DOT
  private static boolean stmt_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr DOT)? ident PLUSEQ expr SEMICOLON
  private static boolean stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = stmt_1_0(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, PLUSEQ);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr DOT)?
  private static boolean stmt_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_1_0")) return false;
    stmt_1_0_0(b, l + 1);
    return true;
  }

  // expr DOT
  private static boolean stmt_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr DOT)? ident MINUSEQ expr SEMICOLON
  private static boolean stmt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = stmt_2_0(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, MINUSEQ);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr DOT)?
  private static boolean stmt_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_2_0")) return false;
    stmt_2_0_0(b, l + 1);
    return true;
  }

  // expr DOT
  private static boolean stmt_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr DOT)? ident TIMESEQ expr SEMICOLON
  private static boolean stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = stmt_3_0(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, TIMESEQ);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr DOT)?
  private static boolean stmt_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_3_0")) return false;
    stmt_3_0_0(b, l + 1);
    return true;
  }

  // expr DOT
  private static boolean stmt_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_3_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr DOT)? ident DIVEQ expr SEMICOLON
  private static boolean stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = stmt_4_0(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, DIVEQ);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expr DOT)?
  private static boolean stmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_4_0")) return false;
    stmt_4_0_0(b, l + 1);
    return true;
  }

  // expr DOT
  private static boolean stmt_4_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_4_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // dtype ident EQ expr SEMICOLON
  private static boolean stmt_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = dtype(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // dtype ident SEMICOLON
  private static boolean stmt_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = dtype(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr SEMICOLON
  private static boolean stmt_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // ERR LPAREN expr RPAREN SEMICOLON
  private static boolean stmt_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_9")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ERR, LPAREN);
    r = r && expr(b, l + 1);
    r = r && consumeTokens(b, 0, RPAREN, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // BREAK (INT)? SEMICOLON
  private static boolean stmt_11(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_11")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BREAK);
    r = r && stmt_11_1(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (INT)?
  private static boolean stmt_11_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_11_1")) return false;
    consumeToken(b, INT);
    return true;
  }

  // CONTINUE (INT)? SEMICOLON
  private static boolean stmt_12(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_12")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CONTINUE);
    r = r && stmt_12_1(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // (INT)?
  private static boolean stmt_12_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_12_1")) return false;
    consumeToken(b, INT);
    return true;
  }

  // RETURN expr? SEMICOLON
  private static boolean stmt_13(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_13")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RETURN);
    r = r && stmt_13_1(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr?
  private static boolean stmt_13_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_13_1")) return false;
    expr(b, l + 1);
    return true;
  }

  // IF LPAREN expr RPAREN block (ELSEIF LPAREN expr RPAREN block)* (ELSE block)?
  private static boolean stmt_14(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_14")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IF, LPAREN);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && block(b, l + 1);
    r = r && stmt_14_5(b, l + 1);
    r = r && stmt_14_6(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ELSEIF LPAREN expr RPAREN block)*
  private static boolean stmt_14_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_14_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!stmt_14_5_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "stmt_14_5", c)) break;
    }
    return true;
  }

  // ELSEIF LPAREN expr RPAREN block
  private static boolean stmt_14_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_14_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ELSEIF, LPAREN);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ELSE block)?
  private static boolean stmt_14_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_14_6")) return false;
    stmt_14_6_0(b, l + 1);
    return true;
  }

  // ELSE block
  private static boolean stmt_14_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_14_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE);
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // IFNULL LPAREN ident RPAREN block (ELSE block)?
  private static boolean stmt_15(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_15")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IFNULL, LPAREN);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && block(b, l + 1);
    r = r && stmt_15_5(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ELSE block)?
  private static boolean stmt_15_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_15_5")) return false;
    stmt_15_5_0(b, l + 1);
    return true;
  }

  // ELSE block
  private static boolean stmt_15_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_15_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE);
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // IFOBJ LPAREN ident RPAREN block (ELSE block)?
  private static boolean stmt_16(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_16")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IFOBJ, LPAREN);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && block(b, l + 1);
    r = r && stmt_16_5(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ELSE block)?
  private static boolean stmt_16_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_16_5")) return false;
    stmt_16_5_0(b, l + 1);
    return true;
  }

  // ELSE block
  private static boolean stmt_16_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_16_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE);
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // IF LPAREN ident COLON type RPAREN block (ELSE block)?
  private static boolean stmt_17(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_17")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IF, LPAREN);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && type(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && block(b, l + 1);
    r = r && stmt_17_7(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ELSE block)?
  private static boolean stmt_17_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_17_7")) return false;
    stmt_17_7_0(b, l + 1);
    return true;
  }

  // ELSE block
  private static boolean stmt_17_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_17_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE);
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // WHILE LPAREN expr RPAREN block
  private static boolean stmt_18(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_18")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, WHILE, LPAREN);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // FOREACH LPAREN argdecl IN expr RPAREN block
  private static boolean stmt_19(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_19")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, FOREACH, LPAREN);
    r = r && argdecl(b, l + 1);
    r = r && consumeToken(b, IN);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ident EQ expr
  public static boolean structassignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structassignment")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ident(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1);
    exit_section_(b, m, STRUCTASSIGNMENT, r);
    return r;
  }

  /* ********************************************************** */
  // structassignment (COMMA structassignment)*
  public static boolean structassignments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structassignments")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = structassignment(b, l + 1);
    r = r && structassignments_1(b, l + 1);
    exit_section_(b, m, STRUCTASSIGNMENTS, r);
    return r;
  }

  // (COMMA structassignment)*
  private static boolean structassignments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structassignments_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!structassignments_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "structassignments_1", c)) break;
    }
    return true;
  }

  // COMMA structassignment
  private static boolean structassignments_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structassignments_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && structassignment(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // dtype ident EQ expr
  // | ident EQ expr
  public static boolean structfielddecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structfielddecl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRUCTFIELDDECL, "<structfielddecl>");
    r = structfielddecl_0(b, l + 1);
    if (!r) r = structfielddecl_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // dtype ident EQ expr
  private static boolean structfielddecl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structfielddecl_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = dtype(b, l + 1);
    r = r && ident(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ident EQ expr
  private static boolean structfielddecl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structfielddecl_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ident(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN (structfielddecl (COMMA structfielddecl)*)? RPAREN
  public static boolean structfielddecls(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structfielddecls")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && structfielddecls_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, STRUCTFIELDDECLS, r);
    return r;
  }

  // (structfielddecl (COMMA structfielddecl)*)?
  private static boolean structfielddecls_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structfielddecls_1")) return false;
    structfielddecls_1_0(b, l + 1);
    return true;
  }

  // structfielddecl (COMMA structfielddecl)*
  private static boolean structfielddecls_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structfielddecls_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = structfielddecl(b, l + 1);
    r = r && structfielddecls_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA structfielddecl)*
  private static boolean structfielddecls_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structfielddecls_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!structfielddecls_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "structfielddecls_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA structfielddecl
  private static boolean structfielddecls_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structfielddecls_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && structfielddecl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // type_const | type_prime
  public static boolean type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE, "<type>");
    r = type_const(b, l + 1);
    if (!r) r = type_prime(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ctype | DYN | LPAREN type RPAREN
  public static boolean type_const(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_const")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_CONST, "<type const>");
    r = ctype(b, l + 1);
    if (!r) r = consumeToken(b, DYN);
    if (!r) r = type_const_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LPAREN type RPAREN
  private static boolean type_const_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_const_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && type(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // type_const LBRACKET RBRACKET | type_const BANG | type_const QMARK |
  public static boolean type_prime(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_prime")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_PRIME, "<type prime>");
    r = type_prime_0(b, l + 1);
    if (!r) r = type_prime_1(b, l + 1);
    if (!r) r = type_prime_2(b, l + 1);
    if (!r) r = consumeToken(b, TYPE_PRIME_3_0);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // type_const LBRACKET RBRACKET
  private static boolean type_prime_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_prime_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_const(b, l + 1);
    r = r && consumeTokens(b, 0, LBRACKET, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // type_const BANG
  private static boolean type_prime_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_prime_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_const(b, l + 1);
    r = r && consumeToken(b, BANG);
    exit_section_(b, m, null, r);
    return r;
  }

  // type_const QMARK
  private static boolean type_prime_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_prime_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_const(b, l + 1);
    r = r && consumeToken(b, QMARK);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // type
  public static boolean typearg(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typearg")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPEARG, "<typearg>");
    r = type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ident | ident EXTENDS ctype2
  public static boolean typeargspec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeargspec")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ident(b, l + 1);
    if (!r) r = typeargspec_1(b, l + 1);
    exit_section_(b, m, TYPEARGSPEC, r);
    return r;
  }

  // ident EXTENDS ctype2
  private static boolean typeargspec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeargspec_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ident(b, l + 1);
    r = r && consumeToken(b, EXTENDS);
    r = r && ctype2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ident (LANGLE (typearg (COMMA typearg)*)? RANGLE)?
  public static boolean typeident(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ident(b, l + 1);
    r = r && typeident_1(b, l + 1);
    exit_section_(b, m, TYPEIDENT, r);
    return r;
  }

  // (LANGLE (typearg (COMMA typearg)*)? RANGLE)?
  private static boolean typeident_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident_1")) return false;
    typeident_1_0(b, l + 1);
    return true;
  }

  // LANGLE (typearg (COMMA typearg)*)? RANGLE
  private static boolean typeident_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LANGLE);
    r = r && typeident_1_0_1(b, l + 1);
    r = r && consumeToken(b, RANGLE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (typearg (COMMA typearg)*)?
  private static boolean typeident_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident_1_0_1")) return false;
    typeident_1_0_1_0(b, l + 1);
    return true;
  }

  // typearg (COMMA typearg)*
  private static boolean typeident_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = typearg(b, l + 1);
    r = r && typeident_1_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA typearg)*
  private static boolean typeident_1_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident_1_0_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!typeident_1_0_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "typeident_1_0_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA typearg
  private static boolean typeident_1_0_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident_1_0_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && typearg(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ident (LANGLE (typearg (COMMA typearg)*)? RANGLE)
  public static boolean typeident2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident2")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ident(b, l + 1);
    r = r && typeident2_1(b, l + 1);
    exit_section_(b, m, TYPEIDENT_2, r);
    return r;
  }

  // LANGLE (typearg (COMMA typearg)*)? RANGLE
  private static boolean typeident2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident2_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LANGLE);
    r = r && typeident2_1_1(b, l + 1);
    r = r && consumeToken(b, RANGLE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (typearg (COMMA typearg)*)?
  private static boolean typeident2_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident2_1_1")) return false;
    typeident2_1_1_0(b, l + 1);
    return true;
  }

  // typearg (COMMA typearg)*
  private static boolean typeident2_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident2_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = typearg(b, l + 1);
    r = r && typeident2_1_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA typearg)*
  private static boolean typeident2_1_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident2_1_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!typeident2_1_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "typeident2_1_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA typearg
  private static boolean typeident2_1_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeident2_1_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && typearg(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // COLONCOLON? (typeident DOT)* typeident
  public static boolean typeqname(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeqname")) return false;
    if (!nextTokenIs(b, "<typeqname>", COLONCOLON, ID)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPEQNAME, "<typeqname>");
    r = typeqname_0(b, l + 1);
    r = r && typeqname_1(b, l + 1);
    r = r && typeident(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COLONCOLON?
  private static boolean typeqname_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeqname_0")) return false;
    consumeToken(b, COLONCOLON);
    return true;
  }

  // (typeident DOT)*
  private static boolean typeqname_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeqname_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!typeqname_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "typeqname_1", c)) break;
    }
    return true;
  }

  // typeident DOT
  private static boolean typeqname_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeqname_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = typeident(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // COLONCOLON? (typeident2 DOT)* typeident2
  public static boolean typeqname2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeqname2")) return false;
    if (!nextTokenIs(b, "<typeqname 2>", COLONCOLON, ID)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPEQNAME_2, "<typeqname 2>");
    r = typeqname2_0(b, l + 1);
    r = r && typeqname2_1(b, l + 1);
    r = r && typeident2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COLONCOLON?
  private static boolean typeqname2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeqname2_0")) return false;
    consumeToken(b, COLONCOLON);
    return true;
  }

  // (typeident2 DOT)*
  private static boolean typeqname2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeqname2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!typeqname2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "typeqname2_1", c)) break;
    }
    return true;
  }

  // typeident2 DOT
  private static boolean typeqname2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeqname2_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = typeident2(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // USING_TKN refqname SEMICOLON
  public static boolean using(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "using")) return false;
    if (!nextTokenIs(b, USING_TKN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, USING_TKN);
    r = r && refqname(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, USING, r);
    return r;
  }

  /* ********************************************************** */
  // PRIVATE | PROTECTED | INTERNAL_PROT | INTERNAL | PUBLIC
  public static boolean visibility(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "visibility")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VISIBILITY, "<visibility>");
    r = consumeToken(b, PRIVATE);
    if (!r) r = consumeToken(b, PROTECTED);
    if (!r) r = consumeToken(b, INTERNAL_PROT);
    if (!r) r = consumeToken(b, INTERNAL);
    if (!r) r = consumeToken(b, PUBLIC);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
