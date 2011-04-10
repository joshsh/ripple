/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.extras;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.extras.ranking.Amp;
import net.fortytwo.ripple.libs.extras.ranking.Rank;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.URIMap;

/**
 * A collection of miscellaneous primitives.
 */
public class ExtrasLibrary extends Library {
    public static final String
            NS_2011_04 = "http://fortytwo.net/2011/04/ripple/extras#",
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/etc#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/etc#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/etc#";

    private static PrimitiveStackMapping invertVal;

    public void load(final URIMap uf,
                     final LibraryLoader.Context context)
            throws RippleException {
        uf.put(NS_2008_08, getClass().getResource("etc.ttl") + "#");

        registerPrimitives(context,
                Get.class,
                System.class,
                Time.class,
                // TODO: move these?
                Keys.class,
                Rank.class,
                Amp.class);
        invertVal = registerPrimitive(Inverse.class, context);
    }

    public static PrimitiveStackMapping getInvertValue() {
        return invertVal;
    }
}

