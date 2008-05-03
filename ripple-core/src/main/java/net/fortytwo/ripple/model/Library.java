/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RdfValue;

import org.openrdf.model.vocabulary.OWL;

/**
 * RDF data and Java implementation of a library of primitive functions.
 */
public abstract class Library
{
	private static final RdfValue OWL_SAMEAS = new RdfValue( OWL.SAMEAS );

	public abstract void load(URIMap uf, LibraryLoader.LibraryLoaderContext context)
		throws RippleException;

	protected PrimitiveStackMapping registerPrimitive( final Class c,
                                                       final String uri,
                                                       final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
        final ModelConnection mc = context.getModelConnection();
        PrimitiveStackMapping prim;

		try
		{
			prim = (PrimitiveStackMapping) c.newInstance();
			prim.setRdfEquivalent( new RdfValue( mc.createUri( uri ) ), mc );
		}

		catch ( InstantiationException e )
		{
			throw new RippleException( e );
		}

		catch ( IllegalAccessException e )
		{
			throw new RippleException( e );
		}

		final PrimitiveStackMapping primFinal = prim;

		Sink<RippleValue, RippleException> aliasSink = new Sink<RippleValue, RippleException>()
		{
			public void put( final RippleValue v )
				throws RippleException
			{
				context.addAlias( v.toRDF( mc ).sesameValue(), primFinal );
			}
		};

        // Add the primitive's stated URI to the map.
        context.addPrimaryValue( prim.toRDF( mc ).sesameValue(), prim );
        
        // Add all stated aliases (but no aliases of aliases) to the map.
        StatementPatternQuery query = new StatementPatternQuery( prim, OWL_SAMEAS, null, false );
        mc.query( query, aliasSink );

        return prim;
	}
}
