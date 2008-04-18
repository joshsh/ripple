/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;

public class UndefineTermCmd extends Command
{
	private String term;

	public UndefineTermCmd( final String term )
	{
		this.term = term;
	}

	public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
		mc.removeStatementsAbout(
				mc.createUri( qe.getDefaultNamespace() + term ) );
		mc.commit();
	}

	protected void abort()
	{
	}
}

// kate: tab-width 4
