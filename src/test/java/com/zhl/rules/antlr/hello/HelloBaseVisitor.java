// Generated from D:/zhuhailong-dc/workspace/ruleExpress/src/test/java/com/zhl/rules/antlr\Hello.g4 by ANTLR 4.7
package com.zhl.rules.antlr.hello;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link HelloVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public class HelloBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements HelloVisitor<T> {
    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    public T visitR(HelloParser.RContext ctx) {
        return visitChildren(ctx);
    }
}