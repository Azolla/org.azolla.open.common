/*
 * @(#)FtpHelperTest.java		Created at 2013-7-8
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.ftp;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Joiner;

/**
 * The coder is very lazy, nothing to write for this FtpHelperTest class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class FtpHelperTest
{
	Ftp0						ftpHelper			= null;

	private static final String	REMOTE_TEST_ROOT	= "/test/";

	private static final String	TEST_FILENAME		= "ftp.test.txt";

	private static final File	TEST_FILE			= new File(System.getProperty("user.dir"), "src/test/resources/org/azolla/open/common/ftp/ftp.test.txt");

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
		ftpHelper = Ftp0.ins("42.121.28.127", "ftpuser", "ft9VK9aUW");
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
		//		fail("Not yet implemented");
	}

	@Test
	public void testStoreFile()
	{
		Assert.assertEquals(true, ftpHelper.storeFile(REMOTE_TEST_ROOT + TEST_FILENAME, TEST_FILE));
	}

	@Test
	public void testListNames()
	{
		System.out.println("testListNames=" + Joiner.on(";").join(ftpHelper.listNames()));
		System.out.println("testListNames=" + Joiner.on(";").join(ftpHelper.listNames(REMOTE_TEST_ROOT)));
		System.out.println("testListNames=" + Joiner.on(";").join(ftpHelper.listNames(REMOTE_TEST_ROOT + TEST_FILENAME)));
	}

	@Test
	public void testAllNames()
	{
		//		System.out.println("testAllNames=" + Joiner.on(";").join(ftpHelper.allNames()));
		System.out.println("testAllNames=" + Joiner.on(";").join(ftpHelper.allNames(REMOTE_TEST_ROOT)));
		System.out.println("testAllNames=" + Joiner.on(";").join(ftpHelper.allNames("/test")));
		System.out.println("testAllNames=" + Joiner.on(";").join(ftpHelper.allNames(REMOTE_TEST_ROOT + TEST_FILENAME)));
	}

	@Test
	public void testRetrieveFile()
	{
		Assert.assertEquals(true, ftpHelper.retrieveFile(REMOTE_TEST_ROOT + TEST_FILENAME, TEST_FILE.getAbsolutePath()));
	}
}
