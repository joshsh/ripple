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
import net.fortytwo.ripple.model.LibraryLoader;


/**
 * A collection of primitives for manipulating data types and RDF graphs,
 * excluding those which write to (have side-effects upon) the graph.
 */
public class ReadOnlyGraphLibrary extends GraphLibrary
{
    @Override
    public void load( final URIMap uf,
                      final LibraryLoader.Context context )
        throws RippleException
    {
        load( uf, context, false );
    }
}
