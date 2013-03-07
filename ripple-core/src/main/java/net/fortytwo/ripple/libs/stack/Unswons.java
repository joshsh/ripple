package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a list and produces the rest of the list, followed
 * by the first item in the list.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Unswons extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2013_03 + "unswons",
            StackLibrary.NS_2008_08 + "unswons",
            StackLibrary.NS_2007_08 + "unswons",
            StackLibrary.NS_2007_05 + "unswons"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Unswons()
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
        return "l  =>  r f  -- where f is the first member of l and r is the rest of l";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleValue l;

		l = arg.getFirst();
		final RippleList rest = arg.getRest();

		Sink<RippleList> listSink = new Sink<RippleList>()
		{
			public void put( final RippleList list ) throws RippleException
			{
				solutions.put(
						rest.push( list.getRest() ).push(list.getFirst()) );
			}
		};

		mc.toList( l, listSink );
	}
}

