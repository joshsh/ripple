/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.graph;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.rdf.SesameInputAdapter;
import net.fortytwo.ripple.util.HTTPUtils;
import net.fortytwo.ripple.util.RDFHTTPUtils;
import net.fortytwo.ripple.flow.NullSink;
import net.fortytwo.ripple.flow.Sink;

import org.apache.commons.httpclient.HttpMethod;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;

/**
 * A primitive which consumes an information resource and produces a two-element
 * list (prefix name) for each namespace defined in the corresponding Semantic
 * Web document.
 */
public class Namespaces extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            GraphLibrary.NS_2008_08 + "namespaces",
            GraphLibrary.NS_2007_08 + "namespaces"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }
 
	public Namespaces()
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
        return "doc  =>  (prefix name)  -- for each namespace defined in document doc";
    }

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		String uri;

		uri = mc.toUri( stack.getFirst() ).toString();

		SesameInputAdapter sc = createAdapter( arg, solutions );

		HttpMethod method = HTTPUtils.createGetMethod( uri );
		HTTPUtils.setRdfAcceptHeader( method );
		RDFHTTPUtils.read( method, sc, uri, null );
		/*
		URLConnection uc = HttpUtils.openConnection( uri.toString() );
		HttpUtils.prepareUrlConnectionForRdfRequest( uc );
		RdfUtils.query( uc, sc, uri.toString() );*/
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

			// Push namespaces.
			private Sink<Namespace, RippleException> nsSink = new Sink<Namespace, RippleException>()
            {
                public void put( final Namespace ns ) throws RippleException
                {
                    resultSink.put( arg.with(
                            rest.push( mc.value( ns.getPrefix() ) )
                                    .push( mc.value( ns.getName() ) ) ) );
                }
            };

            // Discard comments.
			private Sink<String, RippleException> cmtSink = new NullSink<String, RippleException>();

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

