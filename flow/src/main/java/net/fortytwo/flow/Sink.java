/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow;

public interface Sink<T, E extends Exception>
{
	void put( T t ) throws E;
}
