/*
 * @(#)TupleTest.java		Created at 2014-1-21
 * 
 * Copyright (c) 2011-2014 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.collect;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.azolla.l.ling.collect.Tuple;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Stopwatch;

/**
 * The coder is very lazy for this TupleTest class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
@RunWith(Parameterized.class)
public class TupleTest
{
    private Stopwatch stopwatch = Stopwatch.createStarted();

    @Rule
    public TestName   testName  = new TestName();

    private String    ip;

    /**
     * This is a constructor
     *
     */
    public TupleTest(String ip)
    {
        //do nothing
        super();
        this.ip = ip;
    }

    //@Parameters修饰我们提供参数的静态方法，它需要返回List<Object[]>，List包含的是参数组，Object[]即按顺序提供的一组参数。
    @Parameters
    public static List<String[]> testData()
    {
        Random random = new Random();
        int l1 = 4;
        int l2 = 1;
        String[][] rtnArray = new String[l1][l2];
        for(int i1 = 0; i1 < l1; i1++)
        {
            for(int i2 = 0; i2 < l2; i2++)
            {
                rtnArray[i1][i2] = "" + random.nextInt(255) + "." + random.nextInt(255) + "." + random.nextInt(255) + "." + random.nextInt(255);
            }
        }

        return Arrays.asList(rtnArray);

    }

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

//    @Test(timeout = 33)
    @Test
    public void test()
    {
        String[] ipArray = ip.split("\\.");
        Assert.assertEquals(Tuple.of(ipArray[0], ipArray[1], ipArray[2], ipArray[3]).toString("", ".", ""), ip);
    }
}
