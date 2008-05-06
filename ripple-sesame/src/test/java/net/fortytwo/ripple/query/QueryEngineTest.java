/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import net.fortytwo.ripple.model.Lexicon;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.model.impl.sesame.SesameModel;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.URIMap;

import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.memory.MemoryStore;

public class QueryEngineTest extends RippleTestCase
{
	private class InitializeLexiconTest extends TestRunnable
	{
		public void test()
			throws Exception
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
			sail.shutDown();
System.out.println("--- l");
		}
	}
	
	public void runTests()
		throws Exception
	{
		//testAsynchronous( new InitializeLexiconTest() );
	}
}

