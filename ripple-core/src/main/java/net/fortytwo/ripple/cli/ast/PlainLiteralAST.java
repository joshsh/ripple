/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.StringUtils;

public class PlainLiteralAST implements AST<RippleList>
{
	private final String value, language;

	public PlainLiteralAST( final String value )
	{
        this.value = value;
        language = null;
	}

	public PlainLiteralAST( final String value, final String language )
	{
        this.value = value;
        this.language = language;
	}

	public void evaluate( final Sink<RippleList, RippleException> sink,
						final QueryEngine qe,
						final ModelConnection mc )
		throws RippleException
	{
		RippleValue v = ( null == language )
			? mc.value( value )
			: mc.value( value, language );
		sink.put( mc.list( v ) );
	}

	public String toString()
	{
		return "\"" + StringUtils.escapeString( value ) + "\""
			+ ( ( null == language ) ? "" : ( "@" + language ) );
	}
}

