package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;

import java.net.URI;

/**
 * RDF data and Java implementation of a library of primitive functions.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public abstract class Library
{
	public abstract void load(LibraryLoader.Context context)
		throws RippleException;

    protected void registerPrimitives( final LibraryLoader.Context context,
                                       final Class ... classes )
        throws RippleException
    {
        for ( Class c : classes )
        {
            registerPrimitive( c, context );    
        }
    }

    protected PrimitiveStackMapping registerPrimitive( final Class c,
                                                       final LibraryLoader.Context context )
		throws RippleException
	{
        final ModelConnection mc = context.getModelConnection();
        PrimitiveStackMapping prim;

		try
		{
			prim = (PrimitiveStackMapping) c.newInstance();
            prim.setRdfEquivalent( mc.valueOf(URI.create(prim.getIdentifiers()[0])), mc );
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
            context.addAlias( mc.valueOf(URI.create(identifiers[i])).sesameValue(), prim );
        }

        return prim;
	}
}
