package net.fortytwo.ripple.flow;

import java.util.Collection;

/**
 * Author: josh
 * Date: Mar 25, 2009
 * Time: 8:52:02 PM
 */
public abstract class SimpleReadOnlyCollection<T> implements Collection<T>
{
    public boolean isEmpty()
    {
        return 0 == size();
    }

    public boolean contains(final Object o)
    {
        for (T t : this)
        {
            if (o.equals(t))
            {
                return true;
            }
        }

        return false;
    }

    public T[] toArray()
    {
        T[] array = (T[]) new Object[size()];

        int i = 0;
        for (T t : this)
        {
            array[i] = t;
            i++;
        }

        return array;
    }

    // FIXME: I'm unsure whether I've implemented this superfluous method correctly.
    public <U> U[] toArray(U[] array)
    {
        int size = size();
        U[] targetArray = (size <= array.length)
                ? array
                : (U[]) new Object[size];

        int i = 0;
        for (T t : this)
        {
            targetArray[i] = (U) t;
            i++;
        }

        if (size < targetArray.length)
        {
            targetArray[size] = null;
        }

        return targetArray;
    }

    public boolean add(final T t) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(final Collection<?> objects)
    {
        for (Object o : objects) {
            if (contains(o))
            {
                return true;
            }
        }

        return false;
    }

    public boolean addAll(final Collection<? extends T> ts) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(final Collection<?> objects) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(final Collection<?> objects) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }
}
