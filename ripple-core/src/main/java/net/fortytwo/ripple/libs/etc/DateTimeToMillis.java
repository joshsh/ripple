/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.etc;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.StackContext;

import java.util.Date;

/**
 * A primitive which produces the current time, in seconds since midnight UTC of
 * January 1, 1970.
 */
/*
etc:dateTimeToMillis
	a rpl:Function ;
	rpl:arity 1 ;
	rpl:parameters (
		[
			rdfs:label "dateTime" ;
			rpl:parameterType xsd:dateTime ;
			rpl:isTransparent true ]
		) ;
	rdfs:label "dateTimeToMillis" ;
	rpl:returnType xsd:long ;
	rdfs:comment "dateTime -> [milliseconds since the Unix epoch]" ;
	.
 */
public class DateTimeToMillis extends PrimitiveStackMapping
{
    private static final String[] IDENTIFIERS = {
            EtcLibrary.NS_2008_08 + "dateTimeToMillis"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public Parameter[] getParameters()
    {
        return new Parameter[] {
                new Parameter( "dateTime", null, true )};
    }

    public String getComment()
    {
        return "dateTime -> [milliseconds since the Unix epoch]";
    }

    public DateTimeToMillis() throws RippleException
	{
		super();
	}

	public void apply( final StackContext arg,
						 final Sink<StackContext, RippleException> solutions	)
		    throws RippleException
	{
		final ModelConnection mc = arg.getModelConnection();
		RippleList stack = arg.getStack();
        Date d = mc.toDateValue( stack.getFirst() );
        stack = stack.getRest();
        
        solutions.put( arg.with( stack.push(
			mc.value( d.getTime() ) ) ) );
	}
}
