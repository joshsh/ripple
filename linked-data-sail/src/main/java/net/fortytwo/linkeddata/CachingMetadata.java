package net.fortytwo.linkeddata;

import info.aduna.iteration.CloseableIteration;
import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
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

    public CachingMetadata(final int capacity,
                           final ValueFactory valueFactory) {
        memos = new InMemoryCache(capacity);

        this.valueFactory = valueFactory;
    }

    public void clear() {
        memos.clear();
    }

    public CacheEntry getMemo(final String graphUri,
                              final SailConnection sc) {
        CacheEntry memo = memos.get(graphUri);

        // If the memo is not cached
        if (null == memo) {
            // Attempt to retrieve the memo from the persistent store.
            return retrieveMemo(graphUri, sc);
        }

        return memo;
    }

    /**
     * Writes metadata to the in-memory cache and, optionally, to the Sail.
     * Note: for now, this metadata resides in the null context.
     *
     * @param graphUri the graph URI of the cached data source
     * @param memo     the memo object representing the state of the data source
     * @param sc       a connection to the Sail, or null if only the in-memory cache should be updated
     */
    public void setMemo(final String graphUri,
                        final CacheEntry memo,
                        final SailConnection sc) {
        memos.put(graphUri, memo);

        if (null != sc) {
            IRI s = valueFactory.createIRI(graphUri);
            Literal memoLit = valueFactory.createLiteral(memo.toString());
            sc.removeStatements(s, LinkedDataCache.CACHE_MEMO, null, LinkedDataCache.CACHE_GRAPH);
            sc.addStatement(s, LinkedDataCache.CACHE_MEMO, memoLit, LinkedDataCache.CACHE_GRAPH);
        }
    }

    // Restores dereferencer state by reading success and failure memos from
    // the last session (if present).
    private CacheEntry retrieveMemo(final String graphUri,
                                    final SailConnection sc) throws SailException {
        CloseableIteration<? extends Statement, SailException> iter;

        iter = sc.getStatements(
                valueFactory.createIRI(graphUri),
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
