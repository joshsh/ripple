package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.LibraryLoader;


/**
 * A collection of primitives for manipulating data types and RDF graphs,
 * excluding those which write to (have side-effects upon) the graph.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class ReadOnlyGraphLibrary extends GraphLibrary {
    @Override
    public void load(final LibraryLoader.Context context)
            throws RippleException {
        load(context, false);
    }
}
