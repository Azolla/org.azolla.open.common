/*
 * @(#)Integer0Test.java		Created at 2014年12月24日
 * 
 * Copyright (c) 2011-2014 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.lang;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.azolla.l.ling.lang.Integer0;
import org.junit.Test;

/**
 * The coder is very lazy for this Integer0Test class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class Integer0Test
{

    /**
     * Test method for {@link org.azolla.l.ling.lang.Integer0#isInt(java.lang.String)}.
     */
    @Test
    public void testIsInt()
    {
        assertFalse(Integer0.isInt("a"));
        assertTrue(Integer0.isInt("1"));
    }

}
