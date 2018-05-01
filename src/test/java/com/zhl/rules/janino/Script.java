package com.zhl.rules.janino;

import org.codehaus.janino.ScriptEvaluator;

/**
 * Created by zhuhailong-dc on 2018/5/1.
 * Janino as a Script Evaluator
 */
public class Script {
    /*
    Analogously to the expression evaluator, a ScriptEvaluator API exists that compiles and processes
    a Java "block", i.e. the body of a method. if a a return value other than "void" is defined, then the
    block must return a value of that type

    As a special feature, it allows methods to be declared. The place and order of the method declarations is not relevant
     */
    public static void main(String[] args) throws Exception {
        //在janino 2.X的版本中，是不支持函数的，升级到3.X支持了
//        se.cook(
//                ""
//                        + "static void method1() {\n"
//                        + "    System.out.println(1);\n"
//                        + "}\n"
//                        + "\n"
//                        + "method1();\n"
//                        + "method2();\n"
//                        + "\n"
//                        + "static void method2() {\n"
//                        + "    System.out.println(2);\n"
//                        + "}\n"
//        );
//
//        se.evaluate(new Object[0]);

//        se.setReturnType(Boolean.class);
//        se.cook("" +
//                "        if (true) {\n" +
//                "            return true;  //System.out.println(\"true\");\n" +
//                "        } else {\n" +
//                "            return false; //System.out.println(\"false\");\n" +
//                "        }");
//        System.out.println(se.evaluate(new Object[0]));


//        //TODO 暂时没有找到给 method方法传入参数的写法。
//        se.setReturnType(int.class);
//        se.cook("    static int method1(int i) {\n" +
//                "        return i;\n" +
//                "    }\n" +
//                "    return method1();");
//        Object res = se.evaluate(null);
//        System.out.println("res = " + res);



    }
}
