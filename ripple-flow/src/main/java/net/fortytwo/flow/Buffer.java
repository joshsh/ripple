package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

/**
 * A collection of data items which grows until "flushed" to a downstream sink.
 * @param <T> the type of data being passed
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class Buffer<T> extends Collector<T>
{
	private final Sink<T> sink;

    /**
     * Constructs a new buffer of the given type
     * @param sink the downstream sink to which to pass the received data items when the buffer is flushed
     */
	public Buffer( final Sink<T> sink )
	{
		super();

		this.sink = sink;
	}

    /**
     * Empties the buffer, pushing the collected data items in FIFO order to the downstream sink
     * @throws RippleException
     */
	public void flush() throws RippleException
	{
		writeTo( sink );

		clear();
	}
}
