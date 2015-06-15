/*
 * @(#)Log0.java		Created at 15/6/15
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.util;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.util.Map;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class Log0
{
    private static Map<Class<?>, Logger> logMap = Maps.newHashMap();

    public static Logger getLog(Class<?> clazz)
    {
        Logger rtnLog = logMap.get(clazz);
        if(rtnLog == null)
        {
            rtnLog = LoggerFactory.getLogger(clazz);
            logMap.put(clazz,rtnLog);
        }
        return rtnLog;
    }

    public static void trace(Class<?> clazz,String msg)
    {
        getLog(clazz).trace(msg);
    }

    public static void trace(Class<?> clazz,String format, Object... objects)
    {
        getLog(clazz).trace(format,objects);
    }

    public static void trace(Class<?> clazz,String format, Throwable throwable)
    {
        getLog(clazz).trace(format,throwable);
    }

    public static void trace(Class<?> clazz,Marker marker, String msg)
    {
        getLog(clazz).trace(marker,msg);
    }
    
    public static void trace(Class<?> clazz,Marker marker, String format, Object... objects)
    {
        getLog(clazz).trace(marker,format,objects);
    }

    public static void trace(Class<?> clazz,Marker marker, String msg, Throwable throwable)
    {
        getLog(clazz).trace(marker,msg,throwable);
    }

    public static void debug(Class<?> clazz,String msg)
    {
        getLog(clazz).debug(msg);
    }

    public static void debug(Class<?> clazz,String format, Object... objects)
    {
        getLog(clazz).debug(format,objects);
    }

    public static void debug(Class<?> clazz,String format, Throwable throwable)
    {
        getLog(clazz).debug(format,throwable);
    }

    public static void debug(Class<?> clazz,Marker marker, String msg)
    {
        getLog(clazz).debug(marker,msg);
    }

    public static void debug(Class<?> clazz,Marker marker, String format, Object... objects)
    {
        getLog(clazz).debug(marker,format,objects);
    }

    public static void debug(Class<?> clazz,Marker marker, String msg, Throwable throwable)
    {
        getLog(clazz).debug(marker,msg,throwable);
    }

    public static void info(Class<?> clazz,String msg)
    {
        getLog(clazz).info(msg);
    }

    public static void info(Class<?> clazz,String format, Object... objects)
    {
        getLog(clazz).info(format, objects);
    }

    public static void info(Class<?> clazz,String format, Throwable throwable)
    {
        getLog(clazz).info(format, throwable);
    }

    public static void info(Class<?> clazz,Marker marker, String msg)
    {
        getLog(clazz).info(marker, msg);
    }

    public static void info(Class<?> clazz,Marker marker, String format, Object... objects)
    {
        getLog(clazz).info(marker, format, objects);
    }

    public static void info(Class<?> clazz,Marker marker, String msg, Throwable throwable)
    {
        getLog(clazz).info(marker, msg, throwable);
    }

    public static void warn(Class<?> clazz,String msg)
    {
        getLog(clazz).warn(msg);
    }

    public static void warn(Class<?> clazz,String format, Object... objects)
    {
        getLog(clazz).warn(format, objects);
    }

    public static void warn(Class<?> clazz,String format, Throwable throwable)
    {
        getLog(clazz).warn(format, throwable);
    }

    public static void warn(Class<?> clazz,Marker marker, String msg)
    {
        getLog(clazz).warn(marker, msg);
    }

    public static void warn(Class<?> clazz,Marker marker, String format, Object... objects)
    {
        getLog(clazz).warn(marker, format, objects);
    }

    public static void warn(Class<?> clazz,Marker marker, String msg, Throwable throwable)
    {
        getLog(clazz).warn(marker, msg, throwable);
    }

    public static void error(Class<?> clazz,String msg)
    {
        getLog(clazz).error(msg);
    }

    public static void error(Class<?> clazz,String format, Object... objects)
    {
        getLog(clazz).error(format,objects);
    }

    public static void error(Class<?> clazz,String format, Throwable throwable)
    {
        getLog(clazz).error(format,throwable);
    }

    public static void error(Class<?> clazz,Marker marker, String msg)
    {
        getLog(clazz).error(marker,msg);
    }

    public static void error(Class<?> clazz,Marker marker, String format, Object... objects)
    {
        getLog(clazz).error(marker,format,objects);
    }

    public static void error(Class<?> clazz,Marker marker, String msg, Throwable throwable)
    {
        getLog(clazz).error(marker,msg,throwable);
    }
}
