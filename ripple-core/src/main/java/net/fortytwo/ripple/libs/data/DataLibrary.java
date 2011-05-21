/*
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.data;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * A collection of primitives for value comparison and datatype conversion.
 */
public class DataLibrary extends Library {
    public static final String
            NS_2011_04 = "http://fortytwo.net/2011/04/ripple/data#";
    public static final String
            NS_XML = "http://www.w3.org/XML/1998/namespace#",
            NS_XSD = "http://www.w3.org/2001/XMLSchema#";

    public void load(final LibraryLoader.Context context)
            throws RippleException {

        registerPrimitives(context,
                // Comparison
                Compare.class,
                Equal.class,

                // Datatype conversion and literal reification
                ToDouble.class,
                ToInteger.class,
                ToMillis.class,
                ToString.class,
                ToUri.class,

                // Note: the xml: namespace is actually said to be
                //       http://www.w3.org/XML/1998/namespace
                //       (i.e. without the hash character).
                Lang.class,

                // Note: this URI is bogus.
                Type.class
        );
    }
}

