package com.lotterLang;

import java.util.List;
import static com.lotterLang.TokenType.*;

// consumes an input sequence of tokens
class Parser 
{
private final List<Token> tokens;
private int current = 0;

Parser(List<Token> tokens)
{
    this.tokens = tokens;
}

// Expression grammar rule
private Expr expression() 
{
    return equality();
}

private Expr equality() 
{
    Expr expr = comparison();
    while (match(EXCLA_EQUAL, EQUAL_EQUAL))
    {
        Token operator = getPreviousToken();
        Expr right = comparison();
        expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
}

private Expr comparison() 
{
    Expr expr = term();

    while(match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL))
    {
        Token operator = getPreviousToken();
        Expr right = term();
        expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
}

private Expr term()
{
    Expr expr = factor();

    while (match(MINUS, PLUS))
    {
        Token operator = getPreviousToken();
        Expr right = factor();
        expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
}

private Expr factor()
{
    Expr expr = unary();

    while (match(SLASH, ASTERISK))
    {
        Token operator = getPreviousToken();
        Expr right = unary();
        expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
}

private Expr unary()
{
    if (match(EXCLA, MINUS))
    {
        Token operator = getPreviousToken();
        Expr right = unary();
        return new Expr.Unary(operator, right);
    }
    return primary();
}

private Expr primary()
{
    if (match(FALSE)) return new Expr.Literal(false);
    if (match(TRUE)) return new Expr.Literal(true);
    if (match(NIL)) return new Expr.Literal(null);

    if (match(NUMBER, STRING)) 
    {
        return new Expr.Literal(getPreviousToken().literal);
    }

    if (match(LEFT_PAREN))
    {
        Expr expr = expression();
        consume(RIGHT_PAREN, "Expect ')' after expression.");
        return new Expr.Grouping(expr);
    }
}

private boolean match(TokenType... types)
{
    for (TokenType type : types)
    {
        if (check(type)) 
        {
            advance();
            return true;
        }
    }
    return false;
}

private boolean check(TokenType type)
{
    if (isAtEnd()) return false;
    return peekNextToken().type == type;
}

private Token advance()
{
    if(!isAtEnd()) current++;
    return getPreviousToken();
}

private boolean isAtEnd() 
{
    return peekNextToken().type == EOF;
}

private Token peekNextToken()
{
    return tokens.get(current);
}

private Token getPreviousToken()
{
    return tokens.get(current - 1);
}

} // class Parser