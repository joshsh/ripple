/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

public interface Sink<T>
{
	void put( T t ) throws RippleException;
}
