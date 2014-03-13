package net.fortytwo.ripple.libs.data;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.graph.GraphLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

import java.util.logging.Logger;

/**
 * A primitive which consumes a literal value and produces its xsd:integer
 * equivalent (if any).
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToInteger extends PrimitiveStackMapping
{
	private static final Logger LOGGER
		= Logger.getLogger( ToInteger.class.getName() );

    private static final String[] IDENTIFIERS = {
            DataLibrary.NS_2013_03 + "to-integer",
            GraphLibrary.NS_2008_08 + "toInteger",
            GraphLibrary.NS_2007_08 + "toInteger",
            GraphLibrary.NS_2007_05 + "toInteger"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public ToInteger()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true )};
    }

    public String getComment()
    {
        return "x  =>  x as integer literal";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

		RippleList stack = arg;

		String s;

		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		int i;

		try
		{
			i = new Integer( s ).intValue();
		}

		catch ( NumberFormatException e )
		{
			LOGGER.fine( "bad integer value: " + s );
			return;
		}

		solutions.put(
				stack.push( mc.numericValue(i) ) );
	}
}

