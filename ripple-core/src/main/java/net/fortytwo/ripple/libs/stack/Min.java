package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a list and produces the least item in the list.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Min extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2013_03 + "min",
            StackLibrary.NS_2008_08 + "min",
            StackLibrary.NS_2007_08 + "min"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Min()
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
        return "l  =>  x   -- where x is the least member of l";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        RippleValue l;

		l = arg.getFirst();
		final RippleList rest = arg.getRest();

		Sink<RippleList> listSink = new Sink<RippleList>()
		{
			public void put( RippleList list ) throws RippleException
			{
				RippleValue result = null;
				while ( !list.isNil() )
				{
					RippleValue v = list.getFirst();
					if ( null == result || mc.getComparator().compare( v, result ) < 0 )
					{
						result = v;
					}
		
					list = list.getRest();
				}
		
				if ( null != result )
				{
					solutions.put(
							rest.push( result ) );
				}
			}
		};

		mc.toList( l, listSink );
	}
}

