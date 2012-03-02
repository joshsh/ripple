/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.io;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.Lexicon;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.StringUtils;

import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.XMLSchema;

import java.io.OutputStream;
import java.io.PrintStream;

import java.util.Iterator;
import java.math.BigDecimal;

public class RipplePrintStream extends PrintStream
{
	private final Lexicon lexicon;

	public RipplePrintStream( final OutputStream out, final Lexicon lexicon )
		throws RippleException
	{
		super( out );
		this.lexicon = lexicon;
	}

	public void print( final RippleValue v ) throws RippleException
	{
		if ( null == v )
		{
			throw new NullPointerException();
		}

    	v.printTo( this );
	}

	public void print( final Value v ) throws RippleException
	{
		if ( null == v )
		{
			throw new NullPointerException();
		}

        if ( v instanceof URI )
        {
            printURI( (URI) v );
        }

        else if ( v instanceof Literal )
        {
            printLiteral( (Literal) v );
        }

        else if ( v instanceof BNode )
        {
            print( "_:" );
            print( ( (BNode) v ).getID() );
        }

        else
        {
            print( v.toString() );
        }
	}

	public void print( final Statement st ) throws RippleException
	{
		print( "    " );
		print( st.getSubject() );

		print( " " );
		print( st.getPredicate() );

		print( " " );
		print( st.getObject() );
	}

	public void print( final Iterator<Statement> stmtIter ) throws RippleException
	{
		while ( stmtIter.hasNext() )
		{
			print( stmtIter.next() );
			print( "\n" );
		}
	}

	////////////////////////////////////////////////////////////////////////////

	private void printURIRef( final URI uri )
	{
		print( "<" + StringUtils.escapeURIString( uri.toString() ) + ">" );
	}

	private void printURI( final URI uri ) throws RippleException
	{
		String symbol = lexicon.findSymbol(uri);

		if ( null == symbol )
		{
			printURIRef( uri );
		}

		else
		{
			print( symbol );
		}
	}

    // TODO: handle literals with special types but whose labels are badly formatted.
    private void printLiteral( final Literal l ) throws RippleException
    {
        URI datatype = l.getDatatype();
        String label = l.getLabel();

        if ( null != datatype )
        {
            // Note: URI's equals() returns "true if the other object is an
            //       instance of URI  and their String-representations are
            //       equal, false otherwise"
            if ( datatype.equals( XMLSchema.BOOLEAN ) )
            {
                printBoolean( l.booleanValue() );
            }

            else if ( datatype.equals( XMLSchema.DECIMAL ) )
            {
                if ( label.startsWith( "+ ") )
                {
                    label = label.substring( 1 );
                }

                printDecimal( new BigDecimal( label ) );
            }

            else if ( datatype.equals( XMLSchema.DOUBLE ) )
            {
                printDouble( l.doubleValue() );
            }

            else if ( datatype.equals( XMLSchema.INTEGER ) )
            {
                // TODO: use l.integerValue, BigInteger
                printInteger( l.intValue() );
            }

            else
            {
                printTypedLiteral( label, datatype );
            }
        }

        else
        {
            // Plain literals are printed as escaped strings with no data type.
            printEscapedString( label );
        }

        String language = l.getLanguage();
        if ( null != language )
        {
            print( "@" + language );
        }
    }

    public void printTypedLiteral( final String label, final URI datatype ) throws RippleException
    {
        printEscapedString( label );
        print( "^^" );
        printURI( datatype );
    }

    public void printBoolean( final boolean v )
    {
        print( v ? "true" : "false" );
    }

    public void printInteger( final int v )
    {
        // This will naturally be the canonical form.
        print( "" + v );
    }

    public void printDouble( final double v ) throws RippleException
    {
        if ( Double.NaN == v )
        {
            printTypedLiteral( "NaN", XMLSchema.DOUBLE );
        }

        else if ( Double.NEGATIVE_INFINITY == v )
        {
            printTypedLiteral( "-INF", XMLSchema.DOUBLE );
        }

        else if ( Double.POSITIVE_INFINITY == v )
        {
            printTypedLiteral( "INF", XMLSchema.DOUBLE );
        }

        else
        {
            String s = "" + v;

            // Add an exponent, if necessary, to make this an unambiguous
            // xsd:double value in Ripple syntax.
            if ( !s.contains( "E" ) )
            {
                s += "E0";
            }

            // Note: this is not necessarily the canonical form.
            print( s );
        }
    }

    public void printDecimal( final BigDecimal v )
    {
		String s = v.toString();

        // Add a decimal point, if necessary, to make this an unambiguous
        // xsd:decimal value in Ripple syntax.
        if ( !s.contains( "." ) )
        {
            s += ".0";
        }

        // Note: this is not necessarily the canonical form.
        print( s );
    }

    private void printEscapedString( final String s )
	{
		print( '\"' );
		print( StringUtils.escapeString( s ) );
		print( '\"' );
	}
}

