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
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.flow.Sink;

import org.openrdf.model.Value;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;

/**
 * A primitive which consumes a literal value and produces its data type (if
 * any).
 */
public class Type extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_XSD + "type"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Type()
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
        return "l  =>  data type of literal l";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		Value v;

		v = stack.getFirst().toRDF( mc ).sesameValue();
		stack = stack.getRest();

		if ( v instanceof Literal )
		{
			URI type = ( (Literal) v ).getDatatype();

			if ( null != type )
			{
				solutions.put( arg.with(
						stack.push( new RDFValue( type ) ) ) );
			}
		}
	}
}

