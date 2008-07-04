/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.libs.etc;

import java.util.Date;

import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.libs.logic.LogicLibrary;
import net.fortytwo.ripple.libs.stack.StackLibrary;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.StackContext;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.model.RippleValue;

/**
 * A primitive which produces the current time, in seconds since midnight UTC of
 * January 1, 1970.
 */
public class DateTimeToMillis extends PrimitiveStackMapping
{
	private static final int ARITY = 1;

    private static final String[] IDENTIFIERS = {
            // FIXME: this is a kludge for programs created by Ripple 0.5-dev.  Remove this alias when it is no longer needed
            EtcLibrary.NS_2007_08 + "dateTimeToMillis",

            EtcLibrary.NS_2008_06 + "dateTimeToMillis"};

    public String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public DateTimeToMillis() throws RippleException
	{
		super();
	}

	public int arity()
	{
		return ARITY;
	}

	public void applyTo( final StackContext arg,
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
