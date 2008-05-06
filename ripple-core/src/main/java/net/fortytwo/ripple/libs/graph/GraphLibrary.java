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
 * A collection of primitives for manipulating data types and RDF graphs.
 */
public class GraphLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/graph#";

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "graph.ttl" ) + "#" );

        registerPrimitive( Assert.class, NS + "assert", context );
        registerPrimitive( AssertIn.class, NS + "assertIn", context );
		registerPrimitive( Contains.class, NS + "contains", context );
//		registerPrimitive( Count.class, NS + "count", context );
		registerPrimitive( Compare.class, NS + "compare", context );
        registerPrimitive( Deny.class, NS + "deny", context );
        registerPrimitive( DenyIn.class, NS + "denyIn", context );
		registerPrimitive( Equal.class, NS + "equal", context );
		registerPrimitive( Forget.class, NS + "forget", context );
		registerPrimitive( New.class, NS + "new", context );

		// Type conversion / literal reification.
		registerPrimitive( ToDouble.class, NS + "toDouble", context );
		registerPrimitive( ToInteger.class, NS + "toInteger", context );
		registerPrimitive( ToString.class, NS + "toString", context );
		registerPrimitive( ToUri.class, NS + "toUri", context );

		// Resource-centric primitives.
        registerPrimitive( InContext.class, NS + "inContext", context );
        registerPrimitive( Infer.class, NS + "infer", context );
        registerPrimitive( Inlinks.class, NS + "inlinks", context );
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

