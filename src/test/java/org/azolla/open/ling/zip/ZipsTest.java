/*
 * @(#)ZipsTest.java		Created at 2013-7-9
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.zip;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.azolla.open.ling.io.File0;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * The coder is very lazy, nothing to write for this ZipsTest class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class ZipsTest
{
	private static final File	testDir	= File0.newFile(File0.getUserDir(), "src/test/resources/org/azolla/open/common/zip/testFolder");

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
		File0.newFile(testDir, System.currentTimeMillis() + File0.TXT_FILETYPE_WITH_POINT).createNewFile();
	}

	/**
	 * The coder is very lazy, nothing to write for this tearDown method
	 * 
	 * @throws java.lang.Exception void
	 */
	@After
	public void tearDown() throws Exception
	{
		File0.delFile(testDir.getParentFile());
		testDir.mkdirs();
		File0.newFile(testDir, "empty.txt").createNewFile();
	}

	@Test
	public void testZipString()
	{
		File0.newFile(testDir, "dir").mkdirs();
		try
		{
			File0.newFile(testDir, "dir/" + System.currentTimeMillis() + File0.TXT_FILETYPE_WITH_POINT).createNewFile();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		assertTrue(Zip0.zip(testDir.getAbsolutePath()));

		assertTrue(Zip0.unzip(File0.newFile(testDir.getParentFile(), testDir.getName() + File0.ZIP_FILETYPE_WITH_POINT), testDir.getAbsolutePath()));
		System.out.println("END-org.azolla.open.common.zip.ZipsTest.testZipString()");
	}

	@Test
	public void testZipList()
	{
		File0.newFile(testDir, "dir").mkdirs();
		try
		{
			File0.newFile(testDir, "dir/" + System.currentTimeMillis() + File0.TXT_FILETYPE_WITH_POINT).createNewFile();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		assertTrue(Zip0.zip(Lists.newArrayList(testDir), testDir.getAbsolutePath()));

		assertTrue(Zip0.unzip(File0.newFile(testDir.getParentFile(), testDir.getName() + File0.ZIP_FILETYPE_WITH_POINT), testDir.getAbsolutePath()));
		System.out.println("END-org.azolla.open.common.zip.ZipsTest.testZipList()");
	}
}
