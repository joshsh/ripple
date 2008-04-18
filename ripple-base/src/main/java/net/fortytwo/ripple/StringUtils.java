/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple;

import java.net.URLDecoder;
import java.net.URLEncoder;

import java.security.MessageDigest;

public final class StringUtils
{
	// See: http://www.koders.com/java/fid97184BECA1A7DCCD2EDA6D243477157EB453294C.aspx
	private static final String SAFE_URL_CHARACTERS
		= "@*-./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";

	private static final int
		FOUR = 4,
		EIGHT = 8,
		SIXTEEN = 16;

    private static MessageDigest sha1Digest = null;
    private static MessageDigest md5Digest = null;

	private StringUtils()
	{
	}

	// Note: extended characters are not escaped for printing.
	public static String escapeString( final String s )
	{
		StringBuilder sb = new StringBuilder();
		for ( int i = 0; i < s.length(); i++ )
		{
			char c = s.charAt( i );
			switch ( c )
			{
				case '\\':
					sb.append( "\\\\" );
					break;
				case '\"':
					sb.append( "\\\"" );
					break;
				case '\t':
					sb.append( "\\t" );
					break;
				case '\n':
					sb.append( "\\n" );
					break;
				case '\r':
					sb.append( "\\r" );
					break;
				default:
					sb.append( c );
			}
		}

		return sb.toString();
	}

	// Note: extended characters are not escaped for printing.
	public static String escapeUriString( final String s )
	{
		StringBuilder sb = new StringBuilder();
		for ( int i = 0; i < s.length(); i++ )
		{
			char c = s.charAt( i );
			switch ( c )
			{
				case '\\':
					sb.append( "\\\\" );
					break;
				case '>':
					sb.append( "\\>" );
					break;
				default:
					sb.append( c );
			}
		}

		return sb.toString();
	}

	// Note: assumes a properly formatted (escaped) String argument.
	public static String unescapeString( final String s )
		throws RippleException
	{
		StringBuilder sb = new StringBuilder();
		String seq;

		for ( int i = 0; i < s.length(); i++ )
		{
			char c = s.charAt( i );

			if ( '\\' == c )
			{
				i++;

				switch ( s.charAt( i ) )
				{
					case '\\':
						sb.append( '\\' );
						break;
                    // Note: this sequence is not created by escapeString()
                    case '\'':
                        sb.append( '\'' );
                        break;
                    case '\"':
						sb.append( '\"' );
						break;
					case 't':
						sb.append( '\t' );
						break;
					case 'n':
						sb.append( '\n' );
						break;
					case 'r':
						sb.append( '\r' );
						break;
					case 'u':
						seq = s.substring( i + 1, i + FOUR + 1 );
						sb.append( toUnicodeChar( seq ) );
						i += FOUR;
						break;
					case 'U':
						seq = s.substring( i + 1, i + EIGHT + 1 );
						sb.append( toUnicodeChar( seq ) );
						i += EIGHT;
						break;
					default:
						throw new RippleException( "bad escape sequence: \\" + s.charAt( i ) + " at character " +  ( i - 1 ) );
				}
			}

			else
			{
				sb.append( c );
			}
		}

		return sb.toString();
	}

	// Note: assumes a properly formatted (escaped) String argument.
	public static String unescapeUriString( final String s )
		throws RippleException
	{
		StringBuilder sb = new StringBuilder();
		String seq;

		for ( int i = 0; i < s.length(); i++ )
		{
			char c = s.charAt( i );

			if ( '\\' == c )
			{
				i++;

				switch ( s.charAt( i ) )
				{
					case '\\':
						sb.append( '\\' );
						break;
					case '>':
						sb.append( '>' );
						break;
					case 'u':
						seq = s.substring( i + 1, i + FOUR + 1 );
						sb.append( toUnicodeChar( seq ) );
						i += FOUR;
						break;
					case 'U':
						seq = s.substring( i + 1, i + EIGHT + 1 );
						sb.append( toUnicodeChar( seq ) );
						i += EIGHT;
						break;
					default:
						throw new RippleException( "bad escape sequence: \\" + s.charAt( i + 1 ) );
				}
			}

			else
			{
				sb.append( c );
			}
		}

		return sb.toString();
	}

