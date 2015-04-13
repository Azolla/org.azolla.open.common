/*
 * @(#)String0Test.java		Created at 15/4/13
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.lang;

import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class String0Test
{
    @Test
    public void testPinyin()
    {
        assertEquals("woshi-zhongguoer_", String0.pinyin("我SHI 中国er！"));
    }
}
