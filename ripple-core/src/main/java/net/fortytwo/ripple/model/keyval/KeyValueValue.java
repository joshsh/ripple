package net.fortytwo.ripple.model.keyval;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;

import java.util.Collection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class KeyValueValue implements RippleValue, Comparable<KeyValueValue> {
    public abstract RippleValue getValue(String key,
                                         ModelConnection mc) throws RippleException;

    public abstract Collection<String> getKeys();

    public Type getType() {
        return Type.KEYVALUE_VALUE;
    }
}
