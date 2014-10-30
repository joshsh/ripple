package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.Element;
import net.fortytwo.ripple.model.keyval.KeyValueValue;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class ElementValue extends KeyValueValue {
    public abstract Element getElement();
}
