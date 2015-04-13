/*
 * @(#)Array0.java		Created at 2013-7-29
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.util;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * The coder is very lazy, nothing to write for this Array0 class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public final class Array0
{
    public static <T> String array2String(T[] array)
    {
        return List0.list2String(Arrays.asList(array));
    }
}
