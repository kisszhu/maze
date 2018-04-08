package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Token;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 虽然Not是一个单目运算符，这个类和其他布尔运算符之间仍然具有相当多的
 * 共同之处，因为我们把它作为Logical的一个子类。
 */
public class Not extends Logical {

    public Not(Token tok, Expr x2) {
        super(tok, x2, x2);
    }

    public void jumpint(int t, int f) {
        expr2.jumping(f, t);
    }

    public String toString() {
        return op.toString() + " " + expr2.toString();
    }

}
