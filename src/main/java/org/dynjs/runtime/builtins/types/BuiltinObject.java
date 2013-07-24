package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.object.Create;
import org.dynjs.runtime.builtins.types.object.DefineProperties;
import org.dynjs.runtime.builtins.types.object.DefineProperty;
import org.dynjs.runtime.builtins.types.object.Freeze;
import org.dynjs.runtime.builtins.types.object.GetOwnPropertyDescriptor;
import org.dynjs.runtime.builtins.types.object.GetOwnPropertyNames;
import org.dynjs.runtime.builtins.types.object.GetPrototypeOf;
import org.dynjs.runtime.builtins.types.object.IsExtensible;
import org.dynjs.runtime.builtins.types.object.IsFrozen;
import org.dynjs.runtime.builtins.types.object.IsSealed;
import org.dynjs.runtime.builtins.types.object.Keys;
import org.dynjs.runtime.builtins.types.object.PreventExtensions;
import org.dynjs.runtime.builtins.types.object.Seal;
import org.dynjs.runtime.builtins.types.object.prototype.*;
import org.dynjs.runtime.builtins.types.object.prototype.rhino.DefineGetter;
import org.dynjs.runtime.builtins.types.object.prototype.rhino.DefineSetter;
import org.dynjs.runtime.builtins.types.object.prototype.rhino.LookupGetter;

public class BuiltinObject extends AbstractBuiltinType {

    public BuiltinObject(final GlobalObject globalObject) {
        super(globalObject, "value");

        final JSObject proto = new DynObject(globalObject);
        setPrototypeProperty( proto );
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        // Object.prototype.foo()
        defineNonEnumerableProperty(proto, "constructor", this );
        
        defineNonEnumerableProperty(proto, "toString", new ToString(globalObject) );
        defineNonEnumerableProperty(proto, "toLocaleString", new ToLocaleString(globalObject) );
        defineNonEnumerableProperty(proto, "hasOwnProperty", new HasOwnProperty(globalObject) );
        defineNonEnumerableProperty(proto, "isPrototypeOf", new IsPrototypeOf(globalObject) );
        defineNonEnumerableProperty(proto, "propertyIsEnumerable", new PropertyIsEnumerable(globalObject) );
        defineNonEnumerableProperty(proto, "valueOf", new ValueOf(globalObject) );
        if (globalObject.getConfig().isRhinoCompatible()) {
            // TODO: this is probably not an exhaustive list of rhino-specific functions, but it will do for a start
            defineNonEnumerableProperty(proto, "__defineGetter__", new DefineGetter(globalObject));
            defineNonEnumerableProperty(proto, "__defineSetter__", new DefineSetter(globalObject));
            defineNonEnumerableProperty(proto, "__lookupGetter__", new LookupGetter(globalObject));
        }

        // Object.foo
        defineNonEnumerableProperty(this, "getPrototypeOf", new GetPrototypeOf(globalObject) );
        defineNonEnumerableProperty(this, "getOwnPropertyDescriptor", new GetOwnPropertyDescriptor(globalObject) );
        defineNonEnumerableProperty(this, "getOwnPropertyNames", new GetOwnPropertyNames(globalObject) );
        defineNonEnumerableProperty(this, "create", new Create(globalObject) );
        defineNonEnumerableProperty(this, "defineProperty", new DefineProperty(globalObject) );
        defineNonEnumerableProperty(this, "defineProperties", new DefineProperties(globalObject) );
        defineNonEnumerableProperty(this, "seal", new Seal(globalObject) );
        defineNonEnumerableProperty(this, "freeze", new Freeze(globalObject) );
        defineNonEnumerableProperty(this, "preventExtensions", new PreventExtensions(globalObject) );
        defineNonEnumerableProperty(this, "isSealed", new IsSealed(globalObject) );
        defineNonEnumerableProperty(this, "isFrozen", new IsFrozen(globalObject) );
        defineNonEnumerableProperty(this, "isExtensible", new IsExtensible(globalObject) );
        defineNonEnumerableProperty(this, "keys", new Keys(globalObject) );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {

        if ( args[0] != Types.UNDEFINED ) {
            if (args[0] instanceof JSObject) {
                return args[0];
            }
            if (args[0] instanceof String || args[0] instanceof Boolean || args[0] instanceof Number) {
                JSObject result = Types.toObject(context, args[0]);
                return result;
            }
        }

        return new DynObject(context.getGlobalObject() );
    }

    public static DynObject newObject(ExecutionContext context) {
        BuiltinObject ctor = (BuiltinObject) context.getGlobalObject().get(context, "__Builtin_Object");
        return (DynObject) context.construct(ctor);
    }

}
