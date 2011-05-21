/*
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.control;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * A collection of primitives for controlling the flow of Ripple programs.
 */
public class ControlLibrary extends Library {
    public static final String
            NS_2011_04 = "http://fortytwo.net/2011/04/ripple/control#";

    public void load(final LibraryLoader.Context context)
            throws RippleException {

        registerPrimitives(context
        );
    }
}

