/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.data.Compare;
import net.fortytwo.ripple.libs.data.Equal;
import net.fortytwo.ripple.libs.data.Lang;
import net.fortytwo.ripple.libs.data.ToDouble;
import net.fortytwo.ripple.libs.data.ToInteger;
import net.fortytwo.ripple.libs.data.ToMillis;
import net.fortytwo.ripple.libs.data.ToString;
import net.fortytwo.ripple.libs.data.ToUri;
import net.fortytwo.ripple.libs.data.Type;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * A collection of primitives for manipulating data types and RDF graphs.
 */
public class GraphLibrary extends Library
{
    public static final String
            NS_2011_04 = "http://fortytwo.net/2011/04/ripple/graph#",
            NS_2010_08 = "http://fortytwo.net/2008/08/ripple/graph#",
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/graph#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/graph#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/graph#";

    public void load(final LibraryLoader.Context context)
        throws RippleException
    {
        load( context, true );
    }

    protected void load( final LibraryLoader.Context context,
                      final boolean includePrimitivesWithSideEffects )
		throws RippleException
	{
		//uf.put( NS_2008_08, getClass().getResource( "graph.ttl" ) + "#" );

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

                // Resource-centric primitives
                InContext.class,
                Inlinks.class,
                Links.class,
                Members.class,

                // Document-centric primitives
                Triples.class,
                Quads.class,

                // Tuple queries
                Sparql.class,

                // JSON
                ToJson.class );
	}
}

