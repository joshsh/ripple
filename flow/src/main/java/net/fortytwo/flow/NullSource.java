/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

public class NullSource<T> implements Source<T>
{
	public void writeTo( Sink<T> sink ) throws RippleException
	{
	}
}
