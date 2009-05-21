/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stack;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Operator;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which consumes a list, an "initial value" and a filter, then
 * produces the result of folding the list with the given filter and initial
 * value.  For instance, <code>(1 2 3) 0 add /fold</code> yields 0 + 1 + 2 + 3
 * = 6.
 */
public class Fold extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            StackLibrary.NS_2008_08 + "fold",
            StackLibrary.NS_2007_08 + "fold",
            StackLibrary.NS_2007_05 + "fold"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Fold()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "l", "a list through which to fold p", true ),
                new Parameter( "v0", "initial value", true ),
                new Parameter( "p", "a binary function", true )};
    }

    public String getComment()
    {
        return "l v0 p  =>  v  -- starting with value v0, sequentially push members of List l and combine with binary operator p";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		RippleValue l;

		final RippleValue f = stack.getFirst();
		stack = stack.getRest();
		final RippleValue v = stack.getFirst();
		stack = stack.getRest();
		l = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<RippleList, RippleException> listSink = new Sink<RippleList, RippleException>()
		{
			public void put( final RippleList list ) throws RippleException
			{
				RippleList lList = list.invert();
				RippleList result = rest.push( v );
		
				while ( !lList.isNil() )
				{
					result = result.push( lList.getFirst() )
						.push( f )
						.push( Operator.OP );
					lList = lList.getRest();
				}
		
				solutions.put( arg.with( result ) );
			}
		};

		mc.toList( l, listSink );
	}
}

