package net.fortytwo.ripple.libs.stream;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A filter which discards the stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Scrap extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StreamLibrary.NS_2011_08 + "scrap",
            StreamLibrary.NS_2008_08 + "scrap",
            StreamLibrary.NS_2007_08 + "scrap",
            StreamLibrary.NS_2007_05 + "scrap"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Scrap()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {};
    }

    public String getComment()
    {
        return "transmits no stacks";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		// Do nothing.
	}
}

