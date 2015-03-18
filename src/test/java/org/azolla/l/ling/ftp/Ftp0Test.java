/*
 * @(#)Ftp0Test.java		Created at 2014-1-22
 * 
 * Copyright (c) 2011-2014 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.ftp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;

import com.google.common.base.Stopwatch;

/**
 * The coder is very lazy for this Ftp0Test class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class Ftp0Test
{
    @ClassRule
    public static TemporaryFolder folderCreater = new TemporaryFolder();

    @Rule
    public TestName               testName      = new TestName();

    private Stopwatch             stopwatch     = Stopwatch.createStarted();

    /**
     * The coder is very lazy for this setUpBeforeClass method
     * 
     * @throws java.lang.Exception void
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
    }

    /**
     * The coder is very lazy for this tearDownAfterClass method
     * 
     * @throws java.lang.Exception void
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    /**
     * The coder is very lazy for this setUp method
     * 
     * @throws java.lang.Exception void
     */
    @Before
    public void setUp() throws Exception
    {
        folderCreater.create();
        stopwatch = Stopwatch.createStarted();
    }

    /**
     * The coder is very lazy for this tearDown method
     * 
     * @throws java.lang.Exception void
     */
    @After
    public void tearDown() throws Exception
    {
        System.out.println(testName.getMethodName() + "=" + stopwatch.stop());
    }

    /**
     * Test method for {@link org.azolla.l.ling.ftp.Ftp0#deleteFile(org.apache.commons.net.ftp.FTPClient, java.lang.String)}.
     */
    @Test
    public void testDeleteFile()
    {
        //        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.azolla.l.ling.ftp.Ftp0#retrieveFile(org.apache.commons.net.ftp.FTPClient, java.lang.String, java.io.File)}.
     */
    @Test
    public void testRetrieveFile()
    {
        //        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.azolla.l.ling.ftp.Ftp0#isExist(org.apache.commons.net.ftp.FTPClient, java.lang.String)}.
     */
    @Test
    public void testIsExist()
    {
        //        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.azolla.l.ling.ftp.Ftp0#isDirectory(org.apache.commons.net.ftp.FTPClient, java.lang.String)}.
     */
    @Test
    public void testIsDirectory()
    {
        //        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.azolla.l.ling.ftp.Ftp0#listNames(org.apache.commons.net.ftp.FTPClient, java.lang.String)}.
     */
    @Test
    public void testListNames()
    {
        //        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.azolla.l.ling.ftp.Ftp0#allNames(org.apache.commons.net.ftp.FTPClient, java.lang.String, org.apache.commons.net.ftp.FTPFileFilter)}.
     */
    @Test
    public void testAllNames()
    {
        //        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.azolla.l.ling.ftp.Ftp0#listFiles(org.apache.commons.net.ftp.FTPClient, java.lang.String, org.apache.commons.net.ftp.FTPFileFilter)}.
     */
    @Test
    public void testListFiles()
    {
        //        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.azolla.l.ling.ftp.Ftp0#storeFile(org.apache.commons.net.ftp.FTPClient, java.lang.String, java.io.File)}.
     */
    @Test
    public void testStoreFile()
    {
        //        fail("Not yet implemented");
    }

}
