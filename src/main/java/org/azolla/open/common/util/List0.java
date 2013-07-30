/*
 * @(#)List0.java		Created at 2013-7-29
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.util;

import java.util.List;

/**
 * The coder is very lazy, nothing to write for this List0 class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class List0
{

	public static <T> String list2String(List<T> list)
	{
		if(null == list)
		{
			return "null";
		}
		StringBuffer rtn = new StringBuffer();
		for(T t : list)
		{
			rtn.append(";").append(String.valueOf(t));
		}
		return "[" + (rtn.length() > 0 ? rtn.substring(1) : "") + "]";
	}

}
