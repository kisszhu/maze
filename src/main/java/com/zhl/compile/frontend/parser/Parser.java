package com.zhl.compile.frontend.parser;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import com.zhl.compile.frontend.inter.*;
import com.zhl.compile.frontend.lexer.*;
import com.zhl.compile.frontend.symbols.Array;
import com.zhl.compile.frontend.symbols.Env;
import com.zhl.compile.frontend.symbols.Type;
import scala.collection.immutable.Stream;

import java.io.IOException;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 语法分析器
 */
public class Parser {

    private Lexer lex; // 这个语法分析器的词法分析器
    private Token look; // 向前看词法单元
    Env top = null; // 向前或顶层的符号表
    int used = 0; // 用于变量声明的存储位置

    public Parser(Lexer l) throws IOException {
        lex = l;
        move();
    }

    void move() throws IOException {
        look = lex.scan();
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) throws IOException {
        if (look.tag == t) move();
        else error("syntax error");
    }

    /**
     * 类Parser对每个非终结符号有一个过程。消除源语言文法中的左递归后可以得到
     * 一个新的文法。这些过程就是基于这个新文法创建的。
     */

    /**
     * 语法分析过程首先调用了过程program
     * 这个过程又调用了block()来对输入流进行语法分析，并构建出抽象语法树
     */
    public void program() throws IOException {
        // program -> block
        Stmt s = block();

        // 下面的代码 生成了中间代码
        int begin = s.newlabel();
        int after = s.newlabel();
        s.emitlabel(begin);
        s.gen(begin, after);
        s.emitlabel(after);
    }

    /**
     * 对符号表的处理过程明确显示在过程block中。
     *
     * @return
     * @throws IOException
     */
    Stmt block() throws IOException {
        match('f');
        // 变量top存放了最顶层的符号表
        Env savedEnv = top;
        top = new Env(top);

        decls();
        Stmt s = stmts();

        match('}');
        // 变量savedEnv是一个指向前面的符号表的连接
        top = savedEnv;
        return s;
    }

    /**
     *
     */
    void decls() throws IOException {
        while (look.tag== Tag.BASIC){ // D -> type ID
            Type p=type();
            Token tok=look;
            match(Tag.ID);
            match(';');
            Id id=new Id((Word)tok,p,used);
            top.put(tok,id);
            used=used+p.width;
        }
    }

    Type type() throws IOException{
        Type p=(Type)look; // 期望 look.tag=Tag.BASIC
        match(Tag.BASIC);
        if(look.tag!='[') return p; // T -> basic
        else return dims(p); // 返回数组类型
    }

    Type dims(Type p) throws IOException{
        match('[');
        Token tok=look;
        match(Tag.NUM);
        match(']');
        if(look.tag=='[')
            p=dims(p);
        return new Array(((Num)look).value,p);
    }

    /**
     * 过程stmt有一个switch语句。这个语句的各个case分支对应于非
     * 终结符号Stmt的的各个产生式。
     * 每个case分支都是用构造函数来建立某个构造对应的节点。
     * 当语法分析器碰到while语句和do语句的开始关键字的时候，就会创建这些语句的结点。
     * 这些结点在相应语句执行完语法分析之前就构造出来，这可以使得任何内层的
     * break语句回指到它的外层循环语句。
     */
    Stmt stmts() throws IOException{
        if(look.tag=='}') return Stmt.Null;
        else return new Seq(stmt(),stmts());
    }

    Stmt stmt() throws IOException{
        Expr x;
        Stmt s,s1,s2;
        Stmt savedStmt; // 用于为break语句保存外层的循环语句
        switch (look.tag){
            case ';':
                move();
                return Stmt.Null;
            case Tag.IF:
                match(Tag.IF);
                match('(');
                x=bool();
                match(')');
                s1=stmt();
                if(look.tag!=Tag.ELSE) return new If(x,s1);
                match(Tag.ELSE);
                s2=stmt();
                return new Else(x,s1,s2);
            case Tag.WHILE:
                While whilenode=new While();
                savedStmt=Stmt.Enclosing;Stmt.Enclosing=whilenode;
                match(Tag.WHILE);match('(');x=bool();match(')');
                s1=stmt();
                whilenode.init(x,s1);
                Stmt.Enclosing=savedStmt; // 重置Stmt.Enclosing
                return whilenode;
            case Tag.DO:
                Do donode=new Do();
                savedStmt=Stmt.Enclosing;Stmt.Enclosing=donode;
                match(Tag.DO);
                s1=stmt();
                match(Tag.WHILE);match('(');x=bool();match(')');match(';');
                donode.init(s1,x);
                Stmt.Enclosing=savedStmt;  // 重置Stmt.Enclosing
                return donode;
            case Tag.BREAK:
                match(Tag.BREAK);match(';');
                return new Break();
            case '{':
                return block();
                default:
                    return assign();
        }
    }

