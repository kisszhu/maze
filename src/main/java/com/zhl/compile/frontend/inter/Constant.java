package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Num;
import com.zhl.compile.frontend.lexer.Token;
import com.zhl.compile.frontend.lexer.Word;
import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 布尔表达式的跳转代码：
 * -
 * 布尔表达式B的跳转代码由方法jumping生成。这个方法的参数是两个标号
 * t和f，它们分别称为表达式B的true出口和false出口。
 * 如果B的值为真，代码中就包含一个目标为t的跳转指令；
 * 如果B的值为假，就有一个目标为f的指令。
 * 按照惯例，特殊标号0表示控制流从B穿越，到达B的代码之后的下一个指令
 */
public class Constant extends Expr {
    /**
     * 它在抽象语法树种构造出一个标号为tok、类型为p的叶子结点
     */

    public Constant(Token tok, Type p) {
        super(tok, p);
    }

    public Constant(int i) {
        super(new Num(i), Type.Int);
    }

    public static final Constant
            True = new Constant(Word.True, Type.Bool),
            False = new Constant(Word.False, Type.Bool);

    /**
     * @param t 如果这个常量是静态对象True，t不是特殊符号0，那么就会
     *          生成一个目标为t的跳转指令
     * @param f 如果这是对象False 且 f 非零，那么就会生成一个目标为f的跳转指令
     */
    public void jumping(int t, int f) {
        if (this == True && t != 0) emit("goto L" + t);
        else if (this == False && f != 0) emit("goto L" + f);
    }

}
