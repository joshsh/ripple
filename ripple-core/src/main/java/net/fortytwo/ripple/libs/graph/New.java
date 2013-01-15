package net.fortytwo.ripple.libs.graph;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.util.ModelConnectionHelper;

/**
 * A primitive which produces a new blank node.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class New extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2011_08 + "new",
            GraphLibrary.NS_2008_08 + "new",
            GraphLibrary.NS_2007_08 + "new",
            GraphLibrary.NS_2007_05 + "new"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public New()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {};
    }

    public String getComment()
    {
        return "n  -- where n is a new blank node";
    }

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

		RippleList stack = arg;

		// Note: stack may be null (and this should not be a problem).
		RippleList result = stack.push(
			new ModelConnectionHelper(mc).createRandomURI() );
//System.out.println( "Creating a new node" );

		solutions.put( result );
	}
}

