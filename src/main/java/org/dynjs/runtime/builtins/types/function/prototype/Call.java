package org.dynjs.runtime.builtins.types.function.prototype;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;

public class Call extends AbstractNonConstructorFunction {
    public static final Object[] EMPTY_ARRAY = new Object[0];

    public Call(GlobalObject globalObject) {
        super(globalObject, "thisArg");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.4.4
        if (!(self instanceof JSFunction)) {
            throw new ThrowException(context, context.createTypeError("Function.call() only allowed on callable objects"));
        }

        Object thisArg = args[0];
        Object[] argList;

        if (args.length > 1) {
            argList = new Object[args.length - 1];
            for (int i = 0; i < argList.length; ++i) {
                argList[i] = args[i + 1];
            }
        } else {
            argList = EMPTY_ARRAY;
        }

        return context.call((JSFunction) self, thisArg, argList);
    }

}
