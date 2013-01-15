package net.fortytwo.flow.diff;

import net.fortytwo.ripple.RippleException;

/**
 * A source of two-channel "diff" data
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface DiffSource<T>
{
    /**
     * Writes all of the data in this source to the specified diff sink
     * @param sink the downstream recipient of the diff
     * @throws RippleException if a data handling error occurs
     */
    void writeTo( DiffSink<T> sink ) throws RippleException;
}
