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

/**
 * A collection of primitives for manipulating data types and RDF graphs.
 */
public class GraphLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/graph#";

	public void load( final URIMap uf, final ModelConnection mc )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "graph.ttl" ) + "#" );

        registerPrimitive( Assert.class, NS + "assert", mc );
        registerPrimitive( AssertIn.class, NS + "assertIn", mc );
		registerPrimitive( Contains.class, NS + "contains", mc );
//		registerPrimitive( Count.class, NS + "count", mc );
		registerPrimitive( Compare.class, NS + "compare", mc );
        registerPrimitive( Deny.class, NS + "deny", mc );
        registerPrimitive( DenyIn.class, NS + "denyIn", mc );
		registerPrimitive( Equal.class, NS + "equal", mc );
		registerPrimitive( Forget.class, NS + "forget", mc );
		registerPrimitive( New.class, NS + "new", mc );

		// Type conversion / literal reification.
		registerPrimitive( ToDouble.class, NS + "toDouble", mc );
		registerPrimitive( ToInteger.class, NS + "toInteger", mc );
		registerPrimitive( ToString.class, NS + "toString", mc );
		registerPrimitive( ToUri.class, NS + "toUri", mc );

		// Resource-centric primitives.
        registerPrimitive( InContext.class, NS + "inContext", mc );
        registerPrimitive( Infer.class, NS + "infer", mc );
        registerPrimitive( Inlinks.class, NS + "inlinks", mc );
        registerPrimitive( Links.class, NS + "links", mc );

		// Document-centric primitives.
		registerPrimitive( Comments.class, NS + "comments", mc );
		registerPrimitive( Namespaces.class, NS + "namespaces", mc );
        registerPrimitive( Triples.class, NS + "triples", mc );
        registerPrimitive( Quads.class, NS + "quads", mc );

		// Note: the xml: namespace is actually said to be
		//       http://www.w3.org/XML/1998/namespace
		//       (i.e. without the hash character).
		registerPrimitive( Lang.class, "http://www.w3.org/XML/1998/namespace#lang", mc );

		// Note: this URI is bogus.
		registerPrimitive( Type.class, "http://www.w3.org/2001/XMLSchema#type", mc );
	}
}

// kate: tab-width 4
