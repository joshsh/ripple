/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.control;

import java.util.LinkedList;

import net.fortytwo.ripple.RippleException;

/**
 * A queue of tasks which are executed sequentially and in FIFO order.
 */
public class TaskQueue extends Task
{
	private LinkedList<Task> queue = new LinkedList<Task>();

	public void add( final Task task )
	{
//System.out.println( "[" + this + "]add( " + task + ")" );
		queue.add( task );
//System.out.println( "    [+] queue.size() is now: " + queue.size() );
	}

	/**
	 * Note: all tasks are to have been added to the queue before this method is
	 * called.
	 */
	protected void executeProtected() throws RippleException
	{
//System.out.println( "[" + this + "].executeProtected()" );
		while ( queue.size() > 0 )
		{
			Task task = queue.removeFirst();
			Scheduler.add( task );
			task.waitUntilFinished();
		}
//System.out.println( "    done." );
	}

	protected void stopProtected()
	{
//System.out.println( "stopping all tasks currently executing: " + this );
	}
}

// kate: tab-width 4
