/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2009 Joshua Shinavier
 */


package net.fortytwo.linkeddata;

import org.openrdf.model.URI;
import org.openrdf.rio.RDFHandler;

import java.io.InputStream;

import net.fortytwo.linkeddata.ContextMemo;

/**
 * Author: josh
 * Date: Jan 16, 2008
 * Time: 12:07:22 PM
 */
public interface Rdfizer
{
	/**
	 *
	 * @param is  a stream representation of the resource to be converted
	 * @param handler  
	 * @param resourceUri
	 * @param baseUri
	 * @return
	 */
	ContextMemo.Status handle( InputStream is, RDFHandler handler, URI resourceUri, String baseUri );
}
