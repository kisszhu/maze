package com.zhl.rules.antlr.java.java8.test;

import com.zhl.rules.antlr.java.java8.JavaLexer;
import com.zhl.rules.antlr.java.java8.JavaParser;
import org.antlr.v4.runtime.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhuhailong-dc on 2018/4/30.
 * Antlr：异常报告和恢复
 * 默认情况下ANTLR输出异常采用的是标准输出，不过我们可以通过实现'AntlrErrorListener'来修改输出异常
 */
public class ErrorListenerTest {

    public static class VerboseListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            List stack = ((Parser) recognizer).getRuleInvocationStack();
            Collections.reverse(stack);
            System.err.println("rule stack: " + stack);
            System.err.println("line " + line + " : " + charPositionInLine + " at " + offendingSymbol + " : " + msg);

        }
    }

    public static void main(String[] args) throws Exception {
        String expr = "class T T { int ;}";
        JavaLexer lexer = new JavaLexer(CharStreams.fromString(expr));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        // 先移除标准的错误监听器,否则会得到重复信息
        parser.removeErrorListeners();
        parser.addErrorListener(new VerboseListener());

        JavaParser.StatementContext sc = parser.statement();
    }
}
