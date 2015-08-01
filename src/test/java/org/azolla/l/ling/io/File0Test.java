/*
 * @(#)File0Test.java		Created at 15/7/29
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.io;

import org.junit.Assert;
import org.junit.Test;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class File0Test
{
    @Test
    public void testFileNameWithoutFileType()
    {
        Assert.assertEquals("abc",File0.fileNameWithoutFileType("abc.txt"));
    }
}
