/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
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
            NS_2010_08 = "http://fortytwo.net/2008/08/ripple/graph#",
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/graph#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/graph#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/graph#";
    public static final String
            NS_XML = "http://www.w3.org/XML/1998/namespace#",
            NS_XSD = "http://www.w3.org/2001/XMLSchema#";

    public void load( final URIMap uf,
                      final LibraryLoader.Context context )
        throws RippleException
    {
        load( uf, context, true );
    }

    protected void load( final URIMap uf,
                      final LibraryLoader.Context context,
                      final boolean includePrimitivesWithSideEffects )
		throws RippleException
	{
		uf.put( NS_2008_08, getClass().getResource( "graph.ttl" ) + "#" );

        if ( includePrimitivesWithSideEffects )
        {
            registerPrimitives( context,

                    // RDF primitives with side effects
                    Assert.class,
                    AssertInContext.class,
                    Deny.class,
                    DenyInContext.class,
                    New.class );
        }

        registerPrimitives( context,

                // Comparison primitives
                Compare.class,
                Equal.class,

                // Type conversion and literal reification primitives
                ToDouble.class,
                ToInteger.class,
                ToString.class,
                ToUri.class,

                // Resource-centric primitives
                InContext.class,
                Infer.class,
                InferInContext.class,
                Inlinks.class,
                Links.class,
                Members.class,

                // Document-centric primitives
                Comments.class,
                Namespaces.class,
                Triples.class,
                Quads.class,

                // Tuple queries
                Sparql.class,

                // JSON
                ToJson.class,

                // Note: the xml: namespace is actually said to be
                //       http://www.w3.org/XML/1998/namespace
                //       (i.e. without the hash character).
		        Lang.class,

		        // Note: this URI is bogus.
		        Type.class );
	}
}

