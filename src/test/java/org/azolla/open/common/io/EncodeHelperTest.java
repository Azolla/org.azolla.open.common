/*
 * @(#)EncodeHelperTest.java		Created at 2013-7-9
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.io;

import java.nio.charset.Charset;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The coder is very lazy, nothing to write for this EncodeHelperTest class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class EncodeHelperTest
{

	/**
	 * The coder is very lazy, nothing to write for this setUpBeforeClass method
	 * 
	 * @throws java.lang.Exception void
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	/**
	 * The coder is very lazy, nothing to write for this tearDownAfterClass method
	 * 
	 * @throws java.lang.Exception void
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	/**
	 * The coder is very lazy, nothing to write for this setUp method
	 * 
	 * @throws java.lang.Exception void
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * The coder is very lazy, nothing to write for this tearDown method
	 * 
	 * @throws java.lang.Exception void
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		Assert.assertEquals(Encoding.ASCII.getEncoding(), Encodes.SINGLETON.getFileEncoding(File0.newFile(File0.getUserDir(), "src/test/resources/org/azolla/open/common/io/empty.txt").getAbsolutePath()));
		Assert.assertEquals(Encoding.ASCII.getEncoding(), Encodes.SINGLETON.getByteEncoding(new String("Azolla").getBytes()));
		Assert.assertEquals(Encoding.ASCII.getEncoding(), Encodes.SINGLETON.getByteEncoding(new String("Azolla".getBytes(), Charset.forName("UTF-8")).getBytes(Charset.forName("UTF-8"))));
	}
}
