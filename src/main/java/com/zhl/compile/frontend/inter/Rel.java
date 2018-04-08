package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Token;
import com.zhl.compile.frontend.symbols.Array;
import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * <
 * <=
 * ==
 * !=
 * >=
 * >
 */
public class Rel extends Logical {
    public Rel(Token tok, Expr x1, Expr x2) {
        super(tok, x1, x2);
    }

    public Type check(Type p1, Type p2) {
        if (p1 instanceof Array || p2 instanceof Array) return null;
        else if (p1 == p2) return Type.Bool;
        else return null;
    }

    public void jumping(int t, int f) {
        // 为子表达式生成代码
        Expr a = expr1.reduce();
        Expr b = expr2.reduce();

        String test = a.toString() + " " + op.toString() + " " + b.toString();

        /**
         * 如果 t 和 f 都不是特殊标号0，那么执行下列代码：
         *   emit("if "+test+" goto L"+t)
         *   emit("goto L"+f)
         * 如果 t 或 f 是特殊标号0，那么最多只会生成一个指令
         *   else if(t != 0) emit("if "+test+" goto L"+t)
         *   else if(f != 0) emit("ifalse "+test+" goto L"+f)
         *   else; // 不生成指令，因为t和f都直接穿越
         */
        emitJumps(test, t, f);
    }


}






















