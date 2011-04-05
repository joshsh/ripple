package net.fortytwo.ripple.libs.blueprints;

import com.tinkerpop.blueprints.pgm.Element;
import net.fortytwo.ripple.model.keyval.KeyValueValue;

/**
 * User: josh
 * Date: 4/6/11
 * Time: 12:15 AM
 */
public interface ElementValue extends KeyValueValue {
    Element getElement();
}
