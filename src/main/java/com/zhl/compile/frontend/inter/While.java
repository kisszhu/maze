package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * -
 */
public class While extends Stmt {

    Expr expr;
    Stmt stmt;

    /**
     * 创建了一个子节点为空的结点
     */
    public While() {
        expr = null;
        stmt = null;
    }

    /**
     * @param x
     * @param s
     */
    public void init(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool) expr.error("boolean required in while");
    }

    /**
     * 用于生成三地址代码
     *
     * @param b
     * @param a
     */
    public void gen(int b, int a) {
        after = a; // 保存标号a
        expr.jumping(0, a);
        int label = newlabel();
        emitlabel(label);
        /**
         * stmt的代码之后紧跟着一个目标为b的跳转指令。
         * 这个指令是的while循环进入下一次迭代。
         */
        stmt.gen(label, b);
        emit("goto L" + b);
    }

}
