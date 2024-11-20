package com.lotterLang;
import java.util.List;

public abstract class Expr
{
    public interface Visitor<R> {
        R visitBinaryExpr(Binary expr);
        R visitGroupingExpr(Grouping expr);
        R visitLiteralExpr(Literal expr);
        R visitUnaryExpr(Unary expr);
    }

    public abstract <R> R accept(Visitor<R> visitor);

public static class Binary extends Expr
{
    @Override
    public  <R> R accept(Visitor<R> visitor) {
        return visitor.visitBinaryExpr(this);
    }

    public Binary(Expr left,Token operator,Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expr getLeft() {
        return this.left;
    }

    public Token getOperator() {
        return this.operator;
    }

    public Expr getRight() {
        return this.right;
    }
    private final Expr left;
    private final Token operator;
    private final Expr right;
}

public static class Grouping extends Expr
{
    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitGroupingExpr(this);
    }

    public Grouping(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() {
        return this.expression;
    }
    private final Expr expression;
}

public static class Literal extends Expr
{
    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitLiteralExpr(this);
    }

    public Literal(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }
    private final Object value;
}

public static class Unary extends Expr
{
    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitUnaryExpr(this);
    }

    public Unary(Token operator,Expr right) {
        this.operator = operator;
        this.right = right;
    }

    public Token getOperator() {
        return this.operator;
    }

    public Expr getRight() {
        return this.right;
    }
    private final Token operator;
    private final Expr right;
}

} // class Expr
