package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public abstract class PropertyAssignment {

    private String name;

    public PropertyAssignment(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract Object accept(Object context, CodeVisitor visitor, boolean strict);

    public abstract int getSizeMetric();

}
