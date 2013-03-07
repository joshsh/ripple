package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number and produces its arc sine (if defined),
 * in the range of -pi/2 through pi/2.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Asin extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "asin",
            MathLibrary.NS_2008_08 + "asin",
            MathLibrary.NS_2007_08 + "asin"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Asin()
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
        return "x  =>  asin(x)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

		RippleList stack = arg;

		double a;
		NumericValue result;

		a = mc.toNumericValue( stack.getFirst() ).doubleValue();
		stack = stack.getRest();

		// Apply the function only if it is defined for the given argument.
		if ( a >= -1 && a <= 1 )
		{
			result = mc.numericValue(Math.asin(a));

			solutions.put(
					stack.push( result ) );
		}
	}

    @Override
    public StackMapping getInverse() throws RippleException
    {
        return MathLibrary.getSinValue();
    }
}

