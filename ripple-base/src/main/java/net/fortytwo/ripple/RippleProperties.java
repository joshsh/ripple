/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple;

import java.util.Properties;
import java.util.Date;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class RippleProperties extends Properties
{
    private final DateFormat dateFormat = new SimpleDateFormat();

    public RippleProperties( final Properties defaults )
    {
        super( defaults );
    }

    public RippleProperties()
    {
        super();
    }
    
    public String getString( final String name ) throws RippleException
	{
		return getProperty( name, true );
	}

    public String getString( final String name, final String defaultValue ) throws RippleException
	{
		String value = getProperty( name, false );
        
        return ( null == value )
                ? defaultValue
                : value;
	}

    public void setString( final String name, final String value )
    {
        if ( null == value )
        {
            remove( name );
        }

        else
        {
            setProperty( name, value );
        }
    }

    public boolean getBoolean( final String name ) throws RippleException
	{
		String value = getProperty( name, true );
		
		return value.equals( "true" );
	}

    public void setBoolean( final String name, final boolean value )
    {
        setProperty( name, "" + value );
    }

    public double getDouble( final String name ) throws RippleException
	{
		String value = getProperty( name, true );

		try
		{
			return new Double( value );
		}

		catch ( java.lang.NumberFormatException e )
		{
			throw new RippleException( e );
		}
	}

    public void setDouble( final String name, final double value )
    {
        setProperty( name, "" + value );
    }

    public float getFloat( final String name ) throws RippleException
	{
		String value = getProperty( name, true );

		try
		{
			return new Float( value );
		}

		catch ( java.lang.NumberFormatException e )
		{
			throw new RippleException( e );
		}
	}

    public void setFloat( final String name, final float value )
    {
        setProperty( name, "" + value );
    }

    public int getInt( final String name ) throws RippleException
	{
		String value = getProperty( name, true );
	
		try
		{
			return new Integer( value );
		}
		
		catch ( java.lang.NumberFormatException e )
		{
			throw new RippleException( e );
		}
	}

    public void setInt( final String name, final int value )
    {
        setProperty( name, "" + value );
    }

    public long getLong( final String name ) throws RippleException
	{
		String value = getProperty( name, true );
		
		try
		{
			return new Long( value );
		}
		
		catch ( java.lang.NumberFormatException e )
		{
			throw new RippleException( e );
		}
	}

    public void setLong( final String name, final long value )
    {
        setProperty( name, "" + value );
    }

    public URI getURI( final String name ) throws RippleException
    {
        String value = getProperty( name, true );

        try
        {
            return new URI( value );
        }

        catch ( URISyntaxException e )
        {
            throw new RippleException( e );
        }
    }

    public void setURL( final String name, final URL value )
    {
        if ( null == value )
        {
            remove( name );
        }

        else
        {
            setProperty( name, "" + value );
        }
    }

    public URL getURL( final String name ) throws RippleException
    {
        String value = getProperty( name, true );

        try
        {
            return new URL( value );
        }

        catch ( MalformedURLException e )
        {
            throw new RippleException( e );
        }
    }

    public void setURI( final String name, final URI value )
    {
        if ( null == value )
        {
            remove( name );
        }

        else
        {
            setProperty( name, "" + value );
        }
    }

    public File getFile( final String name ) throws RippleException
    {
        String value = getProperty( name, true );
        
        return new File( value );
    }

    public File getFile( final String name, final File defaultValue ) throws RippleException
    {
        String value = getProperty( name, false );

        return ( null == value )
                ? defaultValue
                : new File( value );
    }

    public void setFile( final String name, final File value )
    {
        if ( null == value )
        {
            remove( name );
        }

        else
        {
            setProperty( name, "" + value.getAbsolutePath() );
        }
    }

    public Date getDate( final String name ) throws RippleException
    {
        String value = getProperty( name, true );

        try
        {
            return dateFormat.parse(value);
        }

        catch ( ParseException e )
        {
            throw new RippleException( e );
        }
    }

    public Date getDate( final String name, final Date defaultValue ) throws RippleException
    {
        String value = getProperty( name, false );

        try
        {
            return null == value
                    ? defaultValue
                    : dateFormat.parse(value);
        }

        catch ( ParseException e )
        {
            throw new RippleException( e );
        }
    }

    public void setDate( final String name, final Date value )
    {
        if ( null == value )
        {
            remove( name );
        }

        else
        {
            setProperty( name, "" + dateFormat.format( value ) );
        }
    }

    private String getProperty( final String name, final boolean required ) throws RippleException
	{
		String s = getProperty( name );
		
		if ( null == s && required )
		{
			throw new RippleException( "no value for property: " + name );
		}
		
		return s;
	}
}
