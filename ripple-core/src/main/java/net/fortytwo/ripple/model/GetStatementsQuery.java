/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model;

import net.fortytwo.ripple.flow.Sink;

import org.openrdf.model.Value;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Statement;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import net.fortytwo.ripple.flow.Buffer;
import net.fortytwo.ripple.RippleException;
import info.aduna.iteration.CloseableIteration;

/**
 * Author: josh
 * Date: Feb 7, 2008
 * Time: 10:46:12 AM
 */
public class GetStatementsQuery
{
	public enum Type { SP_O, PO_S, SO_P };

	public Resource subject;
	public URI predicate;
	public Value object;
	public Resource[] contexts;
    public Type type = Type.SP_O;

    public boolean includeInferred = false;

    /*public void setSubject( final Resource subject )
    {
        this.subject = subject;
    }

    public void setPredicate */

    public void getStatements( final SailConnection sc, final Sink<Statement, RippleException> results ) throws RippleException
	{
		// Note: we must collect results in a buffer before putting anything
		//       into the sink, as inefficient as that is, because otherwise
		//       we might end up opening another RepositoryResult before
		//       the one below closes, which currently causes Sesame to
		//       deadlock.  Even using a separate RepositoryConnection for
		//       each RepositoryResult doesn't seem to help.
		Buffer<Statement, RippleException> buffer = new Buffer<Statement, RippleException>( results );
		CloseableIteration<? extends Statement, SailException> stmtIter = null;

//TODO: use CloseableIterationSource

		// Perform the query and collect results.
		try
		{
			stmtIter = ( null == contexts )
					? sc.getStatements( subject, predicate, object, includeInferred )
					: sc.getStatements( subject, predicate, object, includeInferred, contexts );
//stmtIter.enableDuplicateFilter();

			while ( stmtIter.hasNext() )
			{
				buffer.put( stmtIter.next() );
			}

			stmtIter.close();
		}

		catch ( SailException e )
		{
			if ( null != stmtIter )
			{
				try
				{
					stmtIter.close();
				}

				catch ( Throwable t )
				{
					t.printStackTrace( System.err );
					System.exit( 1 );
				}
			}

			throw new RippleException( e );
		}

		buffer.flush();
	}

	public void getValues( final SailConnection sc, final Sink<Value, RippleException> results ) throws RippleException
	{
		Sink<Statement, RippleException> stSink = new Sink<Statement, RippleException>()
		{
			public void put( final Statement st ) throws RippleException
			{
                Value result;

                switch ( type )
                {
                    case SP_O:
                        result = st.getObject();
                        break;
                    case PO_S:
                        result = st.getSubject();
                        break;
                    case SO_P:
                        result = st.getPredicate();
                        break;
                    default:
                        throw new RippleException( "unhandled query type: " + type );
                }

                results.put( result );
			}
		};

        getStatements( sc, stSink );
    }
}
