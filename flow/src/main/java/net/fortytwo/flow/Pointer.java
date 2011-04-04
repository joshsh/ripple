/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.flow;

public class Pointer<T>
{
	private T ref;

	public Pointer()
	{
		ref = null;
	}

	public Pointer( final T ref )
	{
		this.ref = ref;
	}

	public T getRef()
	{
		return ref;
	}

	public void setRef( final T ref )
	{
		this.ref = ref;
	}
}
