package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes a number representing an angle in radians and
 * produces its cosine.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Cos extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "cos",
            MathLibrary.NS_2008_08 + "cos",
            MathLibrary.NS_2007_08 + "cos"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Cos()
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
        return "x  =>  cos(x)";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		NumericValue a, result;

		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = mc.numericValue(Math.cos(a.doubleValue()));

		solutions.put(
				stack.push( result ) );
	}

    @Override
    public StackMapping getInverse() throws RippleException
    {
        return MathLibrary.getAcosValue();
    }
}

