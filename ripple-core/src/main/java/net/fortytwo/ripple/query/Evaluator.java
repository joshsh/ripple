/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.ripple.flow.Mapping;

/**
*  A function which reduces arbitrary expressions to a particular normal form.
*/
public abstract class Evaluator<D, R, E extends Exception> implements Mapping<D, R, E>
{
	/*public int arity()
	{
		// An Evaluator performs reduction; it does not need the stack to be
		// reduced to any level before it is received as input.
		return 0;
	}*/

	public abstract void stop();
	
	public boolean isTransparent()
	{
		return true;
	}
}
