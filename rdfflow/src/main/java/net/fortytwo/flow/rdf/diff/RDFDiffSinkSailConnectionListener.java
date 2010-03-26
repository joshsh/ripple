package net.fortytwo.flow.rdf.diff;

import org.openrdf.sail.SailConnectionListener;
import org.openrdf.model.Statement;

/**
 * Author: josh
 * Date: Jul 23, 2008
 * Time: 12:52:14 PM
 */
public class RDFDiffSinkSailConnectionListener<E extends Exception> implements SailConnectionListener
{
    private final RDFDiffSink<E> sink;

    public RDFDiffSinkSailConnectionListener( final RDFDiffSink sink )
    {
        this.sink = sink;
    }
    
    public void statementAdded( final Statement statement )
    {
        try {
            sink.adderSink().statementSink().put( statement );
        } catch (Exception e) {
            logError( e );
        }
    }

    public void statementRemoved( final Statement statement )
    {
        try {
            sink.subtractorSink().statementSink().put( statement );
        } catch ( Exception e ) {
            logError( e );
        }
    }

    private void logError( final Exception e )
    {
        // FIXME
        System.err.println( "Error: " + e );
    }
}
