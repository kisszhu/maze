package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Token;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 在类Or中，方法jumping生成了一个布尔表达式B=B1 || B2 的跳转代码
 * -
 * 当前假设B的true出口t和false出口都不是特殊符号0。
 * 因为B1为真，B必然为真，，所以B1的true出口必然是t，而它的false出口
 * 对应于B2的第一条指令。
 * B2的true和false出口和B的相应出口相同
 */
public class Or extends Logical {

    public Or(Token tok, Expr x1, Expr x2) {
        super(tok, x1, x2);
    }

    public void jumping(int t, int f) {
        int lable = t != 0 ? t : newlabel();
        expr1.jumping(lable, 0);
        expr1.jumping(t, f);
        if (t == 0) emitlabel(lable);
    }

}
