/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.media;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.URIMap;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;

public class MediaLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/media#";

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "media.ttl" ) + "#" );

		registerPrimitive( Play.class, NS + "play", context );
		registerPrimitive( Show.class, NS + "show", context );
		registerPrimitive( Speak.class, NS + "speak", context );
	}
}
