/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.test.RippleTestCase;
import org.openrdf.model.Value;

public class LibraryTest extends RippleTestCase
{
    public void testPrimitiveAlias() throws Exception
    {
        ModelConnection mc = this.modelConnection;

        Value dup05 = createURI( "http://fortytwo.net/2007/05/ripple/stack#dup", mc );
        Value dup08 = createURI( "http://fortytwo.net/2007/08/ripple/stack#dup", mc );

        RippleValue dup05Val = mc.canonicalValue( dup05 );
        RippleValue dup08Val = mc.canonicalValue( dup08 );

        assertNotNull( dup05Val );
        assertNotNull( dup08Val );
        assertTrue( dup05Val instanceof PrimitiveStackMapping );
        assertTrue( dup08Val instanceof PrimitiveStackMapping );

        assertEquals( dup05Val, dup08Val );
    }
}