	/**
	 *  @param s  a string to encode
	 *  @return  percent-encoded (per RFC 3986) version of the string
	 */
	public static String percentEncode( final String s )
	{
		StringBuffer enc = new StringBuffer( s.length() );
		for ( int i = 0; i < s.length(); ++i )
		{
			char c = s.charAt( i );

			if ( SAFE_URL_CHARACTERS.indexOf( c ) >= 0 )
			{
				enc.append( c );
			}

			else
			{
				// Just keep lsb like:
				// http://java.sun.com/j2se/1.3/docs/api/java/net/URLEncoder.html
				c = (char) ( c & '\u00ff' );
				if ( c < SIXTEEN )
				{
					enc.append( "%0" );
				}
				else
				{
					enc.append( "%" );
				}

				enc.append( Integer.toHexString( c ) );
			}
		}
		return enc.toString();
	}

	/**
	 *  @param s  percent-encoded (per RFC 3986) string to decode
	 *  @return   the decoded string
	 */
// TODO: this is a quick hack which may be too simple
	public static String percentDecode( final String s )
	{
		StringBuffer dec = new StringBuffer( s.length() );

		int i = 0, len = s.length();
		while ( i < len )
		{
			char c = s.charAt( i );

			// Percent-encoded character.
			if ( '%' == c )
			{
				try
				{
					c = (char) Integer.parseInt( s.substring( i + 1, i + 3 ), 16 );
					i += 2;
				}

				catch ( Throwable t )
				{
				}
			}

			i++;
			dec.append( c );
		}

		return dec.toString();
	}

	/**
	 *  @param s  a string to encode
	 *  @return  application/x-www-form-urlencoded version of the string
	 */
 	public static String urlEncode( final String s )
		throws RippleException
	{
		try
		{
			return URLEncoder.encode( s, "UTF-8" );
		}

		catch ( java.io.UnsupportedEncodingException e )
		{
			throw new RippleException( e );
		}
	}

	/**
	 *  @param s  an application/x-www-form-urlencoded string to decode
	 *  @return  the decoded string
	 */
 	public static String urlDecode( final String s )
		throws RippleException
	{
		try
		{
			return URLDecoder.decode( s, "UTF-8" );
		}

		catch ( java.io.UnsupportedEncodingException e )
		{
			throw new RippleException( e );
		}
	}

	// See: http://intertwingly.net/stories/2003/08/05/sha1demo.java
	public static String sha1SumOf( final String plaintext )
		throws RippleException
	{
		try
		{
			if ( null == sha1Digest )
			{
				sha1Digest = MessageDigest.getInstance( "SHA" );
			}
		}

		catch ( java.security.NoSuchAlgorithmException e )
		{
			throw new RippleException( e );
		}

		try
		{
			synchronized ( sha1Digest )
			{
				sha1Digest.update( plaintext.getBytes( "UTF-8" ) );
			}
		}

		catch ( java.io.UnsupportedEncodingException e )
		{
			throw new RippleException( e );
		}

		byte[] digest = sha1Digest.digest();

		String coded = "";

		for  ( byte b : digest )
		{
			String hex = Integer.toHexString( b );

			if ( hex.length() == 1 )
			{
				hex = "0" + hex;
			}

			hex = hex.substring( hex.length() - 2 );
			coded += hex;
		}

		return coded;
	}

    public static String md5SumOf( final String plaintext )
		throws RippleException
	{
		try
		{
			if ( null == md5Digest )
			{
				md5Digest = MessageDigest.getInstance( "MD5" );
			}
		}

		catch ( java.security.NoSuchAlgorithmException e )
		{
			throw new RippleException( e );
		}

		try
		{
			synchronized ( md5Digest )
			{
				md5Digest.update( plaintext.getBytes( "UTF-8" ) );
			}
		}

		catch ( java.io.UnsupportedEncodingException e )
		{
			throw new RippleException( e );
		}

		byte[] digest = md5Digest.digest();

		String coded = "";

		for  ( byte b : digest )
		{
			String hex = Integer.toHexString( b );

			if ( hex.length() == 1 )
			{
				hex = "0" + hex;
			}

			hex = hex.substring( hex.length() - 2 );
			coded += hex;
		}

		return coded;
	}

    ////////////////////////////////////////////////////////////////////////////

	private static char toUnicodeChar( final String unicode )
		throws RippleException
	{
		try
		{
			return (char) Integer.parseInt( unicode.toString(), SIXTEEN );
		}

		catch ( NumberFormatException e )
		{
			throw new RippleException( e );
		}
	}
}
