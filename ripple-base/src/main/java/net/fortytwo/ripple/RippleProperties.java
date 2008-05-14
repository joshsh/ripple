/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple;

import java.util.Properties;
import java.io.File;

//import net.fortytwo.ripple.rdf.RdfUtils;
/*import net.fortytwo.ripple.model.enums.ExpressionOrder;
import net.fortytwo.ripple.model.enums.EvaluationStyle;
import net.fortytwo.ripple.model.enums.EvaluationOrder;*/

//import org.openrdf.rio.RDFFormat;

public class RippleProperties
{
	private Properties props;
	
	public RippleProperties( final Properties props )
	{
		this.props = props;
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

    private String getProperty( final String name, final boolean required ) throws RippleException
	{
		String s = props.getProperty( name );
		
		if ( null == s && required )
		{
			throw new RippleException( "no value for property: " + name );
		}
		
		return s;
	}
}
