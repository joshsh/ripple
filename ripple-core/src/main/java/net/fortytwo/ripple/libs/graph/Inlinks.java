/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.StackContext;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;

/**
 * A primitive which consumes a resource and produces a three-element list
 * (subject, resource, object) for each statement about the resource.
 */
public class Inlinks extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "inlinks"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Inlinks()
		throws RippleException
	{
		super();
	}

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "o", null, true )};
    }

    public String getComment()
    {
        return "o  =>  s p o g";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		final RippleList stack = arg.getStack();

		final RippleValue obj = stack.getFirst();
		final RippleList rest = stack.getRest();

		Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>()
		{
			public void put( final Statement st ) throws RippleException
			{
                Resource context = st.getContext();

                RippleValue subj = mc.canonicalValue( st.getSubject() );
				RippleValue pred = mc.canonicalValue( st.getPredicate() );
                RippleValue ctx = ( null == context ) ? mc.list() : mc.canonicalValue( context );

                solutions.put( arg.with( rest.push( subj ).push( pred ).push( obj ).push( ctx ) ) );
            }
		};

        // FIXME: only SesameModel supports getStatements()
        mc.getStatements( null, null, obj.toRDF( mc ), stSink, Ripple.useInference() );
	}
}
