package net.fortytwo.ripple.libs.logic;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;

/**
 * A collection of primitives for boolean logic and conditionals.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LogicLibrary extends Library
{
    public static final String
            NS_2011_08 = "http://fortytwo.net/2011/08/ripple/logic#",
            NS_2008_08 = "http://fortytwo.net/2008/08/ripple/logic#";

    public void load(final LibraryLoader.Context context)
		throws RippleException
	{
		registerPrimitives( context,
                And.class,
                Not.class,
                Or.class,
                Xor.class );
    }
}
