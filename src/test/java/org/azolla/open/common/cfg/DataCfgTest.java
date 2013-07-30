/*
 * @(#)XmlConfiguresTest.java		Created at 2013-5-2
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.cfg;

import java.io.File;

import org.azolla.open.common.cfg.test.TestRootNode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The coder is very lazy, nothing to write for this XmlConfiguresTest class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class DataCfgTest
{

	private TestRootNode	testRootNode;

	private File			testDataCfgFile	= new File(System.getProperty("user.dir"), "src/test/resources/org/azolla/open/common/cfg/testDataCfg.xml");

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

	/**
	 * Test method for {@link org.azolla.open.common.cfg.Cfg0#unmarshal(java.lang.Class, java.lang.String)}.
	 */
	@Test
	public void testUnmarshal()
	{
		testRootNode = Cfg0.unmarshal(TestRootNode.class, testDataCfgFile.getAbsolutePath());
		Assert.assertNotNull(testRootNode);
		//Just test log
		//		System.out.println(DataCfg.unmarshal(Object.class, System.getProperty("user.dir")));
	}

	/**
	 * Test method for {@link org.azolla.open.common.cfg.Cfg0#marshal(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testMarshal()
	{
		testUnmarshal();

		testRootNode.setTestAttrbuteNode2(testRootNode.getTestAttrbuteNode2() + 1);
		Cfg0.marshal(testRootNode, testDataCfgFile.getAbsolutePath());
	}

}
