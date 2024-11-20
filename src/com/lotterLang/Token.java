package com.lotterLang;
public class Token 
{

final TokenType type;
final String lexeme;
final Object literal;
final int line;

public Token(TokenType type, String lexeme, Object literal, int line) 
{
    this.type = type;
    this.lexeme = lexeme;
    this.literal = literal;
    this.line = line;
}

public final String getLexeme()
{
    return this.lexeme;
}

public String toString() 
{
    return type + " " + lexeme + " " + literal;
}

} // class Token
