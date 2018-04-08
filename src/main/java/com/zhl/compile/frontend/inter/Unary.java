package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Token;
import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 处理单目运算符
 */
public class Unary extends Op {
    public Expr expr;

    public Unary(Token tok, Expr x) {
        super(tok, null);
        expr = x;
        type = Type.max(Type.Int, expr.type);
        if (type == null) error("type error");
    }

    public Expr gen() {
        return new Unary(op, expr.reduce());
    }

    public String toString() {
        return op.toString() + " " + expr.toString();
    }
}
