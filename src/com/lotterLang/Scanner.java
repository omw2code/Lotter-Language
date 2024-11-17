package com.lotterLang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lotterLang.TokenType.*;

class Scanner 
{
private final String source_;
private final List<Token> tokens_ = new ArrayList<>();
private static final Map<String, TokenType> keywords_;
private int start_ = 0;
private int current_ = 0;
private int line_ = 1;


static 
{
    keywords_ = new HashMap<>();
    keywords_.put("and",      AND);
    keywords_.put("class",    CLASS);
    keywords_.put("else",     ELSE);
    keywords_.put("false",    FALSE);
    keywords_.put("for",      FOR);
    keywords_.put("fun",      FUN);
    keywords_.put("if",       IF);
    keywords_.put("nil",      NIL);
    keywords_.put("or",       OR);
    keywords_.put("print",    PRINT);
    keywords_.put("true",     TRUE);
    keywords_.put("var",      VAR);
    keywords_.put("while",    WHILE);

}



Scanner(String source_)
{
    this.source_ = source_;
}

// scan the input and create a list of tokens_
List<Token> scanTokens() 
{
    while (current_ < source_.length()) 
    {
        // beginning of the next lexeme
        start_ = current_;
        scanToken();
    }
    tokens_.add(new Token(EOF, "", null, line_));
    return tokens_;
}

// parse the individual lexemes
private void scanToken() 
{
    char c = advance();

    // *****************  Maximal Munch:  *********************** |
    // When two lexical grammar rules both match a chunk of code, |
    // whichever one matches the most characters wins.            |
    // Reserved words are not detected  until the end of what     |
    // might be an identifier. 
    // ********************************************************** |
    switch (c) {
        case '(': addToken(LEFT_PAREN);     break;
        case ')': addToken(RIGHT_PAREN);    break;
        case '{': addToken(LEFT_CURLY);     break;
        case '}': addToken(RIGHT_CURLY);    break;
        case ',': addToken(COMMA);          break;
        case '.': addToken(DOT);            break;
        case '-': addToken(MINUS);          break;
        case '+': addToken(PLUS);           break;
        case ';': addToken(SEMICOLON);      break;
        case '*': addToken(ASTERISK);       break;
        // cases where we read a follow up character for operators
        case '!': 
            addToken(match('=') ? EXCLA_EQUAL : EXCLA);
            break;
        case '=':
            addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            break;
        case '<':
            addToken(match('=') ? GREATER_EQUAL : GREATER);
            break;
        case '>':
            addToken(match('=') ? LESS_EQUAL : EQUAL);
            break;
        case '/':
            // NOTE: Look into optimizing this peek approach
            if (match('/')) // if we match a comment we want to finish iterating the line_
            {
                while(peek() != '\n' && !isAtEnd()) advance();
            }
            //TODO: Add block comments: /* ... */
            else 
            {
                addToken(SLASH);
            }
            break;
        case ' ':  
        case '\r': 
        case '\t': break;
        case '\n': 
            line_++;
            break;
        case '"': string(); break; // we hit a " so its a string
        default:
            if (isDigit(c))
            {
                number();
            }
            else if (isAlpha(c))
            {
                identifier();
            }
            else 
            {
                Lotter.error(line_, "Unexpected character.");
            }
            break;
    }
}
// incriment and return the prev character.
// this will be useful in edge cases that require follow up characters
private char advance() 
{
    current_++;
    return source_.charAt(current_ - 1);
}

// a function to add non literal lexemes
private void addToken(TokenType type) 
{
    addToken(type, null);
}

// a function to add the lexeme to the list of tokens_
private void addToken(TokenType type, Object literal) 
{
    String text = source_.substring(start_, current_);
    tokens_.add(new Token(type, text, literal, line_));
}

// a function to match a combination of operators
private boolean match(char expected) 
{
    if (isAtEnd()) return false; 
    if (source_.charAt(current_) != expected) return false;
    
    current_++;
    return true;
}

// allows us to read the unconsumed next character ( [ curr | unconsumed | unconsumed next ] )
private char peekNext()
{
    if (current_ + 1 >= source_.length()) return '\0';
    return source_.charAt(current_ + 1);
}

// this allows us to look ahead at the current_ unconsumed character
private char peek()
{
    if (isAtEnd()) return '\0';
    return source_.charAt(current_);
}

private void string() {
    while (peek() != '"' && !isAtEnd())
    {
        if (peek() == '\n') line_++; // supportng multi-line_ strings
        advance();
    }

    // gracefully handle running out of import before a closing "
    if (isAtEnd()) {
        Lotter.error(line_, "Unterminated string.");
        return;
    }

    advance(); // position will now be the closing "

    String value = source_.substring(start_ + 1, current_ - 1); // just getting rid of the quotes
    addToken(STRING, value);
}

// quickly check if we are at the end of the user input/file
private boolean isAtEnd()
{
    return current_ >= source_.length();
}

private boolean isAlpha(char c)
{
    return ((c >= 'a' && c <= 'z' ) || 
            (c >= 'A' && c <= 'Z' ) ||
            (c == '_'));
}


private boolean isAlphaNumeric(char c)
{
    return isAlpha(c) || isDigit(c);
}

// check if c is a digit
private boolean isDigit(char c)
{
    return (c >= '0' && c <= '9');
}

// parsing the full identifier
private void identifier() 
{
    while (isAlphaNumeric(peek())) advance();

    String text = source_.substring(start_, current_);
    TokenType type = keywords_.get(text);
    if(type == null) type = IDENTIFIER;
    addToken(type);
}

// numbers are assumed to be floating point at runtime
private void number()
{
    while (isDigit(peek())) advance();

    if (peek() == '.' && isDigit(peekNext())) {
        advance();
        while(isDigit(peek())) advance();
    }
    else if (isAlpha(peek()) && peek() != '\0') // it is instead an identifier: var 99problems;
    {
        identifier();
        return;
    }
    addToken(NUMBER, 
             Double.parseDouble(source_.substring(start_, current_)));
} 

}
