package org.dynjs.runtime.linker.js;

import static org.dynjs.runtime.linker.LinkerUtils.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.dynjs.codegen.DereferencedReference;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.projectodd.rephract.LinkLogger;
import org.projectodd.rephract.StrategicLink;
import org.projectodd.rephract.StrategyChain;
import org.projectodd.rephract.guards.Guards;
import org.projectodd.rephract.mop.ContextualLinkStrategy;

import com.headius.invokebinder.Binder;

public class JavascriptObjectLinkStrategy extends ContextualLinkStrategy<ExecutionContext> {

    public JavascriptObjectLinkStrategy(LinkLogger logger) {
        super(ExecutionContext.class, logger);
    }

    @Override
    public StrategicLink linkGetProperty(StrategyChain chain, Object receiver, String propName, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        if (isJavascriptObjectReference(receiver)) {
            MethodHandle handle = binder
                    .filter(0, referenceBaseFilter())
                    .convert(Object.class, JSObject.class, ExecutionContext.class, String.class)
                    .invokeVirtual(lookup(), "get");

            MethodHandle guard = javascriptObjectReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }

        if (isJavascriptEnvironmnetReference(receiver)) {
            MethodHandle handle = binder
                    .convert(Object.class, Reference.class, ExecutionContext.class, String.class)
                    .permute(0, 1, 2, 0)
                    .filter(0, referenceBaseFilter())
                    .filter(3, referenceStrictnessFilter())
                    .convert(Object.class, EnvironmentRecord.class, ExecutionContext.class, String.class, boolean.class)
                    .invokeVirtual(lookup(), "getBindingValue");

            MethodHandle guard = javascriptEnvironmentReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }

        return chain.nextStrategy();
    }

