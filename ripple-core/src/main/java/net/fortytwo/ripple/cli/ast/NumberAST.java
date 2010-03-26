/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.cli.ast;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.flow.Sink;

public abstract class NumberAST implements AST<RippleList>
{
	public abstract NumericValue getValue( ModelConnection mc ) throws RippleException;

    public void evaluate( final Sink<RippleList, RippleException> sink,
						final QueryEngine qe,
						final ModelConnection mc )
		throws RippleException
	{
		sink.put( mc.list( getValue( mc ) ) );
	}

    // Strip off leading "+" from mantissa and/or exponent.
    // Convert exponent portion to lower case.
    // Eliminate trailing decimal point.
    // Do not bother with leading or trailing zeroes.
    protected String canonicalize( final String rep )
    {
        return rep.toLowerCase()
                .replaceAll( "[+]", "" )
                .replaceAll( "[.]$", "" )
                .replaceAll( "[.]e", "" );
    }
}