    /**
     * 为方便起见，赋值语句的代码出现在一个辅助过程assign中
     */
    Stmt assign() throws IOException{
        Stmt stmt;Token t=look;
        match(Tag.ID);
        Id id=top.get(t);
        if(id==null) error(t.toString()+" undeclared");
        if(look.tag=='='){ // S -> id=E
            move();stmt=new Set(id,bool());
        }else{
            Access x=offset(id);
            match('=');stmt=new SetElem(x,bool());
        }
        match(';');
        return stmt;
    }

    Expr bool() throws IOException{
        Expr x=join();
        while(look.tag==Tag.OR){
            Token tok=look;move();x=new Or(tok,x,join());
        }
        return x;
    }

    Expr join() throws IOException{
        Expr x=equality();
        while(look.tag==Tag.AND){
            Token tok=look;move();x=new And(tok,x,equality());
        }
        return x;
    }

    Expr equality() throws IOException{
        Expr x=rel();
        while(look.tag==Tag.EQ || look.tag==Tag.NE){
            Token tok=look;move();x=new Rel(tok,x,rel());
        }
        return x;
    }


    Expr rel() throws IOException{
        Expr x=expr();
        switch (look.tag){
            case '<':case Tag.LE:case Tag.GE:case '>':
                Token tok=look;move();return new Rel(tok,x,expr());
            default:
                return x;
        }
    }

    Expr expr() throws IOException{
        Expr x=term();
        while(look.tag=='+' || look.tag=='-'){
            Token tok=look;move();x=new Arith(tok,x,term());
        }
        return x;
    }

    Expr term() throws IOException{
        Expr x=unary();
        while(look.tag=='+' || look.tag=='/'){
            Token tok=look;move();x=new Arith(tok,x,unary());
        }
        return x;
    }

    Expr unary() throws IOException{
        if(look.tag=='-'){
            move();return new Unary(Word.minux,unary());
        }else if(look.tag=='!'){
            Token tok=look;move();return new Not(tok,unary());
        }else return factor();
    }

    /**
     * 在语法分析器中的其余代码处理表达式"因子"
     */
    Expr factor() throws IOException{
        Expr x=null;
        switch (look.tag){
            case '(':
                move();x=bool();match(')');
                return x;
            case Tag.NUM:
                x=new Constant(look,Type.Int);move();return x;
            case Tag.REAL:
                x=new Constant(look,Type.Float);move();return x;
            case Tag.TRUE:
                x= Constant.True;move();return x;
            case Tag.FALSE:
                x= Constant.False;move();return x;
            default:
                error("syntax error");
                return x;
            case Tag.ID:
                String s=look.toString();
                Id id=top.get(look);
                if(id==null) error(look.toString()+" undeclared");
                move();
                if(look.tag!='[') return id;
                else return offset(id);
        }
    }

    /**
     * 辅助过程offset为数组地址计算生成代码
     */
    Access offset(Id a) throws IOException{ // I -> [E] | [E] I
        Expr i;Expr w;Expr t1,t2;Expr loc; // 继承id
        Type type=a.type;
        match('[');i=bool();match(']');
        type=((Array)type).of;
        w=new Constant(type.width);
        t1=new Arith(new Token('*'),i,w);
        loc=t1;
        while(look.tag=='['){ // 多维下标 I -> [E]I
            match('['); i=bool();match(']');
            type=((Array)type).of;
            w=new Constant(type.width);
            t1=new Arith(new Token('*'),i,w);
            t2=new Arith(new Token('*'),loc,t1);
            loc=t2;
        }
        return new Access(a,loc,type);
    }
}

