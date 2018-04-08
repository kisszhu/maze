package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * -
 */
public class If extends Stmt {
    Expr expr;
    Stmt stmt;

    /**
     * 构造函数为语句 if(E) S 构造一个结点。
     * 字段expr和stmt分别保存了E和S对应的结点。
     */
    public If(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool) expr.error("boolean required in if");
    }

    public void gen(int b, int a) {
        int label = newlabel(); // stmt的代码的标号
        // 调用expr.jumping(0,a)指明如果expr的值为真，控制流必须穿越expr的代码
        // 否则控制流必须转向标号a
        expr.jumping(0, a); // 为真时控制流穿越，为假时转向a
        emitlabel(label);
        stmt.gen(label, a);
    }
}
