package com.lotterLang.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst 
{

public static void main(String[] args)
{
    if(args.length != 1)
    {
        System.err.println("Usage: GenerateAst <output dir>");
        System.exit(64);
    }
    String outputDir = args[0];
    // each is the name of the class followed by the list of fields
    // each field has a type and name
    try {
        defineAst(outputDir, "Expr", Arrays.asList(
            "Binary      : Expr left,Token operator,Expr right",
            "Grouping    : Expr expression",
            "Literal     : Object value",
            "Unary       : Token operator,Expr right"
        ));
    } catch (IOException e) {
        e.printStackTrace(); // Handle the IOException here
    }
} 

// metapgrogram AST with the Interpreter Design Pattern
private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException
{
    String path = outputDir + "/" + baseName + ".java";
    
    PrintWriter writer = new PrintWriter(path, "UTF-8");
    
    //print the class template
    writer.println("package com.lotterLang;");
    writer.println("import java.util.List;");
    writer.println();
    writer.println("abstract class " + baseName);
    writer.println("{");

    //visitor pattern
    defineVisitor(writer, baseName, types);
    writer.println();
    writer.println("    abstract <R> R accept(Visitor<R> visitor);");
    writer.println();


    //parse the input string
    for (String type : types)
    {
        String className = type.split(":")[0].trim();
        String attributes = type.split(":")[1].trim();
        defineType(writer, baseName, className, attributes);
        writer.println();
    }

    writer.println("}");
    writer.close();
}

private static void defineVisitor(PrintWriter writer, String baseName, List<String> types)
{
    writer.println("    interface Visitor<R> {");

    for (String type : types)
    {
        String typeName = type.split(":")[0].trim();
        writer.println("        R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
    }

    writer.println("    }");
}

private static void defineType(PrintWriter writer, String baseName, String className, String attributeList)
{

    writer.println("static class " + className + " extends " + baseName);
    writer.println("{");

    // visitor pattern
    writer.println("    @Override");
    writer.println("    <R> R accept(Visitor<R> visitor) {");
    writer.println("        return visitor.visit" + className + baseName + "(this);");
    writer.println("    }");
    writer.println();

    // constructor
    writer.println("    "+ className + "(" + attributeList + ") {");
    //initialize the attributes
    String[] attributes = attributeList.split(",");
    for (String attribute : attributes)
    {
        String name = attribute.split(" ")[1];
        writer.println("        this." + name + " = " + name + ";");
    }
    writer.println("    }");


    //attributes
    for (String attribute : attributes)
    {
        writer.println("    final " + attribute + ";");
    }

    writer.println("}");

}

}
