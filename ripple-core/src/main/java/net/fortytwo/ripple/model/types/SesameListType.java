package net.fortytwo.ripple.model.types;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleType;
import net.fortytwo.ripple.model.StackMapping;
import net.fortytwo.ripple.model.impl.sesame.SesameList;
import org.openrdf.model.Value;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class SesameListType extends SimpleType<SesameList> {

    public SesameListType() {
        super(SesameList.class);
    }

    @Override
    public boolean isInstance(SesameList instance) {
        return true;
    }

    @Override
    public Value toRDF(SesameList instance, ModelConnection mc) throws RippleException {
        return instance.getRDFEquivalent(mc);
    }

    @Override
    public StackMapping getMapping(SesameList instance) {
        return null;
    }

    @Override
    public void print(SesameList instance, RipplePrintStream p, ModelConnection mc) throws RippleException {
        instance.printTo(p, mc, true);
    }

    @Override
    public Category getCategory() {
        return RippleType.Category.LIST;
    }
}
