package com.zhl.compile.frontend;

import com.zhl.compile.frontend.lexer.Lexer;
import com.zhl.compile.frontend.parser.Parser;

import java.io.IOException;

/**
 * Created by zhuhailong-dc on 2018/4/1.
 * -
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Lexer lex = new Lexer();
        Parser parser = new Parser(lex);
        parser.program();
        System.out.write('\n');
    }

    // {int i;int j;float v;float x;float[] a=new float[100];while(true){ do i=i+1;while(a[i]<v);do j=j-1;while(a[j]>v);if(i>=j) break;x=a[j];a[i]=a[j];a[j]=x;}}

}
