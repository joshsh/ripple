package net.fortytwo.flow.diff;

/**
 * Author: josh
 * Date: Jul 23, 2008
 * Time: 2:21:11 PM
 */
public interface DiffSource<T, E extends Exception>
{
    void writeTo( DiffSink<T, E> sink ) throws E;
}
