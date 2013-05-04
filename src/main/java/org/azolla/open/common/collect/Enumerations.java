/*
 * @(#)Enumerations.java		Created at 2013-5-4
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.collect;

import java.util.Enumeration;
import java.util.Iterator;

import com.google.common.collect.Iterators;

/**
 * The coder is very lazy, nothing to write for this Enumerations class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Enumerations
{
	public static <T> Iterable<T> asIterable(final Enumeration<T> enumerator)
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
