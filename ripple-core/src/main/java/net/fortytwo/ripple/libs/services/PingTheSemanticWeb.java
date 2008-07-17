/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.services;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.etc.EtcLibrary;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import org.jdom.input.SAXBuilder;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 * A primitive which consumes a maximum number of ping results and a string
 * representing a type of Semantic Web document, and issues the corresponding
 * request to the PingTheSemanticWeb service, producing all results as
 * information resources.
 */
public class PingTheSemanticWeb extends PrimitiveStackMapping
{
	private static final int ARITY = 2;
	private static final String MSG = "Note: the PingTheSemanticWeb API has just (as of Aug 27, 2007) undergone major changes.  Check the latest release of Ripple for an updated pingTheSemanticWeb primitive!";

	private static SAXBuilder saxBuilder = null;
	private static void initialize()
	{
		saxBuilder = new SAXBuilder( true );
		saxBuilder.setReuseParser( true );

		String schemaLocation = PingTheSemanticWeb.class.getResource( "pingTheSemanticWeb.xsd" ).toString();
		saxBuilder.setFeature(
			"http://apache.org/xml/features/validation/schema", true );
		saxBuilder.setProperty( "http://apache.org/xml/properties/schema/"
			+ "external-noNamespaceSchemaLocation", schemaLocation );
	}

    private static final String[] IDENTIFIERS = {
            ServicesLibrary.NS_2008_08 + "pingTheSemanticWeb",
            ServicesLibrary.NS_2007_08 + "pingTheSemanticWeb",
            EtcLibrary.NS_2007_05 + "pingTheSemanticWeb"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

	public PingTheSemanticWeb()
		throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions )
		throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();

		if ( null == saxBuilder )
		{
			initialize();
		}

/*
		String type;
		int maxResults;
*/
//		type = mc.stringValue( stack.getFirst() );
		stack = stack.getRest();
//		maxResults = mc.intValue( stack.getFirst() );
		stack = stack.getRest();

solutions.put( arg.with(
		stack.push( mc.value( MSG, XMLSchema.STRING ) ) ) );
/*
		URLConnection urlConn = HttpUtils.openConnection(
			"http://pingthesemanticweb.com/export/?serialization=xml&ns=&domain=&timeframe=any_time&type=" + type + "&nbresults=" + maxResults );

		String[] mimeTypes = { "text/xml" };
		HttpUtils.prepareUrlConnectionForRequest( urlConn, mimeTypes );
		HttpUtils.connect( urlConn );

		Document doc;

		try
		{
			InputStream response = urlConn.getInputStream();

			synchronized ( saxBuilder )
			{
				doc = saxBuilder.build( response );
			}

			response.close();
		}

		catch ( java.io.IOException e )
		{
			throw new RippleException( e );
		}

		catch ( org.jdom.JDOMException e )
		{
			throw new RippleException( e );
		}

		Element root = doc.getRootElement();
		Iterator<Element> childIter = root.getChildren().iterator();
		while ( childIter.hasNext() )
		{
			Element child = childIter.next();
			String s = child.getAttributeValue( "url" );
			sink.put( mc.list(
				new RdfValue( mc.createUri( s ) ), stack ) );
		}
*/
	}
}

