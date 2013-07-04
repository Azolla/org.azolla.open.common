/*
 * @(#)KV.java		Created at 2013-7-4
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.util;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * The coder is very lazy, nothing to write for this KV class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class KV
{
	Map<String, String>	map	= Maps.newHashMap();

	/**
	 * This is a constructor
	 *
	 */
	private KV()
	{
		//do nothing
	}

	public static KV newKV()
	{
		return new KV();
	}

	public KV set(String key, String value)
	{
		map.put(key, value);
		return this;
	}

	public String toString(String separator, String connector)
	{
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, String> entry : map.entrySet())
		{
			sb.append(separator).append(entry.getKey()).append(connector).append(entry.getValue());
		}
		return sb.toString().substring(null == separator ? 4 : separator.length());
	}

	@Override
	public String toString()
	{
		return toString(";", "=");
	}

	public static void main(String[] args)
	{
		StringBuilder sb = new StringBuilder();
		Object o = null;
		sb.append(o).append("a");
		System.out.println(sb.toString());
	}
}
