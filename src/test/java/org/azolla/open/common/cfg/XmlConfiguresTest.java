/*
 * @(#)XmlConfiguresTest.java		Created at 2013-5-2
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.cfg;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The coder is very lazy, nothing to write for this XmlConfiguresTest class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class XmlConfiguresTest
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

	/**
	 * Test method for {@link org.azolla.open.common.cfg.DataCfg#unmarshal(java.lang.Class, java.lang.String)}.
	 */
	@Test
	public void testUnmarshal()
	{
		//Just test log
		System.out.println(DataCfg.unmarshal(Object.class, System.getProperty("user.dir")));
	}

	/**
	 * Test method for {@link org.azolla.open.common.cfg.DataCfg#marshal(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testMarshal()
	{
		//Just test log
		DataCfg.marshal(Object.class, System.getProperty("user.dir"));
	}

}
