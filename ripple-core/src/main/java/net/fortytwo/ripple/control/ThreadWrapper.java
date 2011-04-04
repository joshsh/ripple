/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.control;

import net.fortytwo.ripple.RippleException;

public abstract class ThreadWrapper
{
	private final String name;

	private boolean finished;
	private RippleException error;

	protected abstract void run() throws RippleException;

	public ThreadWrapper( final String name )
	{
		this.name = name;
	}

	private void setFinished( final boolean f )
    {
        finished = f;
    }

    private void setError( final RippleException e )
    {
        error = e;
    }

    private boolean getFinished()
    {
        return finished;
    }

    private RippleException getError()
    {
        return error;
    }

	public void start( final long timeout ) throws RippleException
	{
		setFinished( false );
		setError( null );

		final Object monitorObj = "";
		final ThreadWrapper tw = this;

		Runnable target = new Runnable() {
			public void run()
			{
				try
				{
					tw.run();
				}

				catch ( RippleException e )
				{
					setError( e );
				}

				setFinished( true );
				synchronized ( monitorObj )
				{
					monitorObj.notify();
				}
			}
		};

		Thread t = new Thread( target, name );
		t.start();

		try
		{
			synchronized ( monitorObj )
			{
				if ( timeout > 0 )
				{
					monitorObj.wait( timeout );
				}
				else
				{
					monitorObj.wait();
				}
			}

			if ( !getFinished() )
			{
				t.interrupt();
			}
		}

		catch ( InterruptedException e )
		{
			throw new RippleException( e );
		}

		if ( !getFinished() )
		{
			throw new RippleException( "operation timed out" );
		}

		else if ( null != getError() )
		{
			throw getError();
		}
	}

	public void start() throws RippleException
	{
		start( -1 );
	}
}

