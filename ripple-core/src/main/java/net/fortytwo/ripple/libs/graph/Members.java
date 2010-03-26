/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;

/**
 * A primitive which consumes an RDF container and produces all items in the
 * container.
 */
public class Members extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "members",
            GraphLibrary.NS_2007_08 + "contains",
            GraphLibrary.NS_2007_05 + "contains"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Members() throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "c", "an RDF Container; for instance, a Bag", true )};
    }

    public String getComment()
    {
        return "c  =>  x  -- for each member x of Container c";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue head = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleValue, RippleException> pushSink = new Sink<RippleValue, RippleException>()
		{
			public void put( final RippleValue v ) throws RippleException
			{
				solutions.put( arg.with( rest.push( v ) ) );
			}
		};		
		
		mc.putContainerMembers( head, pushSink );
	}
}

