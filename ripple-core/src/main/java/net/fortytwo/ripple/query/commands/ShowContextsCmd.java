/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.io.RipplePrintStream;

public class ShowContextsCmd extends Command
{
	public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
		final RipplePrintStream ps = qe.getPrintStream();

		Sink<RippleValue, RippleException> printSink = new Sink<RippleValue, RippleException>()
		{
			private int i = 0;

			public void put( final RippleValue v ) throws RippleException
			{
				ps.print( "[" + i++ + "] " );
				ps.println( v );
			}
		};

		ps.println( "" );
		mc.putContexts( printSink );
		ps.println( "" );
	}

	protected void abort()
	{
	}
}

// kate: tab-width 4
