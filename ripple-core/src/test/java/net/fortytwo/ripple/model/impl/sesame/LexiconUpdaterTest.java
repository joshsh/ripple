package net.fortytwo.ripple.model.impl.sesame;

import net.fortytwo.ripple.test.RippleTestCase;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class LexiconUpdaterTest extends RippleTestCase
{
    public void testNothing() throws Exception {

    }

    /*
    public void testRejectBadNamespaces() throws Exception
    {
        String nsBase = "http://example.org/ns";
        int i = 0;

        Lexicon lexicon = new Lexicon( getTestModel() );
        LexiconUpdater updater = new LexiconUpdater( lexicon );

        Iterator<String> prefixIter = getLines( "badNsPrefixes.txt" ).iterator();
        assertTrue( prefixIter.hasNext() );
        while ( prefixIter.hasNext() )
        {
            String prefix = prefixIter.next();
            assertNull( lexicon.getNamespaceUri(prefix) );

            i++;
            String nsUri = nsBase + i + "#";
            Namespace ns = new NamespaceImpl( prefix, nsUri );
            updater.adderSink().namespaceSink().put( ns );

            assertNull( lexicon.getNamespaceUri(prefix) );
        }
    }

    public void testAcceptGoodNamespaces() throws Exception
    {
        String nsBase = "http://example.org/ns";
        int i = 0;

        Lexicon lexicon = new Lexicon( getTestModel() );
        LexiconUpdater updater = new LexiconUpdater( lexicon );

        Iterator<String> prefixIter = getLines( "goodNsPrefixes.txt" ).iterator();
        assertTrue( prefixIter.hasNext() );
        while ( prefixIter.hasNext() )
        {
            String prefix = prefixIter.next();
            assertNull( lexicon.getNamespaceUri(prefix) );

            i++;
            String nsUri = nsBase + i + "#";
            Namespace ns = new NamespaceImpl( prefix, nsUri );
            updater.adderSink().namespaceSink().put( ns );

            assertEquals( lexicon.getNamespaceUri(prefix), nsUri );
        }
    }

	private Collection<String> getLines( final String fileName ) throws Exception
	{
		InputStream is = LexiconUpdaterTest.class.getResourceAsStream( fileName );
		Collection<String> lines = FileUtils.getLines( is );
		is.close();
		return lines;
	}   */
}

