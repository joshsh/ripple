package net.fortytwo.ripple.libs.media;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class MediaLibrary extends Library {

    public static final String
            NS_2013_03 = "http://fortytwo.net/2013/03/ripple/media#",
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/media#",
            NS_2007_08 = "http://fortytwo.net/2007/08/ripple/media#";

    public void load(final LibraryLoader.Context context) throws RippleException {

        registerPrimitive(Play.class, context);
        registerPrimitive(Show.class, context);
        registerPrimitive(Speak.class, context);
    }
}
