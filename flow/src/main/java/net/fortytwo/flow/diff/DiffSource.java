package net.fortytwo.flow.diff;

import net.fortytwo.ripple.RippleException;

/**
 * Author: josh
 * Date: Jul 23, 2008
 * Time: 2:21:11 PM
 */
public interface DiffSource<T>
{
    void writeTo( DiffSink<T> sink ) throws RippleException;
}
