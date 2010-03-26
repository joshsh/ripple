/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;

/**
 * A primitive which consumes a subject, predicate and object, then produces the
 * subject after adding the corresponding RDF statement to the triple store.
 */
public class Assert extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "assert",
            GraphLibrary.NS_2007_08 + "assert",
            GraphLibrary.NS_2007_05 + "assert"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Assert()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "s", "the subject of the assertion", false ),
                new Parameter( "p", "the predicate of the assertion", true ),
                new Parameter( "o", "the object of the assertion", true )};
    }

    public String getComment()
    {
        return "s p o  =>  s  -- has the side-effect of asserting the statement (s, p, o)";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue subj, pred, obj;

		obj = stack.getFirst();
		stack = stack.getRest();
		pred = stack.getFirst();
		stack = stack.getRest();
		subj = stack.getFirst();
		stack = stack.getRest();

		mc.add( subj, pred, obj );

		// TODO: store added and removed statements in a buffer until the
		// ModelConnection commits.  You may not simply wait to commit,
		// as writing and then reading without first committing may result
		// in a deadlock.  The LinkedDataSail already does this sort of
		// buffering, which is why it does not deadlock w.r.t. its base
		// Sail.
		mc.commit();

		solutions.put( arg.with( stack.push( subj ) ) );
	}
}

