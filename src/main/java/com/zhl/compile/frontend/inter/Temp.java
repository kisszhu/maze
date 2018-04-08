package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Word;
import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 临时名字也有类型
 */
public class Temp extends Expr {
    static int count = 0;
    int number = 0;

    public Temp(Type p) {
        super(Word.temp, p);
        number = ++count;
    }

    public String toString() {
        return "t" + number;
    }
}
