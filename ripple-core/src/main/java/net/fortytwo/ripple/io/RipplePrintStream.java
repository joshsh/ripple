/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
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

public class RipplePrintStream extends PrintStream
{
	private Lexicon lexicon;

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
			print( "()" );
		}

		else
		{
			v.printTo( this );
		}
	}

	public void print( final Value v ) throws RippleException
	{
		if ( null == v )
		{
			print( "()" );
		}

		else
		{
			if ( v instanceof URI )
			{
				printUri( (URI) v );
			}

			else if ( v instanceof Literal )
			{
				URI dataTypeUri = ( (Literal) v ).getDatatype();
				String label = ( (Literal) v ).getLabel().toString();

				// Note: URI's equals() returns "true if the other object is an
				//       instance of URI  and their String-representations are
				//       equal, false  otherwise"
				if ( null != dataTypeUri )
				{
					if ( dataTypeUri.equals( XMLSchema.BOOLEAN ) )
					{
						print( label );
					}

					else if ( dataTypeUri.equals( XMLSchema.DOUBLE ) )
					{
						print( label );
					}

					else if ( dataTypeUri.equals( XMLSchema.INTEGER ) )
					{
						print( label );
					}

					else if ( dataTypeUri.equals( XMLSchema.STRING ) )
					{
						printEscapedString( label );
					}

					else
					{
						printEscapedString( label );
						print( "^^" );
						printUri( dataTypeUri );
					}
				}

				else
				{
					// For now, plain literals are printed as string-typed literals.
					printEscapedString( label );
				}

				String language = ( (Literal) v ).getLanguage();
				if ( null != language )
				{
					print( "@" + language );
				}
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

	private void printUriRef( final URI uri )
	{
		print( "<" + StringUtils.escapeUriString( uri.toString() ) + ">" );
	}

	private void printUri( final URI uri ) throws RippleException
	{
		String symbol = lexicon.symbolForUri( uri );

		if ( null == symbol )
		{
			printUriRef( uri );
		}

		else
		{
			print( symbol );
		}
	}

	private void printEscapedString( final String s )
	{
		print( '\"' );
		print( StringUtils.escapeString( s ) );
		print( '\"' );
	}
}

// kate: tab-width 4
