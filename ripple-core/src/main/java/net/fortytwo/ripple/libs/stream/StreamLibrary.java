package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stream.ranking.Amp;
import net.fortytwo.ripple.libs.stream.ranking.Rank;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * A collection of data flow primitives.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class StreamLibrary extends Library {
    public static final String
            NS_2011_08 = "http://fortytwo.net/2011/08/ripple/stream#",
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/stream#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/stream#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/stream#";

    public void load(final LibraryLoader.Context context)
            throws RippleException {

        registerPrimitives(context,
                Both.class,
                Distinct.class,
                Each.class,
                Intersect.class,
                Limit.class,
                Scrap.class,

                // Closed world primitives
                Amp.class,
                Count.class,
                Order.class,
                Rank.class);
    }
}

