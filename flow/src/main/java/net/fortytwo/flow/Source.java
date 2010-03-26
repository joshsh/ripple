/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.flow;

public interface Source<T, E extends Exception>
{
	void writeTo( Sink<T, E> sink ) throws E;
}
