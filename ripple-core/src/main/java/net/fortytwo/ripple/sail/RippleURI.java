package net.fortytwo.ripple.sail;

import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.Value;
import org.openrdf.model.impl.SimpleIRI;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleURI extends SimpleIRI implements RippleSesameValue {
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
