/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.ripple.query;


import net.fortytwo.ripple.test.RippleTestCase;

public class QueryEngineTest extends RippleTestCase
{
    public void testNothing() throws Exception {

    }
    
    /*
        public void testInitializeLexicon() throws Exception
		{
System.out.println("--- a");
			Sail sail = new MemoryStore();
System.out.println("--- s");
			sail.initialize();
			SailConnection sc = sail.getConnection();
System.out.println("--- d");

			String ns1 = "http://example.org/ns1/";
			String ns2 = "http://example.org/ns2#";
			sc.setNamespace( "ns1", ns1 );
			sc.setNamespace( "ns2", ns2 );
System.out.println("--- f");
			URIMap uriMap = new URIMap();
			Model model = new SesameModel( sail, uriMap );
System.out.println("--- g");
			QueryEngine qe = new QueryEngine( model, new LazyEvaluator(), System.out, System.err );
System.out.println("--- g2");
			Lexicon lex = qe.getLexicon();
System.out.println("--- h");
			assertEquals( ns1, lex.resolveNamespacePrefix( "ns1" ) );
			assertEquals( ns2, lex.resolveNamespacePrefix( "ns2" ) );
			
System.out.println("--- j");
			sc.close();
System.out.println("--- k");
System.out.println("--- l");
		}
*/
}

