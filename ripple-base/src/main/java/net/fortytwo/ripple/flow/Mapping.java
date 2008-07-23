/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.flow;


public interface Mapping<D, R, E extends Exception>
{
	/**
	 * @return whether this function is referentially transparent w.r.t. all of its
	 * parameters.
	 */
	boolean isTransparent();

    // Open-world, push-based
    void apply( D arg, Sink<R, E> solutions ) throws E;

    /*
    // Open-world, pull-based
    Iterator<R> apply( D arg ) throws E;

    // Closed-world, push-based
    void apply( Iterator<D> source, Sink<R, E> sink ) throws E;

    // Closed-world, pull-based
    Iterator<R> apply( Iterator<D> source ) throws E;
    

    public class Count<D> implements Mapping<D, Integer, RippleException>
    {
        public void apply( Collection<D> source, Sink<Integer, RippleException> sink ) throws RippleException
        {
            sink.put( new Integer( source.size() ) );
        }
    }*/

}
