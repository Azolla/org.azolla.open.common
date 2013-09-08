/*
 * @(#)Cfg0Test.java		Created at 2013-9-1
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.cfg;

import java.io.File;

import org.azolla.open.ling.cfg.test.TestRootNode;
import org.azolla.open.ling.io.Encoding;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The coder is very lazy, nothing to write for this Cfg0Test class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class Cfg0Test
{

	private TestRootNode	testRootNode;

	private File			testDataCfgFile	= new File(System.getProperty("user.dir"), "src/test/resources/" + Cfg0Test.this.getClass().getPackage().getName().replace('.', '/') + "/testDataCfg.xml");

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
		testRootNode = Cfg0.unmarshal(TestRootNode.class, testDataCfgFile);
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
	 * Test method for {@link org.azolla.open.ling.cfg.Cfg0#unmarshal(java.lang.Class, java.lang.String)}.
	 */
	@Test
	public void testUnmarshal()
	{
		testRootNode = Cfg0.unmarshal(TestRootNode.class, testDataCfgFile);
		testRootNode = Cfg0.unmarshal(null, testDataCfgFile);
		testRootNode = Cfg0.unmarshal(TestRootNode.class, null);
		testRootNode = Cfg0.unmarshal(null, null);
	}

	/**
	 * Test method for {@link org.azolla.open.ling.cfg.Cfg0#marshal(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testMarshalTString()
	{
		Cfg0.marshal(testRootNode, testDataCfgFile);
		Cfg0.marshal(testRootNode, null);
		Cfg0.marshal(null, testDataCfgFile);
		Cfg0.marshal(null, null);
	}

	/**
	 * Test method for {@link org.azolla.open.ling.cfg.Cfg0#marshal(java.lang.Object, java.lang.String, org.azolla.open.ling.io.Encoding)}.
	 */
	@Test
	public void testMarshalTStringEncoding()
	{
		Cfg0.marshal(testRootNode, testDataCfgFile, Encoding.UTF8);
		Cfg0.marshal(testRootNode, testDataCfgFile, null);
		Cfg0.marshal(testRootNode, null, Encoding.UTF8);
		Cfg0.marshal(null, testDataCfgFile, Encoding.UTF8);
		Cfg0.marshal(testRootNode, null, null);
		Cfg0.marshal(null, null, Encoding.UTF8);
		Cfg0.marshal(null, testDataCfgFile, Encoding.UTF8);
		Cfg0.marshal(testRootNode, testDataCfgFile, null);
		Cfg0.marshal(null, null, null);
	}

	/**
	 * Test method for {@link org.azolla.open.ling.cfg.Cfg0#reset()}.
	 */
	@Test
	public void testReset()
	{
		testRootNode = Cfg0.unmarshal(TestRootNode.class, testDataCfgFile);
		Cfg0.reset();
		testRootNode = Cfg0.unmarshal(TestRootNode.class, testDataCfgFile);
	}

}
