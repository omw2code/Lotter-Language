package com.lotterLang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lotterLang.TokenType.*;

class Scanner 
{
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) 
    {
        this.source = source;
    }

    List<Token> scanTokens() 
    {
        while (current < source.length()) 
        {
            // beginning of the next lexeme
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() 
    {
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_CURLY); break;
            case '}': addToken(RIGHT_CURLY); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(ASTERISK); break;
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
                if (match('/')) // if we match a comment we want to finish iterating the line
                {
                    while(peek() != '\n' && !isAtEnd()) advance();
                }
                else 
                {
                    addToken(SLASH);
                }
                break;
            case ' ':  
            case '\r': 
            case '\t': 
                break;
            case '\n': 
                line++;
                break;
            case '"': string(); break; // we hit a " so its a string
            default:
                Lotter.error(line, "Unexpected character.");
                break;
        }
    }
    // incriment and return the prev character.
    // this will be useful in edge cases that require follow up characters
    private char advance() 
    {
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type) 
    {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) 
    {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expected) 
    {
        if (isAtEnd()) return false; 
        if (source.charAt(current) != expected) return false;
        
        current++;
        return true;
    }

    // this allows us to look ahead at the current unconsumed character
    private char peek()
    {
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private boolean isAtEnd()
    {
        //implement this
        return false;
    }

    private void string() {
        while(peek() != '"' && !isAtEnd())
        {
            if (peek() == '\n') line++;
            advance();
        }

        // gracefully handle running out of import before a closing "
        if(isAtEnd()) {
            Lotter.error(line, "Unterminated string.");
            return;
        }

        advance(); // position will now be the closing "

        String value = source.substring(start + 1, current - 1); // just getting rid of the quotes
        addToken(STRING, value);
    }
}
