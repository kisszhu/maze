package com.zhl.compile.frontend.lexer;

/**
 * Created by zhuhailong-dc on 2018/4/1.
 * -
 */
public class Num extends Token {
    public final int value;
    public Num(int v) {super(Tag.NUM);value=v;}

    public String toString(){return ""+value;}

}
