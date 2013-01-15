package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a string, maps its characters to lower case, and
 * produces the result.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ToLowerCase extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2011_08 + "to-lower-case",
            StringLibrary.NS_2008_08 + "toLowerCase",
            StringLibrary.NS_2007_08 + "toLowerCase"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public ToLowerCase()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "s", null, true )};
    }

    public String getComment()
    {
        return "s  =>  s2 -- where s2 is equal to s with all characters converted to lower case";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		RippleValue s = stack.getFirst();
		stack = stack.getRest();

        String result = mc.toString( s ).toLowerCase();

		solutions.put(
				stack.push( StringLibrary.value( result, mc, s ) ) );
	}
}

