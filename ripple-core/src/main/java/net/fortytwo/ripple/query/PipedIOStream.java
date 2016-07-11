package net.fortytwo.ripple.query;

import java.io.IOException;
import java.io.InputStream;

/**
 * A buffer to which bytes are written by any number of threads, and from which
 * bytes are read, in FIFO order, by a single thread.  The reader thread may be
 * the same as any of the writer threads.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class PipedIOStream extends InputStream
{
    private static final int BUFFER_EXPANSION = 2;
    private static final int DEFAULT_INITIAL_SIZE = 200;

    private final Object mutex = "";

    private int pos, length, size;
    private int[] data;

    public PipedIOStream() {
        size = DEFAULT_INITIAL_SIZE;
        data = new int[size];
        pos = 0;
        length = 0;
    }

    @Override
    public synchronized void close() throws IOException {
        data = null;

    }

    @Override
    public synchronized int available() {
        return length;
    }

    @Override
    public int read() throws IOException {
        // TODO: race condition
        if (0 == length) {
            synchronized (mutex) {
                try {
                    mutex.wait();
                } catch (InterruptedException e) {
                    throw new IOException(e.toString());
                }
            }
        }

        synchronized (this) {
            if (null == data) {
                throw new IOException("can't query: stream has been closed");
            }

            int c = data[pos];
            pos = (1 + pos) % size;
            length--;
            return c;
        }
    }

    private synchronized void write(int b) throws IOException {
        if (null == data) {
            throw new IOException("can't write: pipe has been closed");
        }

        // Expand the buffer if needed.
        if (length + 1 > size) {
            int newSize = size * BUFFER_EXPANSION;
            int[] newData = new int[newSize];
            for (int i = 0; i < length; i++) {
                newData[i] = data[(pos + i) % size];
            }

            pos = 0;
            data = newData;
            size = newSize;
        }

        data[(pos + length) % size] = b;
        length++;

        synchronized (mutex) {
            mutex.notify();
        }
    }

    public void write(final byte[] b) throws IOException {
        for (byte aB : b) {
            write(aB);
        }
    }

    public void write(final byte[] b, final int beginIndex, final int length) throws IOException {
        for (int i = beginIndex; i < length && i < b.length; i++) {
            write(b[i]);
        }
    }

    @Override
    public long skip(final long n) throws IOException {
        long i;
        for (i = 0; i < n; i++) {
            int c = read();
            if (-1 == c) {
                break;
            }
        }

        return i;
    }
}
