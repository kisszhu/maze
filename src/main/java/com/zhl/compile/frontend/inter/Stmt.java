package com.zhl.compile.frontend.inter;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 每个语句构造被实现为Stmt的一个子类。一个构造的组成部分对应的字段是
 * 相应子类的对象。
 * 例如，如我们将看到的，类While有一个对应于测试表达式的字段和一个子语句字段
 */
public class Stmt extends Node {

    public Stmt() {
    }

    public static Stmt Null = new Stmt();

    /**
     * 下面的代码时处理三地址代码的生成。
     * 其中b标记这个语句的代码的开始处，而a标记这个语句的代码之后的第一条指令
     * <p>
     * -
     * 方法gen是子类中的gen方法的占位符。
     * 子类While和Do要把它们的标号a存放在字段after中。
     * 当任务内层的break语句要跳出这个外层构造时就可以使用这些标号。
     */
    // 调用时的参数是语句开始处的标号和语句的下一条指令的标号
    public void gen(int b, int a) {
    }

    // 保存语句的下一条指令的标号
    int after = 0;

    // 表示一个空的语句序列
    // 用于break语句
    // 在语法分析时被用于跟踪外层构造。
    // (对于包含continue语句的源语言，我们可以使用同样的方法来跟踪一个continue语句的外层构造)
    public static Stmt Enclosing = Stmt.Null;

}
