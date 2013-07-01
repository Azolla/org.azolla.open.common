/*
 * @(#)Date0.java		Created at 2013-6-30
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * DateHelper
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Date0
{
	private static final Logger	LOG			= LoggerFactory.getLogger(Date0.class);

	public static final String	Y			= "yyyy";
	public static final String	M			= "MM";
	public static final String	D			= "dd";
	public static final String	YM			= "yyyyMM";
	public static final String	YMD			= "yyyyMMdd";
	public static final String	Y_M			= "yyyy-MM";
	public static final String	Y_M_D		= "yyyy-MM-dd";

	public static final String	DATE		= Y_M_D;
	public static final String	TIME		= "HH:mm:ss";
	public static final String	DATA_TIME	= "yyyy-MM-dd HH:mm:ss";				//2013-05-04 19:51:08
	public static final String	DATATIME	= "yyyyMMddHHmmss";

	/**
	 * 20130701195108
	 * 
	 * @return String
	 */
	public static final String DATATIME()
	{
		return toString(now(), DATATIME);
	}

	/**
	 * 201307
	 * 
	 * @return String
	 */
	public static final String YM()
	{
		return toString(now(), YM);
	}

	public static Date now()
	{
		return new Date();
	}

	/**
	 * Date -> String
	 * 
	 * @param date
	 * @return String
	 */
	public static String toString(Date date)
	{
		return toString(date, DATA_TIME);
	}

	/**
	 * Date -> String
	 * 
	 * @param date
	 * @param pattern
	 * @return String
	 */
	public static String toString(Date date, String pattern)
	{
		Preconditions.checkNotNull(date);
		Preconditions.checkNotNull(pattern);

		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * Date -> long
	 * 
	 * @return long
	 */
	public static long toUnixTimestamp()
	{
		return toUnixTimestamp(now());
	}

	/**
	 * Date -> long
	 * 
	 * @param date
	 * @return long
	 */
	public static long toUnixTimestamp(Date date)
	{
		Preconditions.checkNotNull(date);

		return date.getTime();
	}

	/**
	 * long -> Date
	 * 
	 * @param timestamp
	 * @return Date
	 */
	public static Date valueOf(long timestamp)
	{
		return new Date(timestamp);
	}

	/**
	 * String -> Date
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date valueOf(String date)
	{
		return valueOf(date, DATA_TIME);
	}

	/**
	 * String -> Date
	 * 
	 * @param date
	 * @param pattern
	 * @return Date
	 */
	public static Date valueOf(String date, String pattern)
	{
		Date rtnDate = null;

		try
		{
			rtnDate = new SimpleDateFormat(pattern).parse(date);
		}
		catch(ParseException e)
		{
			LOG.error("dateString = [{}], pattern = [{}]", date, pattern, e.toString(), e);
			rtnDate = null;
		}

		return rtnDate;
	}
	
	/**
	 * date > now = 1
	 * date = now = 0
	 * date < now = -1
	 * 
	 * @param date
	 * @return int
	 */
	public static int compareWithNow(Date date)
	{
		Preconditions.checkNotNull(date);

		return date.compareTo(now());
	}

}
