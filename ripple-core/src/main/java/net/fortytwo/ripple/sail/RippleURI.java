package net.fortytwo.ripple.sail;

import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;

/**
 * User: josh
 * Date: 6/2/11
 * Time: 1:06 PM
 */
public class RippleURI extends URIImpl implements RippleSesameValue {
    private RippleList list = null;

    public RippleURI(String uriString) {
        super(uriString);
    }

    @Override
    public RippleList getStack() {
        return list;
    }

    @Override
    public void setStack(final RippleList list) {
        this.list = list;
    }

    @Override
    public Value getNativeValue() {
        return this;
    }
}
