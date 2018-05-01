package com.zhl.rules.janino;

import org.codehaus.janino.ScriptEvaluator;

/**
 * Created by zhuhailong-dc on 2018/5/1.
 * -
 */
public class ClassBody {
    public static void main(String[] args) throws Exception {
        ScriptEvaluator se = new ScriptEvaluator();
        se.setReturnType(int.class);
        se.cook("class Test {\n" +
                "    public int method() {\n" +
                "        return 1;\n" +
                "    }\n" +
                "}\n" +
                "Test test = new Test();\n" +
                "int res = test.method();\n" +
                "return res;\n"
        );
        System.out.println(se.evaluate(null));
    }
}
