/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.StringUtils;

public class URIAST implements AST<RippleList>
{
	private final String value;

	public URIAST( final String escapedValue )
	{
        String s;

        try
		{
			s = StringUtils.unescapeUriString( escapedValue );
		}

		catch ( RippleException e )
		{
			e.logError();
            s = null;
            System.exit( 1 );
		}

        this.value = s;
    }

	public void evaluate( final Sink<RippleList, RippleException> sink,
						final QueryEngine qe,
						final ModelConnection mc )
		throws RippleException
	{
		sink.put( mc.list( mc.value( mc.createURI( value ) ) ) );
	}

	public String toString()
	{
		return "<" + StringUtils.escapeURIString( value ) + ">";
	}
}

