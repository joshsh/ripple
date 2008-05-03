/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.string;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Library;
import net.fortytwo.ripple.model.LibraryLoader;
import net.fortytwo.ripple.URIMap;

/**
 * A collection of string manipulation primitives.
 */
public class StringLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/string#";

    public void load( final URIMap uf,
                      final LibraryLoader.LibraryLoaderContext context )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "string.ttl" ) + "#" );

		registerPrimitive( EndsWith.class, NS + "endsWith", context );
		registerPrimitive( IndexOf.class, NS + "indexOf", context );
		registerPrimitive( LastIndexOf.class, NS + "lastIndexOf", context );
		registerPrimitive( Length.class, NS + "length", context );
        registerPrimitive( Matches.class, NS + "matches", context );
        registerPrimitive( Md5.class, NS + "md5", context );
		registerPrimitive( PercentDecode.class, NS + "percentDecode", context );
		registerPrimitive( PercentEncode.class, NS + "percentEncode", context );
		registerPrimitive( ReplaceAll.class, NS + "replaceAll", context );
		registerPrimitive( Sha1.class, NS + "sha1", context );
		registerPrimitive( Split.class, NS + "split", context );
		registerPrimitive( StartsWith.class, NS + "startsWith", context );
		registerPrimitive( StrCat.class, NS + "strCat", context );
		registerPrimitive( Substring.class, NS + "substring", context );
		registerPrimitive( ToLowerCase.class, NS + "toLowerCase", context );
		registerPrimitive( ToUpperCase.class, NS + "toUpperCase", context );
		registerPrimitive( Trim.class, NS + "trim", context );
		registerPrimitive( UrlDecode.class, NS + "urlDecode", context );
		registerPrimitive( UrlEncode.class, NS + "urlEncode", context );
	}
}

// kate: tab-width 4
