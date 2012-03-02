/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2012 Joshua Shinavier
 */


package net.fortytwo.flow.diff;

import net.fortytwo.flow.Sink;

/**
 * Author: josh
 * Date: Mar 7, 2008
 * Time: 1:20:34 PM
 */
public interface DiffSink<T>
{
    Sink<T> getPlus();
    Sink<T> getMinus();
}
