package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Token;
import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 提供了reduce的一个实现
 */
public class Op extends Expr {

    /**
     * 这个类的子类包括：
     * 表示算术运算符的子类Arith
     * 表示单目运算符的子类Unary
     * 表示数组访问的子类Access
     * <p>
     * 在每种情况下，reduce调用gen来生成一个项，生成一个指令把
     * 这个项赋值给一个新的临时名字，并返回这个临时名字。
     */

    public Op(Token tok, Type p) {
        super(tok, p);
    }

    public Expr reduce() {
        Expr x = gen();
        Temp t = new Temp(type);
        emit(t.toString() + " = " + x.toString());
        return t;
    }

}
