/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2010 Joshua Shinavier
 */


package net.fortytwo.ripple.cli;

/**
*  A trivial exception for breaking out of the ANTLR-generated parser, which
*  does not match end-of-input.
*/
public class ParserQuitException extends RuntimeException
{
	private static final long serialVersionUID = -9033960706498220927L;
}

