package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleType;
import org.json.JSONException;

import java.util.Collection;
import java.util.Comparator;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class KeyValueType<T> extends SimpleType<T> {

    protected KeyValueType(Class clazz) {
        super(clazz);
    }

    @Override
    public RippleType.Category getCategory() {
        return RippleType.Category.KEYVALUE_VALUE;
    }

    public abstract Collection<String> getKeys(T instance);

    public abstract Object getValue(T instance, String key, ModelConnection mc) throws RippleException;
}
