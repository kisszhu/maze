package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Token;
import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 表达式构造被实现为Expr的子类
 * 类Expr包含字段op和type
 * 分别表示了一个结点上的运算符和类型。
 */
public class Expr extends Node {

    public Token op;
    public Type type;

    Expr(Token tok, Type p) {
        op = tok;
        type = p;
    }

    /**
     * 返回了一个"项"，该项可以成为一个三地址指令的右部。给定一个
     * 表达式E=E1+E2，方法gen返回一个项x1+x2，其中x1和x2分别是存放
     * E1和E2值得地址。如果这个对象是一个地址，就可以返回this值。
     * Expr的子类通常会重新实现gen。
     */
    public Expr gen() {
        return this;
    }

    /**
     * 把一个表达式计算（或者说“归约”）成为一个单一的地址。
     * 也就是说，它返回一个常量、一个标识符、或者一个临时名字。
     * 给定一个表达式E，方法reduce返回一个存放E的值得临时变量t
     */
    public Expr reduce() {
        return this;
    }

    public void jumping(int t, int f) {
        emitJumps(toString(), t, f);
    }

    public void emitJumps(String test, int t, int f) {
        if (t != 0 && f != 0) {
            emit("if " + test + " goto L" + t);
            emit("goto L" + f);
        } else if (t != 0) emit("if " + test + " goto L" + t);
        else if (f != 0) emit("ifelse " + test + " goto L" + f);
        else ; // 不生成指令，因为t和f都直接穿越
    }

    public String toString() {
        return op.toString();
    }


}




















