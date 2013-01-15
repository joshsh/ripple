package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;

/**
 *  A filter which yields no results, regardless of its inputs.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class NullStackMapping implements StackMapping
{
	public int arity()
	{
		return 0;
	}

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
        // Do nothing.
    }
	
	public boolean isTransparent()
	{
		return true;
	}

    // The null filter is considered to be its own inverse
    public StackMapping getInverse() throws RippleException
    {
        return this;
    }

    @Override
    public String toString() {
        return "[NullStackMapping]";
    }
}

