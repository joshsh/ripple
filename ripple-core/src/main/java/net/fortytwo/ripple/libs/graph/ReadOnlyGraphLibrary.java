/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;


/**
 * A collection of primitives for manipulating data types and RDF graphs,
 * excluding those which write to (have side-effects upon) the graph.
 */
public class ReadOnlyGraphLibrary extends Library
{
    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
        // Note: contains descriptions of excluded primitives.
        uf.put( GraphLibrary.NS_2008_08, getClass().getResource( "graph.ttl" ) + "#" );

		/* Excluded
		// RDF primitives with side-effects.
        registerPrimitive( Assert.class, context );
        registerPrimitive( AssertInContext.class, context );
        registerPrimitive( Deny.class, context );
        registerPrimitive( DenyInContext.class, context );
		registerPrimitive( Forget.class, context );
		registerPrimitive( New.class, context );
		 */

        // Comparison primitives.
        registerPrimitive( Compare.class, context );
        registerPrimitive( Equal.class, context );

        // Type conversion / literal reification.
		registerPrimitive( ToDouble.class, context );
		registerPrimitive( ToInteger.class, context );
		registerPrimitive( ToString.class, context );
		registerPrimitive( ToUri.class, context );

		// Resource-centric primitives.
        registerPrimitive( InContext.class, context );
        registerPrimitive( Infer.class, context );
        registerPrimitive( InferInContext.class, context );
        registerPrimitive( Inlinks.class, context );
        registerPrimitive( Links.class, context );
        registerPrimitive( Members.class, context );

		// Document-centric primitives.
		registerPrimitive( Comments.class, context );
		registerPrimitive( Namespaces.class, context );
        registerPrimitive( Triples.class, context );
        registerPrimitive( Quads.class, context );

		// Note: the xml: namespace is actually said to be
		//       http://www.w3.org/XML/1998/namespace
		//       (i.e. without the hash character).
		registerPrimitive( Lang.class, context );

		// Note: this URI is bogus.
		registerPrimitive( Type.class, context );
	}
}
