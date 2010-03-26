/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.query;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Author: josh
 * Date: Jan 23, 2008
 * Time: 4:53:10 PM
 */
public class NullOutputStream extends OutputStream
{
	public void write( int i ) throws IOException
	{
		// Do nothing.
	}
}
