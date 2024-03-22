package com.nom.ideanom;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.nom.ideanom.NomTypes.*;

%%

%{
  public _NomLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _NomLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

WHITE_SPACE=[ \t\r\n]+
STRING=\"[^\"]*\"
FLOAT=([0-9]+)?\\.[0-9]+
INT=[0-9]+
INTERNAL_PROT=(internal[ \t\r\n]+protected)|(protected[ \t\r\n]+internal)
ID=[a-zA-Z_][a-zA-Z0-9_]*
COMMENT=#.*?[\r\n]

%%
<YYINITIAL> {
  {WHITE_SPACE}         { return WHITE_SPACE; }

  "true"                { return TRUE; }
  "false"               { return FALSE; }
  "using"               { return USING_TKN; }
  "abstract"            { return ABSTRACT; }
  "partial"             { return PARTIAL; }
  "expando"             { return EXPANDO; }
  "readonly"            { return READONLY; }
  "namespace"           { return NAMESPACE; }
  "interface"           { return INTERFACE; }
  "class"               { return CLASS; }
  "instance"            { return INSTANCE; }
  "extends"             { return EXTENDS; }
  "implements"          { return IMPLEMENTS; }
  "dyn"                 { return DYN; }
  "static"              { return STATIC; }
  "final"               { return FINAL; }
  "null"                { return NUL; }
  "virtual"             { return VIRTUAL; }
  "override"            { return OVERRIDE; }
  "multi"               { return MULTI; }
  "default"             { return DEFAULT; }
  "shape"               { return SHAPE; }
  "material"            { return MATERIAL; }
  "in"                  { return IN; }
  "out"                 { return OUT; }
  "inout"               { return INOUT; }
  "calltarget"          { return CALLTARGET; }
  "public"              { return PUBLIC; }
  "private"             { return PRIVATE; }
  "protected"           { return PROTECTED; }
  "internal"            { return INTERNAL; }
  "ifnull"              { return IFNULL; }
  "ifobj"               { return IFOBJ; }
  "if"                  { return IF; }
  "else"                { return ELSE; }
  "elseif"              { return ELSEIF; }
  "while"               { return WHILE; }
  "for"                 { return FOR; }
  "foreach"             { return FOREACH; }
  "return"              { return RETURN; }
  "break"               { return BREAK; }
  "continue"            { return CONTINUE; }
  "fun"                 { return FUN; }
  "super"               { return SUPER; }
  "constructor"         { return CONSTRUCT; }
  "new"                 { return NEW; }
  "let"                 { return LET; }
  "letvar"              { return LETVAR; }
  "DEBUG"               { return DBG; }
  "ERROR"               { return ERR; }
  "RUNTIMECMD"          { return RUNTIMECMD; }
  ";"                   { return SEMICOLON; }
  "::"                  { return COLONCOLON; }
  ":"                   { return COLON; }
  "."                   { return DOT; }
  ".."                  { return DOTDOT; }
  ","                   { return COMMA; }
  "("                   { return LPAREN; }
  ")"                   { return RPAREN; }
  "["                   { return LBRACKET; }
  "]"                   { return RBRACKET; }
  "{"                   { return LBRACE; }
  "}"                   { return RBRACE; }
  "<"                   { return LANGLE; }
  ">"                   { return RANGLE; }
  "->"                  { return ARROW; }
  "?"                   { return QMARK; }
  "&&"                  { return AND; }
  "||"                  { return OR; }
  "++"                  { return APPEND; }
  "+"                   { return PLUS; }
  "-"                   { return MINUS; }
  "/"                   { return SLASH; }
  "*"                   { return TIMES; }
  "**"                  { return POW; }
  "%"                   { return PERCENT; }
  "="                   { return EQ; }
  "=="                  { return EQEQ; }
  "!="                  { return NEQ; }
  "==="                 { return EQEQEQ; }
  "<="                  { return LEQ; }
  ">="                  { return GEQ; }
  "^"                   { return BITXOR; }
  "&"                   { return BITAND; }
  "|"                   { return BITOR; }
  "+="                  { return PLUSEQ; }
  "-="                  { return MINUSEQ; }
  "*="                  { return TIMESEQ; }
  "/="                  { return DIVEQ; }
  "=>"                  { return BIGARROW; }
  "!"                   { return BANG; }

  {WHITE_SPACE}         { return WHITE_SPACE; }
  {STRING}              { return STRING; }
  {FLOAT}               { return FLOAT; }
  {INT}                 { return INT; }
  {INTERNAL_PROT}       { return INTERNAL_PROT; }
  {ID}                  { return ID; }
  {COMMENT}             { return COMMENT; }

}

[^] { return BAD_CHARACTER; }
