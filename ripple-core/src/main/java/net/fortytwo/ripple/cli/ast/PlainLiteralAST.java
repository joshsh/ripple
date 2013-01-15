package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.StringUtils;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
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

	public void evaluate( final Sink<RippleList> sink,
						final QueryEngine qe,
						final ModelConnection mc )
		throws RippleException
	{
		RippleValue v = ( null == language )
			? mc.plainValue(value)
			: mc.languageTaggedValue(value, language);
		sink.put( mc.list().push(v) );
	}

	public String toString()
	{
		return "\"" + StringUtils.escapeString( value ) + "\""
			+ ( ( null == language ) ? "" : ( "@" + language ) );
	}
}

