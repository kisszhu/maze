package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Lexer;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 表达式的中间代码
 * 包inter包含了Node的类层次结构。Node有两个子类：
 * 对应于表达式结点的Expr和对应于语句结点的Stmt
 * Expr的某些方法处理布尔表达式和跳转代码
 */
public class Node {
    /**
     * 抽象语法树种的结点被实现为类Node的对象。为了报告错误，字段
     * lexline保存了本结点对应的构造在源程序中的行好
     */
    int lexline = 0;

    Node() {
        lexline = Lexer.line;
    }

    void error(String s) {
        throw new Error("near line " + lexline + ": " + s);
    }

    static int labels = 0;

    public int newlabel() {
        return ++labels;
    }

    public void emitlabel(int i) {
        System.out.println("L" + i + ":");
    }

    public void emit(String s) {
        System.out.println("\t" + s);
    }
    

}
