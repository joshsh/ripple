/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.test.NewRippleTestCase;
import org.openrdf.model.Value;

public class LibraryTest extends NewRippleTestCase
{
    public void testPrimitiveAlias() throws Exception
    {
        Value dup05 = modelConnection.createURI( "http://fortytwo.net/2007/05/ripple/stack#dup" );
        Value dup08 = modelConnection.createURI( "http://fortytwo.net/2007/08/ripple/stack#dup" );

        RippleValue dup05Val = modelConnection.value( dup05 );
        RippleValue dup08Val = modelConnection.value( dup08 );

        assertNotNull( dup05Val );
        assertNotNull( dup08Val );
        assertTrue( dup05Val instanceof PrimitiveStackMapping );
        assertTrue( dup08Val instanceof PrimitiveStackMapping );

        assertEquals( dup05Val, dup08Val );
    }
}
