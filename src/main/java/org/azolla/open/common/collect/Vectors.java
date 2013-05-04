/*
 * @(#)Vectors.java		Created at 2013-5-4
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.collect;

import java.util.List;
import java.util.Vector;

/**
 * The coder is very lazy, nothing to write for this Vectors class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Vectors
{
	public static <T> Vector<T> emptyVector()
	{
		return new Vector<T>(0, 0);
	}

	public static <T> Vector<Vector<T>> transform(List<List<T>> from)
	{
		if(null == from)
		{
			return emptyVector();
		}

		Vector<Vector<T>> to = new Vector<Vector<T>>(from.size());
		for(List<T> fromEntry : from)
		{
			to.add(new Vector<T>(fromEntry));
		}
		to.trimToSize();

		return to;
	}
}
