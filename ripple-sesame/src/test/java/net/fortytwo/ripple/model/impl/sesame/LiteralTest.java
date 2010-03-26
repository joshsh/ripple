/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-sesame/src/test/java/net/fortytwo/ripple/model/impl/sesame/NumericLiteralTest.java $
 * $Revision: 73 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.util.FileUtils;

import java.math.BigDecimal;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

public class LiteralTest extends RippleTestCase
{
    public void testEscapedCharacters() throws Exception
    {
        assertReducesTo( "\"\\\"\" length >>", "1" );
        assertReducesTo( "\"\\\"\"^^xsd:string length >>", "1" );
    }
}
