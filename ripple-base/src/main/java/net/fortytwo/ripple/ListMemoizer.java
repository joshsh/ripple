/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple;

import java.util.Comparator;

// TODO: balancing operations

/**
 * A space-efficient map using where the key values are linked lists.
 */
public class ListMemoizer<T, M>
{
    private final Comparator<T> comparator;
    private ListMemoizerInner inner;

    public ListMemoizer( final Comparator<T> comparator )
    {
        this.comparator = comparator;
        this.inner = null;
    }

    public M get( final ListNode<T> list )
    {
        return ( null == inner )
                ? null
                : inner.get( list );
    }

    public boolean put( final ListNode<T> list, final M memo )
    {
        if ( null == inner )
        {
            inner = new ListMemoizerInner( list, memo );
            return true;
        }

        else
        {
            return inner.put( list, memo );
        }
    }

    private class ListMemoizerInner
    {
        private final T first;

        private M memo;
        private ListMemoizerInner left, right;
        private ListMemoizerInner rest;

        public ListMemoizerInner( final ListNode<T> list, final M memo )
        {
            if ( null == list )
            {
                throw new IllegalArgumentException( "the empty list cannot be memoized" );
            }

            first = list.getFirst();
            left = null;
            right = null;

            ListNode<T> r = list.getRest();
            if ( null == r )
            {
                rest = null;
                this.memo = memo;
            }

            else
            {
                rest = new ListMemoizerInner( r, memo );
                this.memo = null;
            }
        }

        public boolean put( final ListNode<T> list, final M memo )
        {
            if ( null == list )
            {
                throw new IllegalArgumentException( "the empty list cannot be memoized" );
            }

            int cmp = comparator.compare( this.first, list.getFirst() );

            if ( 0 == cmp )
            {
                ListNode<T> r = list.getRest();

                if ( null == r )
                {
                    if ( null == this.memo )
                    {
                        this.memo = memo;
                        return true;
                    }

                    else
                    {
                        return false;
                    }
                }

                else
                {
                    if ( null == this.rest )
                    {
                        this.rest = new ListMemoizerInner( r, memo );
                        return true;
                    }

                    else
                    {
                        return this.rest.put( r, memo );
                    }
                }
            }

            else if ( cmp < 0 )
            {
                if ( null == this.left )
                {
                    this.left = new ListMemoizerInner( list, memo );
                    return true;
                }

                else
                {
                    return this.left.put( list, memo );
                }
            }

            else
            {
                if ( null == this.right )
                {
                    this.right = new ListMemoizerInner( list, memo );
                    return true;
                }

                else
                {
                    return this.right.put( list, memo );
                }
            }
        }

        public M get( final ListNode<T> list )
        {
            if ( null == list )
            {
                return null;
            }

            int cmp = comparator.compare( this.first, list.getFirst() );

            if ( 0 == cmp )
            {
                ListNode<T> r = list.getRest();

                if ( null == r )
                {
                    return this.memo;
                }

                else
                {
                    return ( null == this.rest )
                            ? null
                            : this.rest.get( r );
                }
            }

            else if ( cmp < 0 )
            {
                return ( null == this.left )
                        ? null
                        : this.left.get( list );
            }

            else
            {
                return ( null == this.right )
                        ? null
                        : this.right.get( list );
            }
        }

        ////////////////////////////////////////////////////////////////////////////

        private int compare( final ListMemoizerInner first,
                            final ListMemoizerInner second )
        {
            if ( null == first )
            {
                return ( null == second )
                    ? 0 : -1;
            }

            else if ( null == second )
            {
                return 1;
            }

            else
            {
                return first.compareTo( second );
            }
        }

        private int compareTo( final ListMemoizerInner other )
        {
            int cmp = comparator.compare( this.first, other.first );

            if ( 0 != cmp )
            {
                return cmp;
            }

            cmp = compare( this.left, other.left );
            if ( 0 != cmp )
            {
                return cmp;
            }

            cmp = compare( this.rest, other.rest );
            if ( 0 != cmp )
            {
                return cmp;
            }

            cmp = compare( this.right, other.right );
            return cmp;
        }
    }
}

