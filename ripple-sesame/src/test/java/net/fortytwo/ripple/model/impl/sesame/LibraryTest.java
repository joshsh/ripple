/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2011 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.PrimitiveStackMapping;
import net.fortytwo.ripple.model.RippleValue;
import net.fortytwo.ripple.test.RippleTestCase;

public class LibraryTest extends RippleTestCase
{
    public void testPrimitiveAlias() throws Exception
    {
        ModelConnection mc = this.modelConnection;

        String dup05 = "http://fortytwo.net/2007/05/ripple/stack#dup";
        String dup08 = "http://fortytwo.net/2007/08/ripple/stack#dup";

        RippleValue dup05Val = mc.canonicalValue( mc.uriValue(dup05) );
        RippleValue dup08Val = mc.canonicalValue( mc.uriValue(dup08) );

        assertNotNull( dup05Val );
        assertNotNull( dup08Val );
        assertTrue( dup05Val instanceof PrimitiveStackMapping );
        assertTrue( dup08Val instanceof PrimitiveStackMapping );

        assertEquals( dup05Val, dup08Val );
    }

    public void testAliasInExpression() throws Exception {
        assertReducesTo("<http://fortytwo.net/2007/05/ripple/stack#dup>", "<http://fortytwo.net/2007/08/ripple/stack#dup>");
        assertReducesTo("2 <http://fortytwo.net/2007/05/ripple/stack#dup>.", "2 2");
        assertReducesTo("2 <http://fortytwo.net/2007/08/ripple/stack#dup>.", "2 2");
    }

    public void testAliasesAsKeywords() throws Exception {
        assertReducesTo("dup", "<http://fortytwo.net/2007/05/ripple/stack#dup>");
        assertReducesTo("2 dup.", "2 <http://fortytwo.net/2007/05/ripple/stack#dup>.");

        assertReducesTo("xsd:type", "type");
        assertReducesTo("xsd:type", "<http://www.w3.org/2001/XMLSchema#type>");
        assertReducesTo("42 xsd:type.", "42 type.");
        assertReducesTo("42 type.", "42 <http://www.w3.org/2001/XMLSchema#type>.");
    }
}
