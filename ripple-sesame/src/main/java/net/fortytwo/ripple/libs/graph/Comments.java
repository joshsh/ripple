/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.NullSink;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.flow.rdf.RDFSink;
import net.fortytwo.flow.rdf.SesameInputAdapter;
import net.fortytwo.ripple.util.HTTPUtils;
import net.fortytwo.ripple.util.RDFHTTPUtils;

import org.apache.commons.httpclient.HttpMethod;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * A primitive which consumes an information resource and produces each of
 * the comments matched in the corresponding RDF document. Note: with the
 * current Sesame bindings, nothing is actually matched.
 */
public class Comments extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "comments",
            GraphLibrary.NS_2007_08 + "comments"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public Comments()
		throws RippleException
	{
		super();
	}
    
    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "doc", "a Semantic Web document", true )};
    }

    public String getComment()
    {
        return "doc  =>  comment  -- for each comment in doc";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		String uri;

		uri = stack.getFirst().toString();

		SesameInputAdapter sc = createAdapter( arg, solutions  );

		HttpMethod method = HTTPUtils.createGetMethod( uri );
		HTTPUtils.setRdfAcceptHeader( method );
		RDFHTTPUtils.read( method, sc, uri, null );
	}

	static SesameInputAdapter createAdapter( final StackContext arg,
										final Sink<StackContext, RippleException> resultSink )
	{
		final ModelConnection mc = arg.getModelConnection();
		final RippleList rest = arg.getStack().getRest();

		RDFSink rdfSink = new RDFSink()
		{
			// Discard statements.
			private Sink<Statement, RippleException> stSink = new NullSink<Statement, RippleException>();

			// Discard namespaces.
			private Sink<Namespace, RippleException> nsSink = new NullSink<Namespace, RippleException>();

			// Push comments.
			private Sink<String, RippleException> cmtSink = new Sink<String, RippleException>()
			{
				public void put( final String comment )
					throws RippleException
				{
					resultSink.put( arg.with(
						rest.push( mc.value( comment, XMLSchema.STRING ) ) ) );
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

		SesameInputAdapter sc = new SesameInputAdapter( rdfSink );

		return sc;
	}
}

