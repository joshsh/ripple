/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.flow.Sink;

import org.openrdf.model.Value;
import org.openrdf.model.Literal;

/**
 * A primitive which consumes a plain literal value and produces its language
 * tag (or an empty string if the literal has no language tag).
 */
public class Lang extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_XML + "lang"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Lang()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "l", null, true )};
    }

    public String getComment()
    {
        return "l  =>  language tag of literal l";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		Value v;
		String result;

		v = stack.getFirst().toRDF( mc ).sesameValue();
		stack = stack.getRest();

		if ( v instanceof Literal )
		{
			result = ( (Literal) v ).getLanguage();

			if ( null == result )
			{
				result = "";
			}

			solutions.put( arg.with(
					stack.push( mc.value( result ) ) ) );
		}
	}
}

