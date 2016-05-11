package net.fortytwo.ripple.sail;

import net.fortytwo.ripple.model.RippleList;
import org.openrdf.model.Value;
import org.openrdf.model.impl.SimpleBNode;

import java.util.UUID;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleBNode extends SimpleBNode implements RippleSesameValue {
    private RippleList list = null;

    public RippleBNode() {
        super(UUID.randomUUID().toString());
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

    @Override
    public Value getNativeValue() {
        return this;
    }
}
