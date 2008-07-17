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
 * A collection of primitives for manipulating data types and RDF graphs.
 */
public class GraphLibrary extends Library
{
    public static final String
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/graph#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/graph#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/graph#";
    public static final String
            NS_XML = "http://www.w3.org/XML/1998/namespace#",
            NS_XSD = "http://www.w3.org/2001/XMLSchema#";

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put( NS_2008_08, getClass().getResource( "graph.ttl" ) + "#" );

        // RDF primitives with side-effects.
        registerPrimitive( Assert.class, context );
        registerPrimitive( AssertInContext.class, context );
        registerPrimitive( Deny.class, context );
        registerPrimitive( DenyInContext.class, context );
		registerPrimitive( Forget.class, context );
		registerPrimitive( New.class, context );

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

