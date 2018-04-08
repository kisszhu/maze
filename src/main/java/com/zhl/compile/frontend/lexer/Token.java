package com.zhl.compile.frontend.lexer;

/**
 * Created by zhuhailong-dc on 2018/4/1.
 * -
 */
public class Token {
    public final int tag;
    public Token(int t){tag=t;}
    public String toString(){return ""+(char)tag;}
}
