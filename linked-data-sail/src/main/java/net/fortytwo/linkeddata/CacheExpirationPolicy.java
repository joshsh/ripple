package net.fortytwo.linkeddata;

/**
 * A rule which determines whether the caching metadata for a previously dereferenced URI is expired.
 * If it is expired, the Linked Data cache will issue a new request in order to retrieve fresh data.
 *
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public interface CacheExpirationPolicy {
    boolean isExpired(String uri,
                      CacheEntry entry);
}
