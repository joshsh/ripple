package net.fortytwo.ripple.flow;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Author: josh
 * Date: Jul 23, 2008
 * Time: 8:28:14 PM
 */
public class DiffMultitee<T, E extends Exception> implements DiffSink<T, E>
{
    private final Collection<DiffSink<T, E>> subscribers;
    private final Sink<T, E> plusSink;
    private final Sink<T, E> minusSink;

    public DiffMultitee()
    {
        subscribers = new LinkedList<DiffSink<T, E>>();

        plusSink = new Sink<T, E>()
        {
            public void put( final T t ) throws E
            {
                for ( DiffSink<T, E> s : subscribers )
                {
                    s.getPlus().put( t );
                }
            }
        };

        minusSink = new Sink<T, E>()
        {
            public void put( final T t ) throws E
            {
                for ( DiffSink<T, E> s : subscribers )
                {
                    s.getMinus().put( t );
                }
            }
        };
    }

    public void subscribe( final DiffSink<T, E> subscriber )
    {
        subscribers.add( subscriber );
    }

    public Sink<T, E> getPlus()
    {
        return plusSink;
    }

    public Sink<T, E> getMinus()
    {
        return minusSink;
    }
}
