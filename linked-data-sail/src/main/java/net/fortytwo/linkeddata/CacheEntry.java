package net.fortytwo.linkeddata;

import net.fortytwo.ripple.RippleException;
import org.restlet.data.MediaType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CacheEntry {
    public static enum Status {
        Undetermined,       // to be used only when a memo is created
        Success,            // normal outcome
        Timeout,            // network timeout
        InvalidUri,         // bad URI
        ParseError,         // a document was received, but failed to parse
        ClientError,        // 4xx HTTP error
        ServerError,        // 5xx HTTP error
        BadUriScheme,       // no suitable URI dereferencer was found
        BadMediaType,       // no suitable rdfizer was found
        DereferencerError,  // TODO: break this down into more specific conditions
        RdfizerError,       // TODO: break this down into more specific conditions
        Ignored,            // don't bother dereferencing these URIs
        Failure             // all other error conditions
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
    private MediaType mediaType;
    private String dereferencer;
    private String rdfizer;

    public CacheEntry() {
        this(Status.Undetermined);
    }

    public CacheEntry(final Status status) {
        this.status = status;
        this.timestamp = new Date();
    }

    /**
     * Constructs a cache entry from a set of key/value pairs
     *
     * @param keyValues the key/value pairs defining the entry
     * @throws RippleException if a parse error occurs
     */
    // TODO: parse error handling
    public CacheEntry(final String keyValues) throws RippleException {
        String[] props = keyValues.split(",");
        for (String prop : props) {
            int eq = prop.indexOf('=');
            String key = prop.substring(0, eq);
            String value = prop.substring(eq + 1);

            try {
                addEntry(key, value);
            } catch (ParseException e) {
                throw new RippleException(e);
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

    public MediaType getMediaType() {
        return mediaType;
    }

    public Map<String, String> getKeyValues() {
        Map<String, String> map = new HashMap<String, String>();

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

    public void setMediaType(final MediaType mt) {
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
        if (key.equals(STATUS)) {
            this.status = Status.valueOf(value);
        } else if (key.equals(TIMESTAMP)) {
            this.timestamp = TIMESTAMP_FORMAT.parse(value);
        } else if (key.equals(MEDIATYPE)) {
            this.mediaType = new MediaType(value);
        } else if (key.equals(DEREFERENCER)) {
            this.dereferencer = value;
        } else if (key.equals(RDFIZER)) {
            this.rdfizer = value;
        }
    }
}
