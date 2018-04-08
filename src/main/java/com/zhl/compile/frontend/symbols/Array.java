package com.zhl.compile.frontend.symbols;

import com.zhl.compile.frontend.lexer.Tag;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 数组是这个源语言中唯一的构造类型。
 */
public class Array extends Type {
    public Type of;  // 数组的元素类型
    public int size = 1; // 元素个数

    public Array(int sz, Type p) {
        super("[]", Tag.INDEX, sz * p.width);
        size = sz;
        of = p;
    }

    public String toString() {
        return "[" + size + "]" + of.toString();
    }
}
