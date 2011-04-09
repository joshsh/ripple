/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.StringUtils;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RDFValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;

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
        RDFValue v = mc.uriValue(value);
		sink.put( mc.list( mc.canonicalValue( v.sesameValue() ) ) );
	}

	public String toString()
	{
		return "<" + StringUtils.escapeURIString( value ) + ">";
	}
}

