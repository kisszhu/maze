package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Token;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 在一般情况下，B的true出口t可能是特殊标号0。
 */
public class And extends Logical {

    public And(Token tok, Expr x1, Expr x2) {
        super(tok, x1, x2);
    }

    public void jumping(int t, int f) {
        int label = f != 0 ? f : newlabel();
        expr1.jumping(0, label);
        expr2.jumping(t, f);
        if (f == 0) emitlabel(label);
    }
}
