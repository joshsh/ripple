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
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;


/**
 * A collection of primitives for manipulating data types and RDF graphs,
 * excluding those which write to (have side-effects upon) the graph.
 */
public class ReadOnlyGraphLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/graph#";

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "graph.ttl" ) + "#" );

		registerPrimitive( Contains.class, NS + "contains", context );
//		registerPrimitive( Count.class, NS + "count", context );
		registerPrimitive( Compare.class, NS + "compare", context );
		registerPrimitive( Equal.class, NS + "equal", context );

		/* These primitives are excluded
		registerPrimitive( Assert.class, NS + "assert", mc );
		registerPrimitive( Deny.class, NS + "deny", mc );
		registerPrimitive( Forget.class, NS + "forget", mc );
		registerPrimitive( New.class, NS + "new", mc );*/

		// Type conversion / literal reification.
		registerPrimitive( ToDouble.class, NS + "toDouble", context );
		registerPrimitive( ToInteger.class, NS + "toInteger", context );
		registerPrimitive( ToString.class, NS + "toString", context );
		registerPrimitive( ToUri.class, NS + "toUri", context );

		// Resource-centric primitives.
        registerPrimitive( InContext.class, NS + "inContext", context );
		registerPrimitive( Infer.class, NS + "infer", context );
		registerPrimitive( Links.class, NS + "links", context );

		// Document-centric primitives.
		registerPrimitive( Comments.class, NS + "comments", context );
		registerPrimitive( Namespaces.class, NS + "namespaces", context );
		registerPrimitive( Triples.class, NS + "triples", context );
        registerPrimitive( Quads.class, NS + "quads", context );

		// Note: the xml: namespace is actually said to be
		//       http://www.w3.org/XML/1998/namespace
		//       (i.e. without the hash character).
		registerPrimitive( Lang.class, "http://www.w3.org/XML/1998/namespace#lang", context );

		// Note: this URI is bogus.
		registerPrimitive( Type.class, "http://www.w3.org/2001/XMLSchema#type", context );
	}
}
