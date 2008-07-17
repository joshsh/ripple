/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.io;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.query.QueryEngine;
import net.fortytwo.ripple.test.NewRippleTestCase;

public class RipplePrintStreamTest extends NewRippleTestCase
{
    public void testLiterals() throws Exception
    {
        Model model = getTestModel();
        ModelConnection mc = model.getConnection( "" );
        QueryEngine qe = new QueryEngine( model, null, System.out, System.err );
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        RipplePrintStream ps = new RipplePrintStream( new PrintStream( bos ), qe.getLexicon() );

        mc.setNamespace( "xsd", "http://www.w3.org/2001/XMLSchema#", true );

        ps.print( mc.value( 42 ) );
        assertEquals( "42", bos.toString() );
        bos.reset();
        ps.print( mc.value( 0 ) );
        assertEquals( "0", bos.toString() );
        bos.reset();
        ps.print( mc.value( -42 ) );
        assertEquals( "-42", bos.toString() );
        bos.reset();

        ps.print( mc.value( 42.0 ) );
        assertEquals( "42.0E0", bos.toString() );
        bos.reset();
        ps.print( mc.value( 0.0 ) );
        assertEquals( "0.0E0", bos.toString() );
        bos.reset();
        ps.print( mc.value( -42.0 ) );
        assertEquals( "-42.0E0", bos.toString() );
        bos.reset();

        ps.print( mc.value( true ) );
        assertEquals( "true", bos.toString() );
        bos.reset();
        ps.print( mc.value( false ) );
        assertEquals( "false", bos.toString() );
        bos.reset();

        //...

        ps.close();
        bos.close();
        mc.close();
    }
}
