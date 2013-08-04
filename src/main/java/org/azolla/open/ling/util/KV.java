/*
 * @(#)KV.java		Created at 2013-7-4
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.util;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * The coder is very lazy, nothing to write for this KV class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class KV
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

	//	public static KV ins()
	//	{
	//		return new KV();
	//	}

	public static KV ins(String key, String value)
	{
		return new KV().set(key, value);
	}

	public static KV ins(String key, Object o)
	{
		return new KV().set(key, o);
	}

	public KV set(String key, String value)
	{
		map.put(key, value);
		return this;
	}

	public KV set(String key, Object o)
	{
		map.put(key, o2String(o));
		return this;
	}

	/**
	 * The coder is very lazy, nothing to write for this o2String method
	 * 
	 * @param o
	 * @return String
	 */
	private String o2String(Object o)
	{
		String rtn = String.valueOf(o);

		if(o.getClass().isArray())
		{
			// check for primitive array types because they
			// unfortunately cannot be cast to Object[]
			if(o instanceof boolean[] || o instanceof Boolean[])
			{
				rtn = Array0.array2String((Boolean[]) o);
			}
			else if(o instanceof byte[] || o instanceof Byte[])
			{
				rtn = Array0.array2String((Byte[]) o);
			}
			else if(o instanceof char[] || o instanceof Character[])
			{
				rtn = Array0.array2String((Character[]) o);
			}
			else if(o instanceof short[] || o instanceof Short[])
			{
				rtn = Array0.array2String((Short[]) o);
			}
			else if(o instanceof int[] || o instanceof Integer[])
			{
				rtn = Array0.array2String((Integer[]) o);
			}
			else if(o instanceof long[] || o instanceof Long[])
			{
				rtn = Array0.array2String((Long[]) o);
			}
			else if(o instanceof float[] || o instanceof Float[])
			{
				rtn = Array0.array2String((Float[]) o);
			}
			else if(o instanceof double[] || o instanceof Double[])
			{
				rtn = Array0.array2String((Double[]) o);
			}
			else
			{
				rtn = Array0.array2String((Object[]) o);
			}
		}
		return rtn;
	}

	public String toString(String separator, String connector)
	{
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, String> entry : map.entrySet())
		{
			sb.append(separator).append(entry.getKey()).append(connector).append(entry.getValue());
		}
		return "[" + sb.toString().substring(null == separator ? 4 : separator.length()) + "]";
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
