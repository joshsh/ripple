/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Mapping;

/**
 * A streaming filter which maps stacks to stacks.
 */
public interface StackMapping extends Mapping<RippleList, RippleList, ModelConnection> {
    /**
     * @return the fixed number of arguments which this function consumes before computing a result.
     */
    int arity();

    /**
     * @return the <code>StackMapping</code> exactly inverse to this mapping,
     *         or a <code>NullMapping</code> if no such mapping can be defined.
     * @throws RippleException if the inverse cannot be determined
     */
    StackMapping getInverse() throws RippleException;
}
