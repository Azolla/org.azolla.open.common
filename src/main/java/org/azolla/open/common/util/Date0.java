/*
 * @(#)Date0.java		Created at 2013-6-30
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.base.Preconditions;

/**
 * DateHelper
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Date0
{
	public static final String	Y				= "yyyy";
	public static final String	M				= "MM";
	public static final String	D				= "dd";
	public static final String	YM				= "yyyyMM";
	public static final String	YMD				= "yyyyMMdd";
	public static final String	Y_M				= "yyyy-MM";
	public static final String	Y_M_D			= "yyyy-MM-dd";

	public static final String	DATE			= Y_M_D;
	public static final String	TIME			= "HH:mm:ss";
	public static final String	DATA_TIME		= "yyyy-MM-dd HH:mm:ss";	//2013-05-04 19:51:08

	public static final String	DATA_TIME_STAMP	= "yyyyMMddHHmmss";

	public static String toString(Date date)
	{
		return toString(date, DATA_TIME);
	}

	public static String toString(Date date, String pattern)
	{
		Preconditions.checkNotNull(date);
		Preconditions.checkNotNull(pattern);

		return new SimpleDateFormat(pattern).format(date);
	}

	public static final String DATA_TIME_STAMP()
	{
		return toString(new Date(), DATA_TIME_STAMP);
	}

	public static final String YM()
	{
		return toString(new Date(), YM);
	}
}
