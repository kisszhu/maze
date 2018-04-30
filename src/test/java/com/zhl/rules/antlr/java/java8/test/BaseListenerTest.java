package com.zhl.rules.antlr.java.java8.test;

import com.zhl.rules.antlr.java.java8.JavaLexer;
import com.zhl.rules.antlr.java.java8.JavaParser;
import com.zhl.rules.antlr.java.java8.JavaParserBaseListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Created by zhuhailong-dc on 2018/4/30.
 * 基础监听器
 */
public class BaseListenerTest {
    static class BaseListener extends JavaParserBaseListener {
        TokenStreamRewriter rewriter;

        public BaseListener(TokenStream token) {
            rewriter = new TokenStreamRewriter(token);
        }

        @Override
        public void enterStatement(JavaParser.StatementContext ctx) {
            super.enterStatement(ctx);
            rewriter.insertAfter(ctx.stop, "!!");
        }
    }

    public static void main(String[] args) throws Exception {
        String expr = "1=1+1;";
        JavaLexer lexer = new JavaLexer(CharStreams.fromString(expr));
        CommonTokenStream token = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(token);
        ParseTree tree = parser.statement();

        BaseListener listener = new BaseListener(token);

        System.out.println("----->>>>>>> ");
        System.out.println(listener.rewriter.getText());

        // 开始遍历语法树
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);

        System.out.println("<<<<<<<-------- ");
        System.out.println(listener.rewriter.getText());

    }
}
