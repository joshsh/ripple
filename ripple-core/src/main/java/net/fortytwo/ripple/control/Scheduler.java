/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.control;

import java.util.Iterator;
import java.util.LinkedList;

import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.flow.NullSink;
import net.fortytwo.ripple.flow.Sink;

import org.apache.log4j.Logger;

public final class Scheduler
{
	private static final Logger LOGGER = Logger.getLogger( Scheduler.class );

	private static Scheduler singleInstance = null;

	private final LinkedList<TaskItem> taskQueue;
	private final LinkedList<WorkerRunnable> allRunnables;
	private final LinkedList<WorkerRunnable> waitingRunnables;
	private final int maxThreads;

	////////////////////////////////////////////////////////////////////////////

	public static void add( final Task task, final Sink<Task, RippleException> completedTaskSink ) throws RippleException
	{
		if ( null == singleInstance )
		{
			singleInstance = new Scheduler();
		}

		singleInstance.addPrivate( task, completedTaskSink );
	}

	public static void add( final Task task ) throws RippleException
	{
		add( task, new NullSink<Task, RippleException>() );
	}

	////////////////////////////////////////////////////////////////////////////

	private Scheduler() throws RippleException
	{
		taskQueue = new LinkedList<TaskItem>();
		allRunnables = new LinkedList<WorkerRunnable>();
		waitingRunnables = new LinkedList<WorkerRunnable>();

		maxThreads = Ripple.getProperties().getInt(Ripple.MAX_WORKER_THREADS);
	}

	private void addPrivate( final Task task, final Sink<Task, RippleException> completedTaskSink )
	{
		// Initialize the task immediately.  It may not begin executing for
		// some time.
		task.begin();

//System.out.println( "[" + this + "]addPrivate( " + task + ", ... )" );
		// Add the new task as a child of the currently executing task.
		Thread currentThread = Thread.currentThread();
		if ( currentThread instanceof WorkerThread )
		{
			Task parent = ( (WorkerThread) currentThread ).getCurrentTask();
//System.out.println( "    parent = " + parent );
			parent.addChild( task );
		}

		TaskItem taskItem = new TaskItem( task, completedTaskSink );

		synchronized ( taskQueue )
		{
			taskQueue.addLast( taskItem );

			// When the queue goes from empty to nonempty, there may be a number
			// of threads waiting for a task.  Notify the first in line that
			// a task is available.
			if ( 1 == taskQueue.size() && waitingRunnables.size() > 0 )
			{
//System.out.println( "    ( 1 == taskQueue.size() && waitingRunnables.size() > 0 )" );
				WorkerRunnable r = waitingRunnables.removeFirst();

				// Remove a task from the queue immediately.
				r.retrieveTask();

				// Alert the waiting runnable that it now has a task.
				synchronized ( r )
				{
					r.notify();
				}
			}

			// If there are more tasks than threads, and we have not reached the
			// maximum number of threads, then start a new one.
			else if ( allRunnables.size() < maxThreads )
			{
//System.out.println( "    taskQueue.size() > allRunnables.size() && allRunnables.size() < maxThreads" );
				WorkerRunnable r = new WorkerRunnable();
				allRunnables.add( r );
				Thread t = new WorkerThread( r );
				t.start();
			}
//else
//System.out.println( "Could not start a new thread" );
		}
//System.out.println( "    ### total number of worker runnables: " + allRunnables.size() );
//System.out.println( "    waitingRunnables.size(): " + waitingRunnables.size() );
//System.out.println( "    taskQueue.size(): " + taskQueue.size() );
	}

// has not been tested
	public void stopAll()
	{
		synchronized ( allRunnables )
		{
			Iterator<WorkerRunnable> iter = allRunnables.iterator();
			while ( iter.hasNext() )
			{
				WorkerRunnable r = iter.next();
				Task task = r.getCurrentTask();
				if ( null != task )
				{
					task.stop();
				}
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////

	private class TaskItem
	{
		public Task task;
		public Sink<Task, RippleException> sink;

		public TaskItem( final Task task, final Sink<Task, RippleException> sink )
		{
			this.task = task;
			this.sink = sink;
		}
	}

	private class WorkerThread extends Thread
	{
		private WorkerRunnable runnable;

		public WorkerThread( final WorkerRunnable r )
		{
			super( r );

			runnable = r;
		}

		public Task getCurrentTask()
		{
			return runnable.getCurrentTask();
		}
	}

	private class WorkerRunnable implements Runnable
	{
		private TaskItem currentTaskItem = null;

		public void run()
		{
//System.out.println( "[" + this + "]run()" );
			// Continue waiting for and executing tasks indefinitely.
			while ( true )
			{
				if ( null == currentTaskItem )
				{
					// Try to remove a task from the queue.
					synchronized ( taskQueue )
					{
//System.out.println( "    testing queue" );
						if ( taskQueue.size() > 0 )
						{
							currentTaskItem = taskQueue.removeFirst();
						}
					}
				}

				// If a task was found in the queue, execute it.
				if ( null != currentTaskItem )
				{
//System.out.println( "    found a task to execute" );
					try
					{
//System.out.println( "    executing task: " + currentTaskItem.task );
						currentTaskItem.task.execute();
					}
		
					// This is the end of the line for exceptions.
					catch ( Throwable t )
					{
						logThrowable( t );
					}

					// Even tasks which failed with a throwable are put into
					// the appropriate completed task sink.
					try
					{
						currentTaskItem.sink.put( currentTaskItem.task );
					}

					catch ( Throwable t )
					{
						logThrowable( t );
					}

					currentTaskItem = null;
				}

				// If there are no tasks in the queue, add this Runnable to a
				// list and wait.
				else
				{
//System.out.println( "    adding self to waiting queue" );
					synchronized ( taskQueue )
					{
						waitingRunnables.addLast( this );
					}

					synchronized ( this )
					{
						try
						{
							wait();
						}

						catch ( InterruptedException e )
						{
							LOGGER.warn( "worker runnable interrupted while waiting for new tasks" );
						}
					}
				}
			}
		}

		private void logThrowable( final Throwable t )
		{
			RippleException e;

			if ( t instanceof RippleException )
			{
				e = (RippleException) t;
			}

			else
			{
				if ( t instanceof InterruptedException )
				{
					LOGGER.warn( "task interrupted: " + currentTaskItem.task );
					return;
				}

				e = new RippleException( t );
			}

			try
			{
				e.logError();
			}

			catch ( Throwable secondary )
			{
				System.out.println( "failed to log error: " + t );
			}
		}

		public void retrieveTask()
		{
			currentTaskItem = taskQueue.removeFirst();
		}

		public Task getCurrentTask()
		{
			return currentTaskItem.task;
		}
	}
}

