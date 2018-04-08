package com.zhl.compile.frontend.lexer;

/**
 * Created by zhuhailong-dc on 2018/4/1.
 * 用于处理浮点数
 */
public class Real extends Token {
    public final float value;
    public Real(float v){super(Tag.REAL);value=v;}
    public String toString(){return ""+value;}
}
