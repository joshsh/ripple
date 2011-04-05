package net.fortytwo.ripple.model.keyval;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;

import java.util.Collection;

/**
 * User: josh
 * Date: 4/5/11
 * Time: 11:09 PM
 */
public interface KeyValueValue extends RippleValue, Comparable<KeyValueValue> {
    RippleValue getValue(String key,
                         ModelConnection mc) throws RippleException;
    Collection<String> getKeys();
}
