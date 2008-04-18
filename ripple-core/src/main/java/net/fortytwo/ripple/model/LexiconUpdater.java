/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import java.util.regex.Pattern;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.rdf.diff.RdfDiffSink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 * Note: several LexiconUpdaters may safely be attached to a single Lexicon.
 */
public class LexiconUpdater implements RdfDiffSink
{
// TODO: Unicode characters supported by the lexer / Turtle grammar
	private static final Pattern PREFIX_PATTERN
		= Pattern.compile( "[A-Za-z][-0-9A-Z_a-z]*" );

	private RDFSink addSink, subSink;

	public LexiconUpdater( final Lexicon lexicon ) throws RippleException
    {
		final boolean override = Ripple.getProperties().getBoolean(
                Ripple.PREFER_NEWEST_NAMESPACE_DEFINITIONS );
		final boolean allowDuplicateNamespaces = Ripple.getProperties().getBoolean(
                Ripple.ALLOW_DUPLICATE_NAMESPACES );

		addSink = new RDFSink()
		{
			private Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>()
			{
				public void put( final Statement st ) throws RippleException
				{
//System.out.println( "st = " + st );
					Resource subj = st.getSubject();
					URI pred = st.getPredicate();
					Value obj = st.getObject();
			
					synchronized ( lexicon )
					{
						if ( subj instanceof URI )
						{
							lexicon.add( (URI) subj );
						}
				
						lexicon.add( pred );
				
						if ( obj instanceof URI )
						{
							lexicon.add( (URI) obj );
						}
					}
				}
			};

			private Sink<Namespace, RippleException> nsSink = new Sink<Namespace, RippleException>()
			{
				public void put( final Namespace ns ) throws RippleException
				{
					if ( !allowedNsPrefix( ns.getPrefix() ) )
					{
						return;
					}
				
					synchronized ( lexicon )
					{
						if ( override || null == lexicon.resolveNamespacePrefix( ns.getPrefix() ) )
						{
							if ( allowDuplicateNamespaces || null == lexicon.nsPrefixOf( ns.getName() ) )
							{
								lexicon.add( ns );
							}
						}
					}
				}
			};

			private Sink<String, RippleException> cmtSink = new Sink<String, RippleException>()
			{
				public void put( final String comment ) throws RippleException
				{
				}
			};

			public Sink<Statement, RippleException> statementSink()
			{
				return stSink;
			}
		
			public Sink<Namespace, RippleException> namespaceSink()
			{
				return nsSink;
			}
		
			public Sink<String, RippleException> commentSink()
			{
				return cmtSink;
			}
		};

// TODO
		subSink = new RDFSink()
		{
			private Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>()
			{
				public void put( final Statement st ) throws RippleException
				{
				}
			};

			private Sink<Namespace, RippleException> nsSink = new Sink<Namespace, RippleException>()
			{
				public void put( final Namespace ns ) throws RippleException
				{
				}
			};

			private Sink<String, RippleException> cmtSink = new Sink<String, RippleException>()
			{
				public void put( final String comment ) throws RippleException
				{
				}
			};

			public Sink<Statement, RippleException> statementSink()
			{
				return stSink;
			}
		
			public Sink<Namespace, RippleException> namespaceSink()
			{
				return nsSink;
			}
		
			public Sink<String, RippleException> commentSink()
			{
				return cmtSink;
			}
		};
	}

	public RDFSink adderSink()
	{
		return addSink;
	}

	public RDFSink subtractorSink()
	{
		return subSink;
	}

	private boolean allowedNsPrefix( final String nsPrefix )
	{
		return PREFIX_PATTERN.matcher( nsPrefix ).matches();
	}
}

// kate: tab-width 4
