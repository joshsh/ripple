/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.control;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;

/**
 * A set of tasks which are executed concurrently.  Tasks are scheduled for
 * execution as soon as they are added to the set.
 */
public class TaskSet
{
	private int count = 0;

	private final Sink<Task> completedTaskSink = new Sink<Task>()
	{
		public synchronized void put( final Task task ) throws RippleException
		{
//System.out.println( "put( " + task + ")" );
			count--;
//System.out.println( "    [-] count is now: " + count );

			if ( 0 == count )
			{
				notify();
			}
		}
	};

	public void add( final Task task ) throws RippleException
	{
//System.out.println( "add( " + task + ")" );
		synchronized ( completedTaskSink )
		{
			count++;
//System.out.println( "    [+] count is now: " + count );
		}

		Scheduler.add( task, completedTaskSink );
	}

	/**
	 * Note: all tasks are to have been added to the set before this method is
	 * called, while there may be any number of tasks which have not finished
	 * executing.
	 */
	public void waitUntilEmpty() throws RippleException
	{
//System.out.println( "[" + this + "].waitUntilEmpty()" );
		synchronized ( completedTaskSink )
		{
			if ( count > 0 )
			{
				try
				{
					completedTaskSink.wait();
				}
	
				catch ( java.lang.InterruptedException e )
				{
					throw new RippleException( "interrupted while waiting to complete tasks" );
				}
			}
		}
//System.out.println( "    done." );
	}

	public void stopWaiting()
	{
		synchronized ( completedTaskSink )
		{
			completedTaskSink.notify();
		}
	}
}

