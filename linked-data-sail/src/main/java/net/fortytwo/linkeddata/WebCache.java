package net.fortytwo.linkeddata;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.linkeddata.sail.LinkedDataSail;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.rdf.RDFUtils;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * User: josh
 * Date: Feb 17, 2010
 * Time: 7:04:08 PM
 */
public class WebCache {
    private final MemoMap memos;

    private final boolean compact, useBNodes;
    private final ValueFactory valueFactory;
    private final URI cacheContext, cacheMemo, fullMemo;

    // A special context memo to indicate an already-known persistence miss.
    private final ContextMemo miss;

    public WebCache(final int capacity,
                    final ValueFactory valueFactory) throws RippleException {
        memos = new MemoMap(capacity);

        this.valueFactory = valueFactory;

        compact = Ripple.getProperties().getBoolean(LinkedDataSail.USE_COMPACT_MEMO_FORMAT);
        useBNodes = Ripple.getProperties().getBoolean(Ripple.USE_BLANK_NODES);

        cacheContext = valueFactory.createURI(WebClosure.CACHE_CONTEXT);
        cacheMemo = valueFactory.createURI(WebClosure.CACHE_MEMO);
        fullMemo = valueFactory.createURI(WebClosure.FULL_MEMO);

        miss = new ContextMemo();
    }

    public ContextMemo getMemo(final String uri,
                               final SailConnection sc) throws RippleException {
        ContextMemo memo = memos.get(uri);

        // If the memo is not cached
        if (null == memo) {
            // Attempt to retrieve the memo from the persistent store.
            try {
                memo = retrieveMemo(uri, sc);
            } catch (SailException e) {
                throw new RippleException(e);
            }

            if (null == memo) {
                // There is no such memo.  Cache the "miss" value to avoid future retrieval attempts.
                memos.put(uri, miss);
            } else {
                // The memo exists.  Cache it.
                memos.put(uri, memo);
            }
        }

        return memo;
    }

    /* TODO
     *                 // TODO: move this so that it's a default which can be overridden
                sc.setNamespace("cache", WebClosure.CACHE_NS);

                // Clear any existing cache metadata (in any named analysis).
                sc.removeStatements(null, null, null, cacheContext);
                sc.commit();
     */
    public void setMemo(final String uri,
                        final ContextMemo memo,
                        final SailConnection sc) throws RippleException {
        try {
            // Unset the memo.
            if (null == memo) {
                unpersistMemo(uri, sc);
                sc.commit();
                memos.remove(uri);
            }

            // Set the memo.
            else {
                persistMemo(uri, memo, sc);
                sc.commit();
                memos.put(uri, memo);
            }
        } catch (SailException e) {
            throw new RippleException(e);
        }
    }

    /**
     * Writes cache metadata to the base Sail.
     * Note: for now, this metadata resides in the null context.
     */
    private void persistMemo(final String uri,
                             final ContextMemo memo,
                             final SailConnection sc) throws SailException, RippleException {
        URI contextURI = valueFactory.createURI(uri);

        // Write context memo in the compact, statement-per-context format
        if (compact) {
            Literal memoLit = valueFactory.createLiteral(memo.toString());
            sc.addStatement(contextURI, cacheMemo, memoLit, cacheContext);
        }

        // Write context memo in the expanded, statement-per-property format
        else {
            Resource memoResource = useBNodes
                    ? valueFactory.createBNode()
                    : RDFUtils.createBNodeUri(valueFactory);
            sc.addStatement(contextURI, fullMemo, memoResource, cacheContext);

            for (ContextProperty entry : memo.getEntries()) {
                URI key = valueFactory.createURI(WebClosure.CACHE_NS + entry.key);
                URI datatype = entry.valueDatatype;
                Literal value = (null == datatype)
                        ? valueFactory.createLiteral(entry.value)
                        : valueFactory.createLiteral(entry.value, datatype);
                sc.addStatement(memoResource, key, value, cacheContext);
            }
        }
    }


    /**
     * Deletes a memo from the persistent store.
     */
    private void unpersistMemo(final String uri,
                               final SailConnection sc) throws SailException, RippleException {
        URI contextURI = valueFactory.createURI(uri);

        if (compact) {
            sc.removeStatements(contextURI, null, null, cacheContext);
        } else {
            Resource memoResource;
            CloseableIteration<? extends Statement, SailException> iter
                    = sc.getStatements(null, fullMemo, null, false, cacheContext);
            try {
                if (!iter.hasNext()) {
                    throw new RippleException("memo resource not found for URI: " + uri);
                }
                memoResource = (Resource) iter.next().getObject();
            } finally {
                iter.close();
            }

            sc.removeStatements(memoResource, null, null, cacheContext);
        }
    }

    /**
     * Restores dereferencer state by reading success and failure memos from
     * the last session (if present).
     */
    private ContextMemo retrieveMemo(final String uri,
                                     final SailConnection sc) throws SailException, RippleException {
        CloseableIteration<? extends Statement, SailException> iter;
        URI contextURI = valueFactory.createURI(uri);

        // Read context memos in the compact, statement-per-context
        if (compact) {
            iter = sc.getStatements(contextURI, cacheMemo, null, false, cacheContext);

            try {
                if (!iter.hasNext()) {
                    return null;
                } else {
                    // Note: if there are multiple statements (which there shouldn't be), make an arbitrary choice.
                    Literal obj = (Literal) iter.next().getObject();

                    return new ContextMemo(obj.getLabel());
                }
            }

            finally {
                iter.close();
            }
        }

        // Read context memos in the expanded, statement-per-property format
        else {
            Resource memoResource;
            iter = sc.getStatements(contextURI, fullMemo, null, false, cacheContext);

            try {
                if (!iter.hasNext()) {
                    return null;
                } else {
                    // Note: if there are multiple statements (which there shouldn't be), make an arbitrary choice.
                    memoResource = (Resource) iter.next().getObject();
                }
            } finally {
                iter.close();
            }

            Collection<ContextProperty> entries
                    = new LinkedList<ContextProperty>();

            CloseableIteration<? extends Statement, SailException> valueIter
                    = sc.getStatements(memoResource, null, null, false, cacheContext);

            try {
                while (valueIter.hasNext()) {
                    Statement memoSt = valueIter.next();

                    URI pred = memoSt.getPredicate();
                    Literal obj = (Literal) memoSt.getObject();

                    ContextProperty entry = new ContextProperty();
                    entry.key = pred.getLocalName();
                    entry.value = obj.getLabel();
                    // Note: datatype is unimportant here; it's only for external applications
                    entries.add(entry);
                }
            } finally {
                valueIter.close();
            }

            return new ContextMemo(entries);
        }
    }

    // An LRU-caching HashMap
    private class MemoMap extends LinkedHashMap<String, ContextMemo> {
        private final int maxCapacity;

        // Creates an access-order linked hashmap with the given maximum capacity.
        // Default values are used for initial capacity and load factor.
        public MemoMap(final int maxCapacity) {
            super(16, 0.75f, true);
            
            this.maxCapacity = maxCapacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
           return size() > maxCapacity;
        }

    }
}
