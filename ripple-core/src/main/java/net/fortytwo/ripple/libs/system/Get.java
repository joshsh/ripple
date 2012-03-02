/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.system;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.util.HTTPUtils;
import net.fortytwo.flow.Sink;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.openrdf.model.vocabulary.XMLSchema;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A primitive which consumes an information resource, issues a GET request for
 * the resource, then produces the retrieved data as a string.
 */
public class Get extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            SystemLibrary.NS_2011_08 + "get",
            SystemLibrary.NS_2008_08 + "get",
            SystemLibrary.NS_2007_08 + "get",
            SystemLibrary.NS_2007_05 + "get"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "uri", null, true )};
    }

    public String getComment()
    {
        return "issues a GET request and produces the received data as a string";
    }

    public Get()
		throws RippleException
	{
		super();
	}

    public void apply(final RippleList arg,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {
		RippleList stack = arg;

		String result;

		String uriStr = stack.getFirst().toString();
        //uriStr = mc.getModel().getURIMap().get( uriStr );
        stack = stack.getRest();

		HttpMethod method = HTTPUtils.createGetMethod( uriStr );
		HTTPUtils.registerMethod( method );

		HttpClient client = HTTPUtils.createClient();

		InputStream body;

		try
		{
			client.executeMethod( method );
	        body = method.getResponseBodyAsStream();
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}

		try
		{
			BufferedReader br = new BufferedReader(
				new InputStreamReader( body ) );
			StringBuffer sb = new StringBuffer();
			String nextLine = "";
			boolean first = true;
			while ( ( nextLine = br.readLine() ) != null )
			{
				if ( first )
				{
					first = false;
				}

				else
				{
					sb.append( '\n' );
				}

				sb.append( nextLine );
			}
			result = sb.toString();

			body.close();
		}

		catch ( java.io.IOException e )
		{
			throw new RippleException( e );
		}

		try
		{
			method.releaseConnection();
		}

		catch ( Throwable t )
		{
			throw new RippleException( t );
		}

		solutions.put( stack.push( mc.typedValue(result, XMLSchema.STRING) ) );
	}
}

