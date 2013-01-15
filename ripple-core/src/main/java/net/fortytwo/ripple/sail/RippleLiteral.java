package net.fortytwo.ripple.sail;

import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LiteralImpl;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleLiteral extends LiteralImpl implements RippleSesameValue, Resource {
    private RippleList list = null;

    public RippleLiteral(String label) {
        super(label);
    }

    public RippleLiteral(String label, String language) {
        super(label, language);
    }

    public RippleLiteral(String label, URI datatype) {
        super(label, datatype);
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
        return null != this.getLanguage()
                ? new LiteralImpl(this.getLabel(), this.getLanguage())
                : null != this.getDatatype()
                ? new LiteralImpl(this.getLabel(), this.getDatatype())
                : new LiteralImpl(this.getLabel());
    }
}
