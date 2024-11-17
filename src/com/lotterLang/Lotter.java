package com.lotterLang;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
// import java.util.Scanner;

public class Lotter 
{
static boolean hadError = false;
public static void main(String[] args) throws IOException 
{
    if(args.length > 1) {
        System.out.println("Usage: Lotter [script]");
        System.exit(64);
    } else if (args.length == 1) {
        runFile(args[1]);
    } else {
        runPrompt();
    }
}

// if a file is passed in as an arg, read in the bytes of the 
// file and create a new String from the file
private static void runFile(String path) throws IOException 
{
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));
    if(hadError) System.exit(65);
}

// REPL
// if no args are passed in, run the interpreter interactively
// from the command line
private static void runPrompt() throws IOException 
{
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    for(;;) {
        System.out.print("> ");
        String line = reader.readLine();
        if (line == null) break; // or pressing Control-D 
        run(line);
        hadError = false; // so the users session is not killed
    }
}

// run the file/prompt and tokenize their contents
private static void run(String source) 
{
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();

    for(Token token : tokens) {
        System.out.println(token);
    }
}

// error handling
static void error(int line, String message) 
{
    report(line, " ", message);
}

// outputting the error 
private static void report(int line, String where, String message) 
{
    System.err.println(
        "[line " + line + "] Error" + where + ": " + message
    );
    hadError = true;
}

}