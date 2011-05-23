/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.stream;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stream.ranking.Amp;
import net.fortytwo.ripple.libs.stream.ranking.Rank;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * A collection of data flow primitives.
 */
public class StreamLibrary extends Library {
    public static final String
            NS_2011_04 = "http://fortytwo.net/2011/04/ripple/stream#",
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/stream#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/stream#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/stream#";

    public void load(final LibraryLoader.Context context)
            throws RippleException {

        registerPrimitives(context,
                Both.class,
                Each.class,
                Intersect.class,
                Limit.class,
                Scrap.class,
                Distinct.class,

                // Closed world primitives
                Amp.class,
                Count.class,
                Order.class,
                Rank.class);
    }
}

