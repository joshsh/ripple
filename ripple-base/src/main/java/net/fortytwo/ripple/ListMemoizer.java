/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
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
    private ListMemoizerHelper helper;

    public ListMemoizer( final Comparator<T> comparator )
    {
        this.comparator = comparator;
        this.helper = null;
    }

    public M get( final ListNode<T> list )
    {
        if ( null == list )
        {
            throw new IllegalArgumentException( "null key" );
        }

        return ( null == helper)
                ? null
                : helper.get( list );
    }

    /**
     * Adds a new value to the map.
     * @param list the list-valued key
     * @param memo the value to store
     * @return whether the key/memo pair has been added to the store.
     * A return value of false indicates that this memoizer already contains a memo for the given key,
     * and that the new memo has not been added.
     */
    public boolean put( final ListNode<T> list, final M memo )
    {
        if ( null == list )
        {
            throw new IllegalArgumentException( "null key" );
        }

        if ( list.isNil() )
        {
            throw new IllegalArgumentException( "the empty list cannot be memoized" );
        }

        if ( null == helper)
        {
            helper = new ListMemoizerHelper( list, memo );
            return true;
        }

        else
        {
            return helper.put( list, memo );
        }
    }

    public boolean remove( final ListNode<T> list )
    {
        if ( null == list )
        {
            throw new IllegalArgumentException( "null key" );
        }

        return !list.isNil() && null != helper && helper.remove( list );
    }

    private class ListMemoizerHelper
    {
        private final T first;

        private M memo;
        private ListMemoizerHelper left, right;
        private ListMemoizerHelper rest;

        public ListMemoizerHelper(final ListNode<T> list, final M memo)
        {
            first = list.getFirst();
            left = null;
            right = null;

            ListNode<T> r = list.getRest();
            if ( r.isNil() )
            {
                rest = null;
                this.memo = memo;
            }

            else
            {
                rest = new ListMemoizerHelper( r, memo );
                this.memo = null;
            }
        }

        // TODO: remove empty branches
        public boolean remove( final ListNode<T> list )
        {
            int cmp = comparator.compare( this.first, list.getFirst() );

            if ( 0 == cmp )
            {
                ListNode<T> r = list.getRest();

                if ( r.isNil() )
                {
                    if ( null != this.memo )
                    {
                        this.memo = null;
                        return true;
                    }

                    else
                    {
                        return false;
                    }
                }

                else
                {
                    return null != this.rest && this.rest.remove( r );
                }
            }

            else if ( cmp < 0 )
            {
                return null != this.left && this.left.remove(list);
            }

            else
            {
                return null != this.right && this.right.remove(list);
            }
        }

        public boolean put( final ListNode<T> list, final M memo )
        {
            int cmp = comparator.compare( this.first, list.getFirst() );

            if ( 0 == cmp )
            {
                ListNode<T> r = list.getRest();

                if ( r.isNil() )
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
                        this.rest = new ListMemoizerHelper( r, memo );
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
                    this.left = new ListMemoizerHelper( list, memo );
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
                    this.right = new ListMemoizerHelper( list, memo );
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
            //System.out.println("getting " + list);
            if ( list.isNil() )
            {
                return null;
            }

            int cmp = comparator.compare( this.first, list.getFirst() );

            if ( 0 == cmp )
            {
                ListNode<T> r = list.getRest();

                if ( r.isNil() )
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

        private int compare( final ListMemoizerHelper first,
                            final ListMemoizerHelper second )
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

        private int compareTo( final ListMemoizerHelper other )
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

