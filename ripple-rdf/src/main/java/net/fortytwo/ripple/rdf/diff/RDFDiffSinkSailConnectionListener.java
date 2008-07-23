package net.fortytwo.ripple.rdf.diff;

import org.openrdf.sail.SailConnectionListener;
import org.openrdf.model.Statement;
import net.fortytwo.ripple.rdf.RDFSink;
import net.fortytwo.ripple.RippleException;

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
            new RippleException( e ).logError();
        }
    }

    public void statementRemoved(Statement statement)
    {
        try {
            sink.subtractorSink().statementSink().put( statement );
        } catch (Exception e) {
            new RippleException( e ).logError();
        }
    }
}
