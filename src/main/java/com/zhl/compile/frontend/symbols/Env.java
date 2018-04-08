package com.zhl.compile.frontend.symbols;

import com.zhl.compile.frontend.inter.Id;
import com.zhl.compile.frontend.lexer.Token;

import java.util.Hashtable;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 类Lexer把字符串映射为字，类Env把字符串词法单元映射为类Id的对象。
 * 类Id和其他的对应于表达式和语句的类一起都在包inter中定义。
 */
public class Env {
    private Hashtable table;
    protected Env prev;

    public Env(Env n) {
        table = new Hashtable();
        prev = n;
    }

    public void put(Token w, Id i) {
        // Token将数字转换为Char
        table.put(w, i);
    }

    public Id get(Token w) {
        for (Env e = this; e != null; e = e.prev) {
            Id found = (Id) (e.table.get(w));
            if (found != null) {
                return found;
            }
        }
        return null;
    }


}
