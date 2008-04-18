/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import java.util.Iterator;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.io.RipplePrintStream;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.flow.Collector;
import net.fortytwo.ripple.flow.Sink;

import org.openrdf.model.Namespace;

public class ShowNamespacesCmd extends Command
{
	public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
		final RipplePrintStream ps = qe.getPrintStream();

		final Collector<Namespace, RippleException> coll = new Collector<Namespace, RippleException>();
		mc.putNamespaces( coll );
		int max = 0;
		Iterator<Namespace> iter = coll.iterator();
		int j = 0;
		while ( iter.hasNext() )
		{
			int len = ( iter.next().getPrefix() + j ).length();
			if ( len > max )
			{
				max = len;
			}
			j++;
		}
		final int maxlen = max + 4;

		Sink<Namespace, RippleException> printSink = new Sink<Namespace, RippleException>()
		{
			private int i = 0;

			public void put( final Namespace ns ) throws RippleException
			{
				String prefix = "[" + i++ + "] " + ns.getPrefix() + ":";
				int len = prefix.length();
				ps.print( prefix );

				for ( int i = 0; i < maxlen - len + 2; i++ )
				{
					ps.print( ' ' );
				}

				ps.print( ns.getName() );
				ps.print( '\n' );
			}
		};

		ps.println( "" );
		coll.writeTo( printSink );
		ps.println( "" );
	}

	protected void abort()
	{
	}
}

// kate: tab-width 4
