package com.zhl.compile.frontend.symbols;

import com.zhl.compile.frontend.lexer.Tag;
import com.zhl.compile.frontend.lexer.Word;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 将类Type定义为类Word的子类。
 * 因为像int这样的基本类型名字就是保留字，将被词法分析器从词素映射为适当的对象。
 * 对应于基本类型的对象是Type.Int、Type.Float、Type.Char、Type.Bool
 * 这些对象从超类中继承了字段tag，相应的值被设置为Tag.BASIC
 * 因此语法分析器以同样的方式处理它们
 */
public class Type extends Word {
    public int width = 0;  // 用于存储分配

    public Type(String s, int tag, int w) {
        super(s, tag);
        width = w;
    }

    public static final Type
            Int = new Type("int", Tag.BASIC, 4),
            Float = new Type("float", Tag.BASIC, 8),
            Char = new Type("char", Tag.BASIC, 1),
            Bool = new Type("bool", Tag.BASIC, 1);

    // 下面的这两个方法主要用于类型转换
    public static boolean numeric(Type p) {
        if (p == Type.Char || p == Type.Int || p == Type.Float) {
            return true;
        } else {
            return false;
        }
    }

    // 在两个数字 类型之间允许进行类型转换，结果类型是这两个类型的“max”值
    public static Type max(Type p1, Type p2) {
        if (!numeric(p1) || !numeric(p2)) return null;
        else if (p1 == Type.Float || p2 == Type.Float) return Type.Float;
        else if (p1 == Type.Int || p2 == Type.Int) return Type.Int;
        else return Type.Char;
    }
}
