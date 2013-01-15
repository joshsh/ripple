package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * A collection of primitives for manipulating data types and RDF graphs.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class GraphLibrary extends Library {
    public static final String
            NS_2011_08 = "http://fortytwo.net/2011/08/ripple/graph#",
            NS_2010_08 = "http://fortytwo.net/2008/08/ripple/graph#",
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/graph#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/graph#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/graph#";

    public void load(final LibraryLoader.Context context)
            throws RippleException {
        load(context, true);
    }

    protected void load(final LibraryLoader.Context context,
                        final boolean includePrimitivesWithSideEffects)
            throws RippleException {
        //uf.put( NS_2008_08, getClass().getResource( "graph.ttl" ) + "#" );

        if (includePrimitivesWithSideEffects) {
            registerPrimitives(context,

                    // RDF primitives with side effects
                    Assert.class,
                    AssertInContext.class,
                    Deny.class,
                    DenyInContext.class,
                    New.class);
        }

        registerPrimitives(context,

                // Resource-centric primitives
                InContext.class,
                Inlinks.class,
                Links.class,
                Members.class,

                // Tuple queries
                Sparql.class,

                // Key/values
                Keys.class,
                KeyValues.class,
                Values.class,

                // JSON
                ToJson.class);
    }
}

