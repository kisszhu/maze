package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Token;
import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 实现了双目运算符，比如+和*。
 */
public class Arith extends Op {
    public Expr expr1, expr2;

    /**
     * tok：是一个表示该运算符的词法单元
     * null：是类型的占位符
     */
    public Arith(Token tok, Expr x1, Expr x2) {
        super(tok, null);
        expr1 = x1;
        expr2 = x2;
        type = Type.max(expr1.type, expr2.type);
        if (type == null) error("type error");
    }

    /**
     * 方法gen把表达式归约为地址，并将表达式的运算符作用于这些地址
     * 从而构造出了一个三地址指令的右部。
     * 比如，假设gen在a+b*c的根部被调用。其中对reduce的调用返回a作为
     * 子表达式a的地址，并返回t作为b*c的地址。
     * 同时，reduce还生成指令t=b*c
     *
     * @return：返回了一个新的Arith节点，其中的运算符是* 而运算分量是地址 a 和 t
     */
    public Expr gen() {
        return new Arith(op, expr1.reduce(), expr2.reduce());
    }

    public String toString() {
        return expr1.toString() + " " + op.toString() + " " + expr2.toString();
    }
}
