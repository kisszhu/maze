package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Token;
import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * Logical为类Or、And、Not提供了一些常见功能
 */
public class Logical extends Expr {

    // 对应于一个逻辑运算符的运算分量
    public Expr expr1, expr2;

    Logical(Token tok, Expr x1, Expr x2) {
        super(tok, null); // 开始时类型设置为null
        expr1 = x1;
        expr2 = x2;
        type = check(expr1.type, expr2.type);
        if (type == null) error("type error");
    }

    public Type check(Type p1, Type p2) {
        if (p1 == Type.Bool && p2 == Type.Bool) return Type.Bool;
        else return null;
    }

    /**
     * TODO 这段代码存在疑问？
     */
    public Expr gen() {
        int f = newlabel();
        int a = newlabel();
        Temp temp = new Temp(type);
        this.jumping(0, f);
        emit(temp.toString() + " = true");
        emit("goto L" + a);
        emitlabel(f);
        emit(temp.toString() + " = false");
        emitlabel(a);
        return temp;
    }

    public String toString() {
        return expr1.toString() + " " + op.toString() + " " + expr1.toString();
    }

}
