package net.fortytwo.ripple.libs.data;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.math.MathLibrary;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which consumes two items x and y and produces a Boolean value of
 * true if x is greater than y according to the natural ordering of x, otherwise
 * false.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Gt extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            DataLibrary.NS_2011_08 + "gt",
            MathLibrary.NS_2008_08 + "gt",
            MathLibrary.NS_2007_08 + "gt",
            MathLibrary.NS_2007_05 + "gt"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Gt()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "x", null, true ),
                new Parameter( "y", null, true )};
    }

    public String getComment()
    {
        return "x y  =>  b  -- where b is true if x > y, otherwise false";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

		RippleList stack = arg;

		RippleValue a, b, result;

		b = stack.getFirst();
		stack = stack.getRest();
		a = stack.getFirst();
		stack = stack.getRest();

		result = mc.booleanValue(mc.getComparator().compare(a, b) > 0);

		solutions.put(
				stack.push( result ) );
	}
}

