package net.fortytwo.ripple.sail;

import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.Value;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface RippleSesameValue {
    RippleList getStack();

    void setStack(RippleList list);

    Value getNativeValue();
}