    @Override
    public StrategicLink linkSetProperty(StrategyChain chain, Object receiver, String propName, Object value, Binder binder, Binder guardBinder)
            throws NoSuchMethodException, IllegalAccessException {

        if (isJavascriptObjectReference(receiver)) {
            MethodHandle handle = binder
                    .convert(void.class, Reference.class, ExecutionContext.class, String.class, Object.class)
                    .permute(0, 1, 2, 3, 0)
                    .filter(0, referenceBaseFilter())
                    .filter(4, referenceStrictnessFilter())
                    .convert(void.class, JSObject.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .invokeVirtual(lookup(), "put");

            MethodHandle guard = javascriptObjectReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }

        if (isJavascriptEnvironmnetReference(receiver)) {
            MethodHandle handle = binder
                    .convert(void.class, Reference.class, ExecutionContext.class, String.class, Object.class)
                    .permute(0, 1, 2, 3, 0)
                    .filter(0, referenceBaseFilter())
                    .filter(4, referenceStrictnessFilter())
                    .convert(void.class, EnvironmentRecord.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .invokeVirtual(lookup(), "setMutableBinding");

            MethodHandle guard = javascriptEnvironmentReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }

        if (isJavascriptStrictUndefinedReference(receiver) && ((Reference) receiver).isStrictReference()) {
            MethodHandle handle = binder
                    .drop(2, 2)
                    .drop(1)
                    .filter(0, createReferenceErrorFilter())
                    .throwException();

            MethodHandle guard = javascriptStrictUndefinedReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }

        if (isJavascriptUndefinedReference(receiver) && !((Reference) receiver).isStrictReference()) {
            MethodHandle handle = binder
                    .convert(void.class, Reference.class, ExecutionContext.class, String.class, Object.class)
                    .permute(1, 1, 2, 3, 0)
                    .filter(0, globalObjectFilter())
                    .filter(4, referenceStrictnessFilter())
                    .convert(void.class, JSObject.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                    .invokeVirtual(lookup(), "put");

            MethodHandle guard = javascriptUndefinedReferenceGuard(guardBinder);
            return new StrategicLink(handle, guard);
        }


        return chain.nextStrategy();
    }

    @Override
    public StrategicLink linkCall(StrategyChain chain, Object receiver, Object self, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        if (isFunctionDereferencedReference(receiver)) {
            MethodHandle handle = binder
                    .convert(Object.class, DereferencedReference.class, ExecutionContext.class, Object.class, Object[].class)
                    .permute(1, 0, 0, 2, 3)
                    .filter(1, dereferencedReferenceFilter())
                    .filter(2, dereferencedValueFilter())
                    .convert(Object.class, ExecutionContext.class, Object.class, JSFunction.class, Object.class, Object[].class)
                    .invokeVirtual(lookup(), "call");

            MethodHandle guard = functionDereferencedReferenceGuard(guardBinder);

            return new StrategicLink(handle, guard);
        }

        if (receiver instanceof JSFunction) {
            MethodHandle handle = binder
                    .permute(1, 0, 2, 3)
                    .convert(Object.class, ExecutionContext.class, JSFunction.class, Object.class, Object[].class)
                    .invokeVirtual(lookup(), "call");

            //MethodHandle guard = getReceiverClassGuard(JSFunction.class, guardBinder);
            MethodHandle guard = getCallableJSObjectGuard(guardBinder);

            return new StrategicLink(handle, guard);
        }

        if (isJavascriptDereferencedReference(receiver)) {
            MethodHandle handle = binder
                    .drop(0)
                    .drop(1, binder.type().parameterCount() - 1)
                    .convert(Object.class, ExecutionContext.class, Object.class)
                    .fold(createTypeErrorFilter())
                    .drop( 1, 2 )
                    .throwException();

            MethodHandle guard = nonFunctionJavascriptDereferencedReferenceGuard(guardBinder);

            return new StrategicLink(handle, guard);
        }

        if (receiver instanceof JSObject) {
            MethodHandle handle = binder
                    .drop(0)
                    .drop(1, binder.type().parameterCount() - 1)
                    .convert(Object.class, ExecutionContext.class, Object.class)
                    .fold(createTypeErrorFilter())
                    .drop( 1, 2 )
                    .throwException();

            //MethodHandle guard = getReceiverClassGuard(JSObject.class, guardBinder);
            MethodHandle guard = getNoncallableJSObjectGuard(guardBinder);

            return new StrategicLink(handle, guard);
        }

        return chain.nextStrategy();
    }

    @Override
    public StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
            IllegalAccessException {

        if (isFunctionDereferencedReference(receiver)) {
            MethodHandle handle = binder
                    .convert(Object.class, DereferencedReference.class, ExecutionContext.class, Object[].class)
                    .permute(1, 0, 0, 2)
                    .filter(1, dereferencedReferenceFilter())
                    .filter(2, dereferencedValueFilter())
                    .convert(Object.class, ExecutionContext.class, Object.class, JSFunction.class, Object[].class)
                    .invokeVirtual(lookup(), "construct");

            MethodHandle guard = functionDereferencedReferenceGuard(guardBinder);

            return new StrategicLink(handle, guard);
        }

        if (receiver instanceof JSFunction) {
            MethodHandle handle = binder
                    .permute(1, 0, 2)
                    .convert(Object.class, ExecutionContext.class, JSFunction.class, Object[].class)
                    .insert(1, new Class[]{Object.class}, new Object[]{null})
                    .invokeVirtual(lookup(), "construct");

            //MethodHandle guard = getReceiverClassGuard(JSFunction.class, guardBinder);
            MethodHandle guard = getCallableJSObjectGuard(guardBinder);

            return new StrategicLink(handle, guard);
        }

        if (receiver instanceof JSObject) {
            MethodHandle handle = binder
                    .drop(0)
                    .drop(1, binder.type().parameterCount() - 1)
                    .convert(Object.class, ExecutionContext.class, Object.class)
                    .fold(createTypeErrorFilter())
                    .drop(1,2)
                    .throwException();

            //MethodHandle guard = getReceiverClassGuard(JSObject.class, guardBinder);
            MethodHandle guard = getNoncallableJSObjectGuard(guardBinder);

            return new StrategicLink(handle, guard);
        }

        return chain.nextStrategy();
    }

    public static boolean callableJSObjectGuard(Object receiver) {
        return (receiver instanceof JSFunction);
    }

    public static boolean noncallableJSObjectGuard(Object receiver) {
        return ((receiver instanceof JSObject) && (!(receiver instanceof JSFunction)));
    }

    public static MethodHandle getCallableJSObjectGuard(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        MethodHandle mh = MethodHandles.lookup().findStatic(JavascriptObjectLinkStrategy.class, "callableJSObjectGuard",
                MethodType.methodType(boolean.class, Object.class));

        return binder
                .drop(1, binder.type().parameterCount() - 1)
                .invoke(mh);
    }

    public static MethodHandle getNoncallableJSObjectGuard(Binder binder) throws NoSuchMethodException, IllegalAccessException {
        MethodHandle mh = MethodHandles.lookup().findStatic(JavascriptObjectLinkStrategy.class, "noncallableJSObjectGuard",
                MethodType.methodType(boolean.class, Object.class));

        return binder
                .drop(1, binder.type().parameterCount() - 1)
                .invoke(mh);
    }

    public static MethodHandle dereferencedReferenceFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JavascriptObjectLinkStrategy.class, "dereferencedReferenceFilter",
                MethodType.methodType(Reference.class, DereferencedReference.class));
    }

    public static Reference dereferencedReferenceFilter(DereferencedReference deref) {
        return deref.getReference();
    }

    public static MethodHandle dereferencedValueFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JavascriptObjectLinkStrategy.class, "dereferencedValueFilter",
                MethodType.methodType(Object.class, DereferencedReference.class));
    }

    public static Object dereferencedValueFilter(DereferencedReference deref) {
        return deref.getValue();
    }

    public static MethodHandle createTypeErrorFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JavascriptObjectLinkStrategy.class, "createTypeErrorFilter",
                MethodType.methodType(ThrowException.class, ExecutionContext.class, Object.class));
    }

    public static ThrowException createTypeErrorFilter(ExecutionContext context, Object nonCallable) {
        return new ThrowException(context, context.createTypeError("not callable: " + nonCallable));
    }

    public static MethodHandle createReferenceErrorFilter() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(JavascriptObjectLinkStrategy.class, "createReferenceErrorFilter",
                MethodType.methodType(ThrowException.class, ExecutionContext.class));
    }

    public static ThrowException createReferenceErrorFilter(ExecutionContext context) {
        return new ThrowException(context, context.createTypeError("not referenceable"));
    }

    /*
     * @Override
     * public StrategicLink linkConstruct(StrategyChain chain, Object receiver, Object[] args, Binder binder, Binder guardBinder) throws NoSuchMethodException,
     * IllegalAccessException {
     * // TODO Auto-generated method stub
     * return super.linkConstruct(chain, receiver, args, binder, guardBinder);
     * }
     */

}
