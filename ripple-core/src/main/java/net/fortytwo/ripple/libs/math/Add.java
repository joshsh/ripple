package net.fortytwo.ripple.libs.math;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackMapping;

/**
 * A primitive which consumes two numbers and produces their sum.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Add extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            MathLibrary.NS_2013_03 + "add",
            MathLibrary.NS_2008_08 + "add",
            MathLibrary.NS_2007_08 + "add",
            MathLibrary.NS_2007_05 + "add"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Add()
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
        return "x y  =>  x + y";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

		RippleList stack = arg;

		NumericValue a, b, result;

		b = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();
		a = mc.toNumericValue( stack.getFirst() );
		stack = stack.getRest();

		result = a.add( b );

		solutions.put(
				stack.push( result ) );
	}

    @Override
    public StackMapping getInverse() throws RippleException
    {
        return MathLibrary.getSubValue();
    }
}

