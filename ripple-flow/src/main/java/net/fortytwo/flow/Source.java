package net.fortytwo.flow;

import net.fortytwo.ripple.RippleException;

/**
 * A push-based data source which passes data to a downstream sink
 * @param <T> the type of data being passed
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface Source<T>
{
    /**
     * Pushes this source's data into a given sink
     * @param sink the downstream sink to receive the data in this source
     * @throws RippleException if a data handling error occurs
     */
	void writeTo( Sink<T> sink ) throws RippleException;
}
