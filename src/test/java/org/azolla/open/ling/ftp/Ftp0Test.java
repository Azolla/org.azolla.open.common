/*
 * @(#)Ftp0Test.java		Created at 2013-9-8
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.ftp;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.net.ftp.FTPFile;
import org.azolla.open.ling.io.File0;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Joiner;

/**
 * The coder is very lazy, nothing to write for this Ftp0Test class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class Ftp0Test
{

	static Ftp0					ftp0				= null;

	private static final String	REMOTE_TEST_ROOT	= "/test/";

	private static final String	TEST_FILENAME		= "ftp.test.txt";

	private static final File	TEST_FILE			= File0.newFile(System.getProperty("user.dir"), "src/test/resources", Ftp0Test.class.getPackage().getName().replace(".", "/"), TEST_FILENAME);

	/**
	 * The coder is very lazy, nothing to write for this setUpBeforeClass method
	 * 
	 * @throws java.lang.Exception void
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		ftp0 = Ftp0.single("42.121.28.127", "ftpuser", "ft9VK9aUW");
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
		//		ftp0 = Ftp0.single("42.121.28.127", "ftpuser", "ft9VK9aUW");
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

	/**
	 * Test method for {@link org.azolla.open.ling.ftp.Ftp0#single(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSingleStringStringString()
	{
		Assert.assertNotEquals(null, ftp0);
	}

	/**
	 * Test method for {@link org.azolla.open.ling.ftp.Ftp0#deleteFile(java.lang.String)}.
	 */
	@Test
	public void testDeleteFileString()
	{
		System.out.println(ftp0.deleteFile(REMOTE_TEST_ROOT));
		System.out.println(ftp0.deleteFile(REMOTE_TEST_ROOT + TEST_FILENAME));
	}

	/**
	 * Test method for {@link org.azolla.open.ling.ftp.Ftp0#retrieveFile(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveFileStringString()
	{
		System.out.println(ftp0.retrieveFile(REMOTE_TEST_ROOT, TEST_FILE.getParentFile()));
	}

	/**
	 * Test method for {@link org.azolla.open.ling.ftp.Ftp0#isExist(java.lang.String)}.
	 */
	@Test
	public void testExistString()
	{
		String[] array = "/a/".split("/");
		System.out.println(array);
		String[] subArray = Arrays.copyOfRange(array, 1, array.length - 1);
		System.out.println("/" + Joiner.on("/").join(subArray));
		System.out.println(ftp0.isExist(REMOTE_TEST_ROOT));
	}

	/**
	 * Test method for {@link org.azolla.open.ling.ftp.Ftp0#isExist(org.apache.commons.net.ftp.FTPClient, java.lang.String)}.
	 */
	@Test
	public void testExistFTPClientString()
	{
		System.out.println(ftp0.isExist(ftp0.getClient(), REMOTE_TEST_ROOT));
	}

	@Test
	public void testIsDirectoryString()
	{
		System.out.println(ftp0.isDirectory(REMOTE_TEST_ROOT));
		System.out.println(ftp0.isDirectory(REMOTE_TEST_ROOT + TEST_FILENAME));
	}

	/**
	 * Test method for {@link org.azolla.open.ling.ftp.Ftp0#listNames()}.
	 */
	@Test
	public void testListNames()
	{
		for(String s : ftp0.listNames())
		{
			System.out.println(s);
		}
		for(String s : ftp0.listNames(""))
		{
			System.out.println(s);
		}
		for(String s : ftp0.listNames("/"))
		{
			System.out.println(s);
		}
		System.out.println("---Folder---Return:SubFile");
		for(String s : ftp0.listNames(REMOTE_TEST_ROOT))
		{
			System.out.println(s);
		}
		System.out.println("---JustOneFileFolder---Return:File");
		for(String s : ftp0.listNames(REMOTE_TEST_ROOT + "cfg"))
		{
			System.out.println(s);
		}
		System.out.println("---EmptyFolder---Return:Empty");
		for(String s : ftp0.listNames(REMOTE_TEST_ROOT + "emptyFolder"))
		{
			System.out.println(s);
		}
		System.out.println("---Document---Return:Document");
		for(String s : ftp0.listNames(REMOTE_TEST_ROOT + TEST_FILENAME))
		{
			System.out.println(s);
		}
		System.out.println("---Notexist---Return:Empty");
		for(String s : ftp0.listNames(REMOTE_TEST_ROOT + "abcxxxxxxxxxxx"))
		{
			System.out.println(s);
		}
	}

	/**
	 * Test method for {@link org.azolla.open.ling.ftp.Ftp0#allNames()}.
	 */
	@Test
	public void testAllNames()
	{
		for(String s : ftp0.allNames())
		{
			System.out.println(s);
		}
		System.out.println("---");
		for(String s : ftp0.allNames(""))
		{
			System.out.println(s);
		}
		System.out.println("---Folder---Return:SubFile");
		for(String s : ftp0.allNames(REMOTE_TEST_ROOT))
		{
			System.out.println(s);
		}
		System.out.println("---JustOneFileFolder---Return:File");
		for(String s : ftp0.allNames(REMOTE_TEST_ROOT + "cfg"))
		{
			System.out.println(s);
		}
		System.out.println("---EmptyFolder---Return:Empty");
		for(String s : ftp0.allNames(REMOTE_TEST_ROOT + "emptyFolder"))
		{
			System.out.println(s);
		}
		System.out.println("---Document---Return:Document");
		for(String s : ftp0.allNames(REMOTE_TEST_ROOT + TEST_FILENAME))
		{
			System.out.println(s);
		}
		System.out.println("---Notexist---Return:Empty");
		for(String s : ftp0.allNames(REMOTE_TEST_ROOT + "abcxxxxxxxxxxx"))
		{
			System.out.println(s);
		}
	}

	/**
	 * Test method for {@link org.azolla.open.ling.ftp.Ftp0#listFiles()}.
	 */
	@Test
	public void testListFiles()
	{
		for(FTPFile ftpFile : ftp0.listFiles())
		{
			System.out.println(ftpFile.getLink());
			System.out.println(ftpFile.getName());
			System.out.println(ftpFile.getRawListing());
		}
		for(FTPFile ftpFile : ftp0.listFiles(REMOTE_TEST_ROOT + TEST_FILENAME))
		{
			System.out.println(ftpFile.getLink());
			System.out.println(ftpFile.getName());
			System.out.println(ftpFile.getRawListing());
		}
	}

	/**
	 * Test method for {@link org.azolla.open.ling.ftp.Ftp0#storeFile(java.lang.String, java.io.File)}.
	 */
	@Test
	public void testStoreFileStringFile()
	{
		System.out.println(ftp0.storeFile(REMOTE_TEST_ROOT, TEST_FILE));
	}

	public void testFTPFile()
	{

	}

}
