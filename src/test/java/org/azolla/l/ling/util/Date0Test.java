/*
 * @(#)Date0Test.java		Created at 15/8/2
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.util;

import org.junit.Test;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class Date0Test
{
    @Test
    public void test2String()
    {
        System.out.println(Date0.toString(Date0.now(),"yyyyMMddHHm"));
    }
}
