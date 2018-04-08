package com.zhl.compile.frontend.inter;

import com.zhl.compile.frontend.lexer.Tag;
import com.zhl.compile.frontend.lexer.Word;
import com.zhl.compile.frontend.symbols.Type;

/**
 * Created by zhuhailong-dc on 2018/4/2.
 * 在生成类Access的代码时演示了方法emitjumps的另一种用法。
 * 源语言允许把布尔值赋给标识符和数组元素，因此一个布尔表达式可能是一个数组访问。
 * 类Access有一个方法gen用来生成“正常”代码，另一个方法jumping用来生成跳转代码。
 */
public class Access extends Op {

    public Id array;
    public Expr index;

    /**
     * @param a : 平坦化的数组a
     * @param i ： 一个下标i
     * @param p : 该数组的元素类型p
     */
    public Access(Id a, Expr i, Type p) {
        super(new Word("[]", Tag.INDEX), p);
    }

    public Expr gen() {
        return new Access(array, index.reduce(), type);
    }

    /**
     * 把这个数组访问归约为一个临时变量后调用emitjumps
     * 在生成数组地址计算代码的过程中完成了类型检查
     *
     * @param t
     * @param f
     */
    public void jumping(int t, int f) {
        emitJumps(reduce().toString(), t, f);
    }

    public String toString() {
        return array.toString() + " [ " + index.toString() + " ] ";
    }



}
