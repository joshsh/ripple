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

/**
 * RDF data and Java implementation of a library of primitive functions.
 */
public abstract class Library
{
	public abstract void load( URIMap uf, LibraryLoader.Context context )
		throws RippleException;

	protected PrimitiveStackMapping registerPrimitive( final Class c,
                                                       final LibraryLoader.Context context )
		throws RippleException
	{
        final ModelConnection mc = context.getModelConnection();
        PrimitiveStackMapping prim;

		try
		{
			prim = (PrimitiveStackMapping) c.newInstance();
            prim.setRdfEquivalent( new RDFValue( mc.createURI( prim.getIdentifiers()[0] ) ), mc );
        }

		catch ( InstantiationException e )
		{
			throw new RippleException( e );
		}

		catch ( IllegalAccessException e )
		{
			throw new RippleException( e );
		}

        // Add the primitive's stated URI to the map.
        context.addPrimaryValue( prim.toRDF( mc ).sesameValue(), prim );

        // Add all stated aliases (but no aliases of aliases) to the map.
        String[] identifiers = prim.getIdentifiers();
        for ( int i = 1; i < identifiers.length; i++ )
        {
            context.addAlias( mc.createURI( identifiers[i] ), prim );
        }

        return prim;
	}
}
