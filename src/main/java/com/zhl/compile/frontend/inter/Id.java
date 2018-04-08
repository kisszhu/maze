package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Word;
import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 因为标识符就是一个地址，类Id从类Expr中继承了gen和reduce的默认实现
 */
public class Id extends Expr {
    public int offset; // 相对地址

    public Id(Word id, Type p, int b) {
        // 第一个值是类型，第二个值是操作
        super(id, p);
        offset = b;
    }

    // 对应于一个标识符的类Id的结点是一个叶子结点
    
}
