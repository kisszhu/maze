package com.zhl.compile.frontend.lexer;

import java.io.IOException;
import java.util.Hashtable;

import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/1.
 */
public class Lexer {
    public static int line = 1;
    char peek = ' ';
    Hashtable words = new Hashtable();

    void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    public Lexer() {
        // 9~13 line 保留了选定的关键字
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("break", Tag.BREAK));
        // 14~16 保留了在其他地方定义的对象的词素
        reserve(Word.True);
        reserve(Word.False);
        reserve(Type.Int);
        reserve(Type.Char);
        reserve(Type.Bool);
        reserve(Type.Float);
    }

    /**
     * 用于把下一个输入字符读到变量peek中
     * 名字readch被复用或重载（19~24 line），以便帮助识别复合的词法单元。
     * 比如，一看到输入字符<，电泳readch("=")就会把下一个字符读入peek，
     * 并检查它是否为=
     */
    void readch() throws IOException {
        peek = (char) System.in.read();
    }

    boolean readch(char c) throws IOException {
        readch();
        if (peek != c) {
            return false;
        }
        peek = ' ';
        return true;
    }

    /**
     * 函数scan 一开始首先略过所有的空白字符，它首先试图识别像
     * <=这样的复合词法单元。
     * 如果不成功，它就试图读入一个字符串
     */
    public Token scan() throws IOException {
        for (; ; readch()) {
            if (peek == ' ' || peek == '\t') {
                continue;
            } else if (peek == '\n') {
                line = line + 1;
            } else {
                break;
            }
        }
        switch (peek) {
            case '&':
                if (readch('&')) return Word.and;
                else return new Token('&');
            case '|':
                if (readch('|')) return Word.or;
                else return new Token('|');
            case '=':
                if (readch('=')) return Word.eq;
                else return new Token('=');
            case '!':
                if (readch('=')) return Word.ne;
                else return new Token('!');
            case '<':
                if (readch('=')) return Word.le;
                else return new Token('<');
            case '>':
                if (readch('=')) return Word.ge;
                else return new Token('>');
        }

        if (Character.isDigit(peek)) {
            int v = 0;
            do {
                v = 10 * v + Character.digit(peek, 10);
                readch();
            } while (Character.isDigit(peek));
            if (peek != '.') return new Num(v);
            float x = v;
            float d = 10;
            for (; ; ) {
                readch();
                if (!Character.isDigit(peek)) break;
                x = x + Character.digit(peek, 10) / d;
                d = d + 10;
            }
            return new Real(x);
        }

        if (Character.isLetter(peek)) {
            StringBuffer b = new StringBuffer();
            do {
                b.append(peek);
                readch();
            } while (Character.isLetterOrDigit(peek));
            String s = b.toString();
            Word w = (Word) words.get(s);
            if (w != null) return w;
            w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }
        Token tok = new Token(peek);
        peek = ' ';
        return tok;
    }
}
