package com.zhl.rules.antlr.labeled;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by zhuhailong-dc on 2018/4/30.
 * 对整个语法树进行遍历，然后进行相关的操作。
 */
public class EvalVisitor extends LabeledExprBaseVisitor<Integer> {

    private Map<String, Integer> memory = new HashedMap();

    @Override
    public Integer visitAssign(LabeledExprParser.AssignContext ctx) {
        String id = ctx.ID().getText(); // id is left-hand side of '='
        int value = visit(ctx.expr()); // compute valueof expression on right
        memory.put(id, value);
        return value;
    }

    @Override
    public Integer visitPrintExpr(LabeledExprParser.PrintExprContext ctx) {
        Integer value = visit(ctx.expr());
        System.out.println(value);
        return 0;
    }

    @Override
    public Integer visitInt(LabeledExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitParens(LabeledExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Integer visitId(LabeledExprParser.IdContext ctx) {
        String id = ctx.ID().getText();
        if (memory.containsKey(id)) return memory.get(id);
        return 0;
    }

    @Override
    public Integer visitMulDiv(LabeledExprParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if (ctx.op.getType() == LabeledExprParser.MUL) {
            return left * right;
        }
        return left / right;
    }

    @Override
    public Integer visitAddSub(LabeledExprParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0)); // get value ofleft subexpression
        int right = visit(ctx.expr(1)); // get value ofright subexpression
        if (ctx.op.getType() == LabeledExprParser.ADD) return left + right;
        return left - right; // must be SUB
    }
}
