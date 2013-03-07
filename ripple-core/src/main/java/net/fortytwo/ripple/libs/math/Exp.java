package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number and produces Euler's number e raised to
 * the power of that number.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Exp extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "exp",
            MathLibrary.NS_2008_08 + "exp",
            MathLibrary.NS_2007_08 + "exp"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Exp()
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
        return "x  =>  exp(x)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		NumericValue a, result;

		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = mc.numericValue(Math.exp(a.doubleValue()));

		solutions.put(
				stack.push( result ) );
	}

    @Override
    public StackMapping getInverse() throws RippleException
    {
        return MathLibrary.getLogValue();
    }
}

