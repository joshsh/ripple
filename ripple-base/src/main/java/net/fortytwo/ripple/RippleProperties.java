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
		String value = getProperty( name );
		return value;
	}

	public boolean getBoolean( final String name ) throws RippleException
	{
		String value = getProperty( name );
		
		return value.equals( "true" );
	}
	
	public int getInt( final String name ) throws RippleException
	{
		String value = getProperty( name );
	
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
		String value = getProperty( name );
		
		try
		{
			return ( new Long( value ) ).longValue();
		}
		
		catch ( java.lang.NumberFormatException e )
		{
			throw new RippleException( e );
		}
	}
	
	/*public RDFFormat getRdfFormat( final String name ) throws RippleException
	{
		String value = getProperty( name );
		
		RDFFormat format = RdfUtils.findFormat( value );
		
		if ( null == format )
		{
			throw new RippleException( "unknown RDF format: " + value );
		}
		
		return format;
	}*/

    /*public ExpressionOrder getExpressionOrder( final String name ) throws RippleException
    {
        String value = getProperty( name );

        ExpressionOrder order = ExpressionOrder.find( value );

        if ( null == order )
        {
            throw new RippleException( "unknown expression order: " + value );
        }

        return order;
    }

    public EvaluationOrder getEvaluationOrder( final String name ) throws RippleException
    {
        String value = getProperty( name );

        EvaluationOrder order = EvaluationOrder.find( value );

        if ( null == order )
        {
            throw new RippleException( "unknown evaluation order: " + value );
        }

        return order;
    }

    public EvaluationStyle getEvaluationStyle( final String name ) throws RippleException
    {
        String value = getProperty( name );

        EvaluationStyle style = EvaluationStyle.find( value );

        if ( null == style )
        {
            throw new RippleException( "unknown evaluation style: " + value );
        }

        return style;
    }*/

    public File getFile( final String name ) throws RippleException
    {
        String value = getProperty( name );
        
        return new File( value );
    }

    private String getProperty( final String name ) throws RippleException
	{
		String s = props.getProperty( name );
		
		if ( null == s )
		{
			throw new RippleException( "no value for property: " + name );
		}
		
		return s;
	}
}
