package com.zhl.rules.antlr.java.java8.test;

import com.zhl.rules.antlr.java.java8.JavaParser;
import com.zhl.rules.antlr.java.java8.JavaParserBaseVisitor;

/**
 * Created by zhuhailong-dc on 2018/4/30.
 * 基础访问器
 */
public class BaseVisitorTest {

//    private static class MyVisitor extends JavaParserBaseVisitor<Double> {
//        @Override
//        public Double visitStatement(JavaParser.StatementContext ctx) {
//            return super.visitStatement(ctx);
//        }
//
//        /**
//         *
//         */
//        @Override
//        public Double visitExpression(JavaParser.ExpressionContext ctx) {
//
//            return super.visitExpression(ctx);
//        }
//    }
}

//在下面的例子中，我们继承了ExprBaseVisitor这个ANTLR生成的类，并重写了visitExpression, visitMultExpr,visitAtom这几个方法。
//        从visitAtom看起，我们先通过使用自动生成的与语法规则同名的expression()来判断用户输入的DSL解析之后的语法节点中是否含有expression规则，
//        如果没有，那么直接取词法单元的文本，并将其转化为Double。如果有expression，则调用visit方法递归地进行解析。
//
//        在visitExpression()中，我们通过ctx.children来获取词法规则expression : multExpr (('+' | '-') multExpr)*
//        ;中*号匹配的多重子规则项，然后判断Context的类型是否为TerminalNode来得知child究竟是词法单元还是语法单元，如果是词法单元，
//        则得到操作方法是加好还是减号，如果是语法单元，则递归下降地继续求值。
