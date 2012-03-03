package net.fortytwo.linkeddata;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.ripple.RippleException;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A collection of caching metadata for aggregated Linked Data.
 * A complete set of metadata is maintained in the underlying triple store,
 * while a subset of the data is kept in a fast in-memory cache.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CachingMetadata {
    private final InMemoryCache memos;

    private final ValueFactory valueFactory;

    // A special context memo to indicate an already-known persistence miss.
    private final CacheEntry miss;

    public CachingMetadata(final int capacity,
                           final ValueFactory valueFactory) throws RippleException {
        memos = new InMemoryCache(capacity);

        this.valueFactory = valueFactory;

        miss = new CacheEntry();
    }

    public CacheEntry getMemo(final String graphUri,
                              final SailConnection sc) throws RippleException {
        CacheEntry memo = memos.get(graphUri);

        // If the memo is not cached
        if (null == memo) {
            // Attempt to retrieve the memo from the persistent store.
            try {
                memo = retrieveMemo(graphUri, sc);
            } catch (SailException e) {
                throw new RippleException(e);
            }

            if (null == memo) {
                // There is no such memo.  Cache the "miss" value to avoid future retrieval attempts.
                memos.put(graphUri, miss);
            } else {
                // The memo exists.  Cache it.
                memos.put(graphUri, memo);
            }
        }

        return memo;
    }

    // Writes caching metadata to the base Sail.
    // Note: for now, this metadata resides in the null context.
    public void setMemo(final String graphUri,
                        final CacheEntry memo,
                        final SailConnection sc) throws RippleException {
        try {
            URI s = valueFactory.createURI(graphUri);

            Literal memoLit = valueFactory.createLiteral(memo.toString());
            sc.removeStatements(s, LinkedDataCache.CACHE_MEMO, null, LinkedDataCache.CACHE_GRAPH);
            sc.addStatement(s, LinkedDataCache.CACHE_MEMO, memoLit, LinkedDataCache.CACHE_GRAPH);

            memos.put(graphUri, memo);
        } catch (SailException e) {
            throw new RippleException(e);
        }
    }

    // Restores dereferencer state by reading success and failure memos from
    // the last session (if present).
    private CacheEntry retrieveMemo(final String graphUri,
                                    final SailConnection sc) throws SailException, RippleException {
        CloseableIteration<? extends Statement, SailException> iter;

        iter = sc.getStatements(
                valueFactory.createURI(graphUri),
                LinkedDataCache.CACHE_MEMO,
                null,
                false,
                LinkedDataCache.CACHE_GRAPH);

        try {
            if (!iter.hasNext()) {
                return null;
            } else {
                // Note: if there are multiple statements (which there shouldn't be), make an arbitrary choice.
                Literal obj = (Literal) iter.next().getObject();

                return new CacheEntry(obj.getLabel());
            }
        } finally {
            iter.close();
        }
    }

    // A fast in-memory cache with an LRU replacement policy
    private class InMemoryCache extends LinkedHashMap<String, CacheEntry> {
        private final int maxCapacity;

        // Creates an access-order linked hashmap with the given maximum capacity.
        // Default values are used for initial capacity and load factor.
        public InMemoryCache(final int maxCapacity) {
            super(16, 0.75f, true);

            this.maxCapacity = maxCapacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > maxCapacity;
        }
    }
}
