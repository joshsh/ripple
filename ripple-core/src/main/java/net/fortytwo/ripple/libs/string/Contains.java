/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.etc.EtcLibrary;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.flow.Sink;

/**
 * A primitive which tells whether a string contains another string as a
 * substring.
 */
public class Contains extends PrimitiveStackMapping
{
	private static final int ARITY = 2;

    private static final String[] IDENTIFIERS = {
            StringLibrary.NS_2008_08 + "contains"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Contains() throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void apply( final StackContext arg,
                       final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		RippleList stack = arg.getStack();
		final ModelConnection mc = arg.getModelConnection();

		String b = mc.toString( stack.getFirst() );
		stack = stack.getRest();
		String a = mc.toString( stack.getFirst() );
		stack = stack.getRest();

		RDFValue result = mc.value( a.contains( b ) );

		solutions.put( arg.with(
				stack.push( result ) ) );
	}
}