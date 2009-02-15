/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.linkeddata.sail;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.ripple.RippleException;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.evaluation.TripleSource;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

public class SailConnectionTripleSource implements TripleSource
{
    private final SailConnection sailConnection;
    private final ValueFactory valueFactory;
	private final boolean includeInferred;

	public SailConnectionTripleSource( final SailConnection conn, final ValueFactory valueFactory, final boolean includeInferred )
	{
        sailConnection = conn;
        this.valueFactory = valueFactory;
		this.includeInferred = includeInferred;
	}

    public CloseableIteration<? extends Statement, QueryEvaluationException> getStatements(
    		final Resource subj, final URI pred, final Value obj, final Resource... contexts ) {
        try
        {
            return new QueryEvaluationIteration(
					sailConnection.getStatements( subj, pred, obj, includeInferred, contexts ) );
        }
        
        catch ( SailException e )
        {
        	new RippleException( e ).logError();
            return new EmptyCloseableIteration<Statement, QueryEvaluationException>();
        }
    }

    public ValueFactory getValueFactory()
    {
        return valueFactory;
    }
}
