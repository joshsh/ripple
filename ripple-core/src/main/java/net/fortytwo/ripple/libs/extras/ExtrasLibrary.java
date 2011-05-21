/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.extras;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.stream.ranking.Amp;
import net.fortytwo.ripple.libs.stream.ranking.Rank;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * A collection of miscellaneous primitives.
 */
public class ExtrasLibrary extends Library {
    public static final String
            NS_2011_04 = "http://fortytwo.net/2011/04/ripple/extras#",
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/etc#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/etc#",
            NS_2007_05 = "http://fortytwo.net/2007/05/ripple/etc#";

    private static Script scriptVal;

    public void load(final LibraryLoader.Context context) throws RippleException {

        registerPrimitives(context,
                Get.class,

                // Disabled by default.
                //System.class,

                Random.class,
                Time.class);
        scriptVal = (Script) registerPrimitive(Script.class, context);
    }

    public static void registerScriptEngine(final String name,
                                            final ScriptEngineWrapper wrapper) {
        scriptVal.addScriptEngine(name, wrapper);
    }
}

