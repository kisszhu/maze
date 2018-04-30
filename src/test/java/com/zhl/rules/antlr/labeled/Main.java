package com.zhl.rules.antlr.labeled;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

/**
 * Created by zhuhailong-dc on 2018/4/30.
 * Main Class
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String inputFile = "." + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "data.txt";
        InputStream is = new FileInputStream(inputFile);
        run(is);
    }

    public static void run(InputStream expr) throws Exception {
        ANTLRInputStream input = new ANTLRInputStream(expr);

        LabeledExprLexer lexer = new LabeledExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LabeledExprParser parser = new LabeledExprParser(tokens);
        ParseTree tree = parser.prog(); // parse
        EvalVisitor eval = new EvalVisitor();
        eval.visit(tree);
    }
}
