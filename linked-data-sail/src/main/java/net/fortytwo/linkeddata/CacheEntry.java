package net.fortytwo.linkeddata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CacheEntry {
    public enum Status {
        BadMediaType,       // no suitable rdfizer was found
        BadUriScheme,       // no suitable URI dereferencer was found
        CacheLookup,           // used when a memo for a newly-encountered URI is first created
        ClientError,        // 4xx HTTP error
        DereferencerError,  // TODO: break this down into more specific conditions
        Failure,            // all other error conditions
        Ignored,            // don't bother dereferencing these URIs
        InvalidUri,         // bad URI
        ParseError,         // a document was received, but failed to parse
        RdfizerError,       // TODO: break this down into more specific conditions
        RedirectsToCached,  // a URI 3xx-redirects to a document to which another URI previously redirected
        ServerError,        // 5xx HTTP error
        Success,            // normal outcome
        Timeout,            // network timeout
        Undetermined,       // used for lookup operations in progress
    }

    // memo entry keys
    private static final String
            DEREFERENCER = "dereferencer",
            MEDIATYPE = "mediaType",
            RDFIZER = "rdfizer",
            STATUS = "status",
            TIMESTAMP = "timestamp";

    // Use XMLSchema-style time stamps, without time zone info, accurate to
    // the nearest second.
    private static final SimpleDateFormat TIMESTAMP_FORMAT
            = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss");

    private Status status;
    private Date timestamp;
    private String mediaType;
    private String dereferencer;
    private String rdfizer;

    public CacheEntry(final Status status) {
        this.status = status;
        this.timestamp = new Date();
    }

    /**
     * Constructs a cache entry from a set of key/value pairs
     *
     * @param keyValues the key/value pairs defining the entry
     */
    // TODO: parse error handling
    public CacheEntry(final String keyValues) {
        String[] props = keyValues.split(",");
        for (String prop : props) {
            int eq = prop.indexOf('=');
            String key = prop.substring(0, eq);
            String value = prop.substring(eq + 1);

            try {
                addEntry(key, value);
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public String getDereferencer() {
        return dereferencer;
    }

    public String getRdfizer() {
        return rdfizer;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMediaType() {
        return mediaType;
    }

    private Map<String, String> getKeyValues() {
        Map<String, String> map = new HashMap<>();

        if (null != this.status) {
            map.put(STATUS,
                    this.status.toString());
        }

        if (null != this.timestamp) {
            map.put(TIMESTAMP,
                    TIMESTAMP_FORMAT.format(this.timestamp));
        }

        if (null != this.mediaType) {
            map.put(MEDIATYPE,
                    this.mediaType.toString());
        }

        if (null != this.dereferencer) {
            map.put(DEREFERENCER,
                    this.dereferencer);
        }

        if (null != this.rdfizer) {
            map.put(RDFIZER,
                    this.rdfizer);
        }

        return map;
    }

    /**
     * @return the compact, parseable string representation of this memo
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (Map.Entry<String, String> entry : getKeyValues().entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }

            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return sb.toString();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public void setMediaType(final
                             String mt) {
        this.mediaType = mt;
    }

    public void setDereferencer(final String dref) {
        this.dereferencer = dref;
    }

    public void setRdfizer(final String rfiz) {
        this.rdfizer = rfiz;
    }

    private void addEntry(final String key,
                          final String value) throws ParseException {
        switch (key) {
            case STATUS:
                this.status = Status.valueOf(value);
                break;
            case TIMESTAMP:
                this.timestamp = TIMESTAMP_FORMAT.parse(value);
                break;
            case MEDIATYPE:
                this.mediaType = value;
                break;
            case DEREFERENCER:
                this.dereferencer = value;
                break;
            case RDFIZER:
                this.rdfizer = value;
                break;
        }
    }
}
