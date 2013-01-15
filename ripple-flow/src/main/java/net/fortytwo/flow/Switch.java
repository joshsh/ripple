package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

/**
 * A modal pipeline which passes data alternately to one of two downstream sinks
 * @param <T> the type of data being passed
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Switch<T> implements Sink<T>
{
	private final Sink<T> left, right;
	private boolean state;

    /**
     * Constructs a new switch over the given sinks
     * @param left one of two downstream sinks
     * @param right the other of two downstream sinks
     */
	public Switch( final Sink<T> left, final Sink<T> right )
	{
		this.left = left;
		this.right = right;
		state = true;
	}

    /**
     * Receives the next data item and passes it to the appropriate downstream sink.
     * @param t the data item being passed
     * @throws RippleException if a data handling error occurs
     */
	public void put( final T t ) throws RippleException
	{
		( ( state ) ? left : right ).put( t );
	}

    /**
     * Toggles between the two downstream sinks.
     * When this method is called, one sink becomes inactive (idle) while the other becomes active (ready to receive data).
     */
	public void flip()
	{
		state = !state;
	}
}
