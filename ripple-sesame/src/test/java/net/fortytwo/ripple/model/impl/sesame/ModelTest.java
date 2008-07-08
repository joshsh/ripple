/*
 * $URL$
 * $Revision$
 * $Author$
 *
 * Copyright (C) 2007-2008 Joshua Shinavier
 */


package net.fortytwo.ripple.model.impl.sesame;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import net.fortytwo.ripple.model.impl.sesame.SesameNumericValue;
import net.fortytwo.ripple.model.NumericValue;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.Model;
import net.fortytwo.ripple.test.RippleTestCase;
import net.fortytwo.ripple.util.FileUtils;

public class ModelTest extends RippleTestCase
{
	private class ModelConstructorTest extends TestRunnable
	{
		public void test()
			throws Exception
		{
			getTestModel();
		}
	}

	public void runTests()
		throws Exception
	{
		testAsynchronous( new ModelConstructorTest() );
	}
}

