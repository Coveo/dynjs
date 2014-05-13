package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public class AdditiveExpression extends AbstractBinaryExpression {

    public AdditiveExpression(final Expression lhs, final Expression rhs, final String op) {
        super(lhs, rhs, op);
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict);
    }

}
