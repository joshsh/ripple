package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;

/**
 * A primitive which consumes a number and produces its hyperbolic tangent.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Tanh extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "tanh",
            MathLibrary.NS_2008_08 + "tanh",
            MathLibrary.NS_2007_08 + "tanh"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Tanh()
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
        return "x  =>  tanh(x)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		NumericValue a, result;

		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = mc.numericValue(Math.tanh(a.doubleValue()));

		solutions.put(
				stack.push( result ) );	}
}

