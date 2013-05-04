/*
 * @(#)TupleTest.java		Created at 2013-5-2
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.collect;

import org.azolla.open.common.collect.Tuple.Quadruple;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The coder is very lazy, nothing to write for this TupleTest class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class TupleTest
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
		//		fail("Not yet implemented");
		Quadruple<Integer, Integer, Integer, Integer> ip = Tuple.of(127, 0, 0, 1);
		System.out.println(Tuple.joinWith("", ".", "").join(ip));
		System.out.println(Tuple.joinWith(".").join(ip));

		//		MessageFormat.format(pattern, arguments)

		//		Vector<String> v = Lists.newArrayList();
		//		List<String> list = Lists.<String> newArrayList();
	}
}
