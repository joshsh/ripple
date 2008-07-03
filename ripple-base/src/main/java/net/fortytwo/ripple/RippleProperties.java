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

public class RippleProperties extends Properties
{
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
		String value = getProperty( name, true );
		return value;
	}

    public String getString( final String name, final String defaultValue ) throws RippleException
	{
		String value = getProperty( name, false );
        
        return ( null == value )
                ? defaultValue
                : value;
	}

    public boolean getBoolean( final String name ) throws RippleException
	{
		String value = getProperty( name, true );
		
		return value.equals( "true" );
	}

    public void setBoolean( final String name, final boolean value ) throws RippleException
    {
        setProperty( name, "" + value );
    }

    public double getDouble( final String name ) throws RippleException
	{
		String value = getProperty( name, true );

		try
		{
			return ( new Double( value ) ).doubleValue();
		}

		catch ( java.lang.NumberFormatException e )
		{
			throw new RippleException( e );
		}
	}

    public void setDouble( final String name, final double value ) throws RippleException
    {
        setProperty( name, "" + value );
    }

    public float getFloat( final String name ) throws RippleException
	{
		String value = getProperty( name, true );

		try
		{
			return ( new Float( value ) ).floatValue();
		}

		catch ( java.lang.NumberFormatException e )
		{
			throw new RippleException( e );
		}
	}

    public void setFloat( final String name, final float value ) throws RippleException
    {
        setProperty( name, "" + value );
    }

    public int getInt( final String name ) throws RippleException
	{
		String value = getProperty( name, true );
	
		try
		{
			return ( new Integer( value ) ).intValue();
		}
		
		catch ( java.lang.NumberFormatException e )
		{
			throw new RippleException( e );
		}
	}

    public void setInt( final String name, final int value ) throws RippleException
    {
        setProperty( name, "" + value );
    }

    public long getLong( final String name ) throws RippleException
	{
		String value = getProperty( name, true );
		
		try
		{
			return ( new Long( value ) ).longValue();
		}
		
		catch ( java.lang.NumberFormatException e )
		{
			throw new RippleException( e );
		}
	}

    public void setLong( final String name, final long value ) throws RippleException
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

    public void setURI( final String name, final URI value ) throws RippleException
    {
        setProperty( name, "" + value );
    }

    public File getFile( final String name ) throws RippleException
    {
        String value = getProperty( name, true );
        
        return new File( value );
    }

    public void setFile( final String name, final File value ) throws RippleException
    {
        setProperty( name, "" + value.getAbsolutePath() );
    }

    public File getFile( final String name, final File defaultValue ) throws RippleException
    {
        String value = getProperty( name, false );

        return ( null == value )
                ? defaultValue
                : new File( value );
    }

    public Date getDate( final String name ) throws RippleException
    {
        long millis = getLong( name );
        return new Date( millis );
    }

    public void setDate( final String name, final Date value ) throws RippleException
    {
        setProperty( name, "" + value.getTime() );
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
