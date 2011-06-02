package net.fortytwo.ripple.sail;

import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.impl.BNodeImpl;

/**
 * User: josh
 * Date: 6/2/11
 * Time: 1:07 PM
 */
public class RippleBNode extends BNodeImpl implements RippleSesameValue {
    private RippleList list = null;

    public RippleBNode() {
        super();
    }

    public RippleBNode(String id) {
        super(id);
    }

    @Override
    public RippleList getStack() {
        return list;
    }

    @Override
    public void setStack(final RippleList list) {
        this.list = list;
    }
}
