/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import java.util.regex.Pattern;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.flow.diff.DiffSink;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.diff.RDFDiffSink;

import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 * Note: several LexiconUpdaters may safely be attached to a single Lexicon.
 */
public class LexiconUpdater implements RDFDiffSink<RippleException>
{
// TODO: Unicode characters supported by the lexer / Turtle grammar
	private static final Pattern PREFIX_PATTERN
		= Pattern.compile( "[A-Za-z][-0-9A-Z_a-z]*" );

	private final RDFSink<RippleException> addSink, subSink;
    private final DiffSink<Statement, RippleException> stSink;
    private final DiffSink<Namespace, RippleException> nsSink;
    private final DiffSink<String, RippleException> cmtSink;

    public LexiconUpdater( final Lexicon lexicon ) throws RippleException
    {
		final boolean override = Ripple.getConfiguration().getBoolean(
                Ripple.PREFER_NEWEST_NAMESPACE_DEFINITIONS );
		final boolean allowDuplicateNamespaces = Ripple.getConfiguration().getBoolean(
                Ripple.ALLOW_DUPLICATE_NAMESPACES );

		addSink = new RDFSink<RippleException>()
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
							lexicon.addURI( (URI) subj );
						}
				
						lexicon.addURI( pred );
				
						if ( obj instanceof URI )
						{
							lexicon.addURI( (URI) obj );
						}
					}
				}
			};

			private Sink<Namespace, RippleException> nsSink = new Sink<Namespace, RippleException>()
			{
				public void put( final Namespace ns ) throws RippleException
				{
// TODO: delete me
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
		subSink = new RDFSink<RippleException>()
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

        stSink = new DiffSink<Statement, RippleException>()
        {
            public Sink<Statement, RippleException> getPlus()
            {
                return addSink.statementSink();
            }

            public Sink<Statement, RippleException> getMinus()
            {
                return subSink.statementSink();
            }
        };

        nsSink = new DiffSink<Namespace, RippleException>()
        {
            public Sink<Namespace, RippleException> getPlus()
            {
                return addSink.namespaceSink();
            }

            public Sink<Namespace, RippleException> getMinus()
            {
                return subSink.namespaceSink();
            }
        };

        cmtSink = new DiffSink<String, RippleException>()
        {
            public Sink<String, RippleException> getPlus()
            {
                return addSink.commentSink();
            }

            public Sink<String, RippleException> getMinus()
            {
                return subSink.commentSink();
            }
        };
	}

	public RDFSink<RippleException> adderSink()
	{
		return addSink;
	}

	public RDFSink<RippleException> subtractorSink()
	{
		return subSink;
	}

    public DiffSink<Statement, RippleException> statementSink()
    {
        return stSink;
    }

    public DiffSink<Namespace, RippleException> namespaceSink()
    {
        return nsSink;
    }

    public DiffSink<String, RippleException> commentSink()
    {
        return cmtSink;
    }

    private boolean allowedNsPrefix( final String nsPrefix )
	{
		return PREFIX_PATTERN.matcher( nsPrefix ).matches();
	}
}
