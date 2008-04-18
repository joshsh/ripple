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
import net.fortytwo.ripple.URIMap;

/**
 * A collection of string manipulation primitives.
 */
public class StringLibrary extends Library
{
	private static final String NS = "http://fortytwo.net/2007/08/ripple/string#";

	public void load( final URIMap uf, final ModelConnection mc )
		throws RippleException
	{
		uf.put(
			NS, getClass().getResource( "string.ttl" ) + "#" );

		registerPrimitive( EndsWith.class, NS + "endsWith", mc );
		registerPrimitive( IndexOf.class, NS + "indexOf", mc );
		registerPrimitive( LastIndexOf.class, NS + "lastIndexOf", mc );
		registerPrimitive( Length.class, NS + "length", mc );
        registerPrimitive( Matches.class, NS + "matches", mc );
        registerPrimitive( Md5.class, NS + "md5", mc );
		registerPrimitive( PercentDecode.class, NS + "percentDecode", mc );
		registerPrimitive( PercentEncode.class, NS + "percentEncode", mc );
		registerPrimitive( ReplaceAll.class, NS + "replaceAll", mc );
		registerPrimitive( Sha1.class, NS + "sha1", mc );
		registerPrimitive( Split.class, NS + "split", mc );
		registerPrimitive( StartsWith.class, NS + "startsWith", mc );
		registerPrimitive( StrCat.class, NS + "strCat", mc );
		registerPrimitive( Substring.class, NS + "substring", mc );
		registerPrimitive( ToLowerCase.class, NS + "toLowerCase", mc );
		registerPrimitive( ToUpperCase.class, NS + "toUpperCase", mc );
		registerPrimitive( Trim.class, NS + "trim", mc );
		registerPrimitive( UrlDecode.class, NS + "urlDecode", mc );
		registerPrimitive( UrlEncode.class, NS + "urlEncode", mc );
	}
}

// kate: tab-width 4
