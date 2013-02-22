package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.Element;
import net.fortytwo.ripple.model.keyval.KeyValueValue;

/**
 * User: josh
 * Date: 4/6/11
 * Time: 12:15 AM
 */
public abstract class ElementValue extends KeyValueValue {
    public abstract Element getElement();
}
