/*
 * @(#)Date0.java		Created at 2013-6-30
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

import org.azolla.l.ling.exception.code.AzollaCode;
import org.azolla.l.ling.text.Fmt0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DateHelper
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Date0
{
    public static final String  Y            = "yyyy";
    public static final String  M            = "MM";
    public static final String  D            = "dd";
    public static final String  YM           = "yyyyMM";
    public static final String  YMD          = "yyyyMMdd";
    public static final String  Y_M          = "yyyy-MM";
    public static final String  Y_M_D        = "yyyy-MM-dd";

    public static final String  H            = "HH";
    public static final String  MI           = "mm";
    public static final String  S            = "ss";

    public static final String  Y_M_D_H_MI_S = "yyyy_MM_dd_HH_mm_ss";

    public static final String  DATE         = Y_M_D;
    public static final String  TIME         = "HH:mm:ss";
    public static final String  DATA_TIME    = "yyyy-MM-dd HH:mm:ss";               //2013-05-04 19:51:08
    public static final String  DATATIME     = "yyyyMMddHHmmss";

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

    public static String toString(Date date)
    {
        return toString(date, DATA_TIME);
    }

    public static String toString(String pattern)
    {
        return toString(now(), pattern);
    }

    public static String toString(Date date, String pattern)
    {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static long toUnixTimestamp()
    {
        return toUnixTimestamp(now());
    }

    public static long toUnixTimestamp(Date date)
    {
        return date.getTime();
    }

    public static Date valueOf(long timestamp)
    {
        return new Date(timestamp);
    }

    public static Date valueOf(String date)
    {
        return valueOf(date, DATA_TIME);
    }

    public static Date valueOf(String date, String pattern)
    {
        Date rtnDate = null;

        try
        {
            rtnDate = new SimpleDateFormat(pattern).parse(date);
        }
        catch(ParseException e)
        {
            Log0.error(Date0.class, Fmt0.LOG_EC_P_M, AzollaCode.PARSEEXCEPTION, KV.ins("date", date).put("pattern", pattern), e);
            rtnDate = null;
        }

        return rtnDate;
    }

    public static int compareWithNow(@Nonnull Date date)
    {
        return date.compareTo(now());
    }

}
