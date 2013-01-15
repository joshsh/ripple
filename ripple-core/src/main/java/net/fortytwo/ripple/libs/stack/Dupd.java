package net.fortytwo.ripple.libs.stack;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which pushes a copy of the second-to-topmost item on the stack to
 * the head of the stack.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Dupd extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2011_08 + "dupd",
            StackLibrary.NS_2008_08 + "dupd",
            StackLibrary.NS_2007_08 + "dupd",
            StackLibrary.NS_2007_05 + "dupd"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Dupd()
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
        return "x y  =>  x x y";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleValue y, x;
		RippleList stack = arg;

		y = stack.getFirst();
		stack = stack.getRest();
		x = stack.getFirst();
		stack = stack.getRest();

		solutions.put(
				stack.push( x ).push( x ).push( y ) );
	}
}

