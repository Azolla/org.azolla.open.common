/*
 * @(#)Enumeration0.java		Created at 2013-6-30
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.util;

import java.util.Enumeration;
import java.util.Iterator;

import com.google.common.collect.Iterators;

/**
 * EnumerationHelper
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Enumeration0
{
    public static <T> Iterable<T> toIterable(final Enumeration<T> enumerator)
    {
        return new Iterable<T>()
        {
            @Override
            public Iterator<T> iterator()
            {
                return null == enumerator ? Iterators.<T> emptyIterator() : Iterators.forEnumeration(enumerator);
            }
        };
    }
}
