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
import java.security.NoSuchAlgorithmException;

public final class StringUtils
{
    private static final String UTF_8 = "UTF-8";

    private static final int
		FOUR = 4,
		EIGHT = 8,
		SIXTEEN = 16;

    private static final MessageDigest SHA1_DIGEST;
    private static final MessageDigest MD5_DIGEST;

    static
    {
        try
        {
            SHA1_DIGEST = MessageDigest.getInstance( "SHA" );
            MD5_DIGEST = MessageDigest.getInstance( "MD5" );
        }

        catch ( NoSuchAlgorithmException e )
        {
            throw new ExceptionInInitializerError( e );
        }
    }

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
	 *  @param s  a string to encode.  Uses the UTF-8 character set.
	 *  @return  percent-encoded (per RFC 3986) version of the string
     * @throws RippleException  if encoding fails
	 */
    public static String percentEncode( final String s ) throws RippleException {
        // TODO: there are said to be other differences between the historical
        // application/x-www-form-urlencoded encoding and the modern URL encoding,
        // apart from the treatment of space characters.
        return urlEncode( s ).replaceAll( "\\+", "%20" );
    }

	/**
	 *  @param s  percent-encoded (per RFC 3986) string to decode.  Uses the UTF-8 character set.
	 *  @return   the decoded string
     * @throws RippleException  if decoding fails
	 */
    public static String percentDecode( final String s ) throws RippleException {
        // TODO: is there any properly percent-encoded string which this would treat incorrectly?
        return urlDecode( s );
//        return urlDecode( s.replaceAll( "\\+", "%2B" ) );
    }

	/**
	 *  @param s  a string to encode.  Uses the UTF-8 character set.
	 *  @return  application/x-www-form-urlencoded version of the string
     * @throws RippleException  if encoding fails
	 */
 	public static String urlEncode( final String s )
		throws RippleException
	{
		try
		{
			return URLEncoder.encode( s, UTF_8 );
		}

		catch ( java.io.UnsupportedEncodingException e )
		{
			throw new RippleException( e );
		}
	}
    
    /**
	 *  @param s  an application/x-www-form-urlencoded string to decode.  Uses the UTF-8 character set.
	 *  @return  the decoded string
     * @throws RippleException  if decoding fails
	 */
 	public static String urlDecode( final String s )
		throws RippleException
	{
		try
		{
			return URLDecoder.decode( s, UTF_8 );
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
			synchronized (SHA1_DIGEST)
			{
				SHA1_DIGEST.update( plaintext.getBytes( UTF_8 ) );
			}
		}

		catch ( java.io.UnsupportedEncodingException e )
		{
			throw new RippleException( e );
		}

		byte[] digest = SHA1_DIGEST.digest();

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
			synchronized (MD5_DIGEST)
			{
				MD5_DIGEST.update( plaintext.getBytes( UTF_8 ) );
			}
		}

		catch ( java.io.UnsupportedEncodingException e )
		{
			throw new RippleException( e );
		}

		byte[] digest = MD5_DIGEST.digest();

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
			return (char) Integer.parseInt( unicode, SIXTEEN );
		}

		catch ( NumberFormatException e )
		{
			throw new RippleException( e );
		}
	}
}
