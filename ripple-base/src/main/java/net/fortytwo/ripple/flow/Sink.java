/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.flow;

public interface Sink<T, E extends Exception>
{
	void put( T t ) throws E;
}
