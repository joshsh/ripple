package net.fortytwo.ripple.query.commands;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.cli.ParserQuitException;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.model.ModelConnection;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class QuitCmd extends Command
{
	public void execute( final QueryEngine qe, final ModelConnection mc )
		throws RippleException
	{
		throw new ParserQuitException();
	}

    public String getName() {
        return "quit";
    }

    protected void abort()
	{
	}
}

