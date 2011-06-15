package net.fortytwo.ripple.sail;

import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.Value;

/**
 * User: josh
 * Date: 6/2/11
 * Time: 1:07 PM
 */
public interface RippleSesameValue {
    RippleList getStack();

    void setStack(RippleList list);

    Value getNativeValue();
}
