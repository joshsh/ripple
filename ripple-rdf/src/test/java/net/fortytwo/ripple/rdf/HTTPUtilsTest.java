/*
 * $URL: https://ripple.googlecode.com/svn/trunk/ripple-rdf/src/test/java/net/fortytwo/ripple/rdf/HTTPUtilsTest.java $
 * $Revision: 136 $
 * $Author: parcour $
 *
 * Copyright (C) 2007-1012 Joshua Shinavier
 */


package net.fortytwo.ripple.rdf;

import java.net.URI;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Author: josh
 * Date: Jan 16, 2008
 * Time: 4:08:58 PM
 */
public class HTTPUtilsTest extends TestCase
{
    /**
     * Make sure that URI schemes are what we expect them to be
     */
    public void testURISchemes() throws Exception
    {
        URI uri;

        uri = new URI( "http://example.com/foo" );
        Assert.assertEquals( "http", uri.getScheme() );
        uri = new URI( "http://myopenlink.net/dataspace/person/kidehen#this" );
        Assert.assertEquals( "http", uri.getScheme() );
        
        uri = new URI( "jar:file:/tmp/foo.txt" );
        Assert.assertEquals( "jar", uri.getScheme() );
    }
}
