/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.linkeddata.sail;

import info.aduna.iteration.CloseableIteration;
import org.openrdf.model.Statement;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.sail.SailException;

public class QueryEvaluationIteration implements CloseableIteration<Statement, QueryEvaluationException>
{
    private final CloseableIteration<? extends Statement, SailException> baseIteration;
    
    public QueryEvaluationIteration( final CloseableIteration<? extends Statement, SailException> baseIteration )
    {
        this.baseIteration = baseIteration;
    }

    public void close() throws QueryEvaluationException
    {
        try
        {
            baseIteration.close();
        }

        catch ( SailException e )
        {
            throw new QueryEvaluationException( e );
        }
    }

    public boolean hasNext() throws QueryEvaluationException
    {
        try
        {
            return baseIteration.hasNext();
        }

        catch ( SailException e ) 
        {
            throw new QueryEvaluationException( e );
        }
    }

    public Statement next() throws QueryEvaluationException
    {
        try
        {
            return baseIteration.next();
        }

        catch ( SailException e )
        {
            throw new QueryEvaluationException( e );
        }
    }

    public void remove() throws QueryEvaluationException
    {
        try
        {
            baseIteration.remove();
        }

        catch ( SailException e )
        {
            throw new QueryEvaluationException( e );
        }
    }
}
