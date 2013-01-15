package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a list and produces the length of the list as
 * an integer.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Size extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2011_08 + "size",
            StackLibrary.NS_2008_08 + "size",
            StackLibrary.NS_2007_08 + "size",
            StackLibrary.NS_2007_05 + "size"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Size()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "l", "a list", true )};
    }

    public String getComment()
    {
        return "l  =>  n   -- where n is the number of members of l";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		RippleValue l;

		l = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList> listSink = new Sink<RippleList>()
		{
			public void put( final RippleList list ) throws RippleException
			{
				int result = list.length();
				solutions.put(
						rest.push( mc.numericValue(result) ) );
			}
		};

		mc.toList( l, listSink );
	}
}

