/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.cli.ParserQuitException;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;

public class QuitCmd extends Command
{
	public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
		throw new ParserQuitException();
	}

	protected void abort()
	{
	}
}

