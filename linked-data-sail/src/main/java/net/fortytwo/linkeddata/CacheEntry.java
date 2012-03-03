package net.fortytwo.linkeddata;

import net.fortytwo.ripple.RippleException;
import org.openrdf.model.URI;
import org.restlet.data.MediaType;
import org.openrdf.model.vocabulary.XMLSchema;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class CacheEntry
{
	public static enum Status
	{
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
            STATUS = "status",
            TIMESTAMP = "timestamp",
            MEDIATYPE = "mediaType",
            DEREFERENCER = "dereferencer",
            RDFIZER = "rdfizer";

	// Use XMLSchema-style time stamps, without time zone info, accurate to
	// the nearest second.
	private static final SimpleDateFormat TIMESTAMP_FORMAT
            = new SimpleDateFormat( "yyyy'-'MM'-'dd'T'HH':'mm':'ss" );

	private Status status;
	private Date timestamp;
    private MediaType mediaType;
    private String dereferencer;
    private String rdfizer;

	public CacheEntry()
	{
		this( Status.Undetermined );
	}

	public CacheEntry(final Status status)
	{
		this.status = status;
		this.timestamp = new Date();
	}

    /**
     * Constructor which uses pre-parsed key-value pairs.
     * @param entries
     * @throws RippleException
     */
    public CacheEntry(final Collection<ContextProperty> entries) throws RippleException
    {
        for ( ContextProperty entry : entries )
        {
            addEntry( entry );
        }
    }

    /**
     * Constructor which parses the compact memo format.
     * @param s
     * @throws RippleException
     */
    // TODO: parse error handling
	public CacheEntry(final String s) throws RippleException
	{
        String[] props = s.split( ";" );
		for ( int i = 0; i < props.length; i++ )
		{
			String prop = props[i];
			int eq = prop.indexOf( '=' );
			String name = prop.substring( 0, eq ).trim();
			String value = prop.substring( eq + 1 ).trim();

            ContextProperty entry = new ContextProperty();
            entry.key = name;
            entry.value = value;
            // Note: valueDatatype is absent, but it's not needed

            addEntry( entry );
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

    public Collection<ContextProperty> getEntries()
    {
        Collection<ContextProperty> entries = new LinkedList<ContextProperty>();
        ContextProperty entry;

        if ( null != this.status )
        {
            entry = new ContextProperty();
            entry.key = STATUS;
            entry.value = this.status.toString();
            entries.add( entry );
        }

        if ( null != this.timestamp )
		{
            entry = new ContextProperty();
            entry.key = TIMESTAMP;
            entry.value = TIMESTAMP_FORMAT.format( this.timestamp );
            entry.valueDatatype = XMLSchema.DATETIME;
            entries.add( entry );
		}

		if ( null != this.mediaType )
		{
            entry = new ContextProperty();
            entry.key = MEDIATYPE;
            entry.value = this.mediaType.toString();
            entries.add( entry );
		}

		if ( null != this.dereferencer)
		{
            entry = new ContextProperty();
            entry.key = DEREFERENCER;
            entry.value = this.dereferencer.toString();
            entries.add( entry );
		}

		if ( null != this.rdfizer)
		{
            entry = new ContextProperty();
            entry.key = RDFIZER;
            entry.value = this.rdfizer.toString();
            entries.add( entry );
		}

        return entries;
    }

    /**
     * @return the compact, parseable string representation of this memo
     */
    public String toString()
	{
		StringBuffer sb = new StringBuffer();

        boolean first = true;
        for ( ContextProperty entry : getEntries() )
        {
            if ( first )
            {
                first = false;
            }

            else
            {
                sb.append( "; " );
            }

            sb.append( entry.key ).append( "=" ).append( entry.value );
        }

		return sb.toString();
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus( final Status status )
	{
		this.status = status;
	}

	public void setMediaType( final MediaType mt )
	{
		this.mediaType = mt;
	}

	public void setDereferencer(final String dref)
	{
		this.dereferencer = dref;
	}

	public void setRdfizer(final String rfiz)
	{
		this.rdfizer = rfiz;
	}

    private void addEntry( final ContextProperty entry ) throws RippleException
    {
        if ( entry.key.equals( STATUS ) )
        {
            this.status = Status.valueOf( entry.value );
        }

        else if ( entry.key.equals( TIMESTAMP ) )
        {
            try {
                this.timestamp = TIMESTAMP_FORMAT.parse( entry.value );
            } catch ( ParseException e ) {
                throw new RippleException( e );
            }
        }

        else if ( entry.key.equals( MEDIATYPE ) )
        {
            // TODO: is it reasonable to *create* a new media type here?
            this.mediaType = new MediaType( entry.value );
        }

        else if ( entry.key.equals(DEREFERENCER) )
        {
            this.dereferencer = entry.value;
        }

        else if ( entry.key.equals( RDFIZER ) )
        {
            this.rdfizer = entry.value;
        }
    }

    /**
     * A key-value pair representing a property of a named graph context
     *
     * @author Joshua Shinavier (http://fortytwo.net)
     */
    public static class ContextProperty {
        public String key;
        public String value;  // must not break parseability of the "compact" memo string
        public URI valueDatatype;  // may be null
    }
}
