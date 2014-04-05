package net.fortytwo.ripple.libs.string;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes a string and suffix, producing a Boolean value of
 * true if the given string ends with the given suffix, otherwise false.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class EndsWith extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2013_03 + "ends-with",
            StringLibrary.NS_2008_08 + "endsWith",
            StringLibrary.NS_2007_08 + "endsWith"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public EndsWith()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "s", null, true ),
                new Parameter( "suffix", null, true )};
    }

    public String getComment()
    {
        return "s suffix  =>  b -- where b is true if the given string ends with the given suffix, otherwise false";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		String affix, s;
		RippleValue result;

		affix = mc.toString( stack.getFirst() );
		stack = stack.getRest();
		s = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		result = mc.valueOf(s.endsWith(affix));
        
        solutions.put(
				stack.push( result ) );
	}
}

