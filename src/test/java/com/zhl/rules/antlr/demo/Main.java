package com.zhl.rules.antlr.demo;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * Created by zhuhailong-dc on 2018/4/4.
 * 基于Antlr生成的词法分析器和语法分析器后，可以基于它们来验证我们的输入
 * 的表达式是否合法
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String[] testStr = {
                "2",
                "a+b+3",
                "(a-b)+3"//,
                // "a+(b*3"
        };
        for (String s : testStr) {
            System.out.printf("Input expr: " + s);
            run(s);
        }
    }

    public static void run(String expr) throws Exception {
        // 对每一个输入的字符串，构造一个ANTLRStringStream流 in
        ANTLRInputStream in = new ANTLRInputStream(expr);

        // 用 in 构造词法分析器 lexer，词法分析的作用是产生记号
        DemoLexer lexer = new DemoLexer(in);

        // 用词法分析器 lexer构造一个记号流 tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // 再使用 tokens 构造语法分析器 parser，至此已经完成词法分析和语法分析的准备工作
        DemoParser parser = new DemoParser(tokens);

        // 最终调用语法分析器的规则prog，完成对表达式的验证。
        parser.prog();
    }
    // Caused by: java.io.InvalidClassException: org.antlr.v4.runtime.atn.ATN; Could not deserialize ATN with UUID
    // 执行提示这个错误信息的话，我升级了Antlr的版本，从4.5.3升级到了4.7
}
