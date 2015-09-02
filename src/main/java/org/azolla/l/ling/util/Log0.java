/*
 * @(#)Log0.java		Created at 15/6/15
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.util;

import com.google.common.collect.Maps;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Just convenient for change log system.
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class Log0
{
    public static final String                             FQCN        = Log0.class.getName();
    private static      Map<Class<?>, LocationAwareLogger> log0Map     = Maps.newHashMap();
    private static      Map<Class<?>, Boolean>             log0BoolMap = Maps.newHashMap();

    private static LocationAwareLogger getLog0(Class<?> clazz)
    {
        LocationAwareLogger rtnLog = log0Map.get(clazz);
        if (rtnLog == null && log0BoolMap.get(clazz) == null)
        {
            try
            {
                rtnLog = (LocationAwareLogger) LoggerFactory.getLogger(clazz);
            }
            catch (Exception e)
            {
            }
            finally
            {
                log0Map.put(clazz, rtnLog);
                log0BoolMap.put(clazz, true);
            }
        }
        return rtnLog;
    }

    private static void msg(Class<?> clazz, int level, String msg, Throwable t)
    {
        LocationAwareLogger locationAwareLogger = log0Map.get(clazz);
        if (locationAwareLogger != null)
        {
            locationAwareLogger.log(null, FQCN, level, msg, null, t);
        }
        else
        {
            LoggerFactory.getLogger(clazz).error(msg, t);
        }
    }

    private static void fmt(Class<?> clazz, int level, String format, @Nonnull Object... objects)
    {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, objects);
        msg(clazz, level, ft.getMessage(), ft.getThrowable());
    }

    public static void trace(Class<?> clazz, String msg)
    {
        msg(clazz, LocationAwareLogger.TRACE_INT, msg, null);
    }

    public static void trace(Class<?> clazz, @Nonnull String format, @Nonnull Object... objects)
    {
        fmt(clazz, LocationAwareLogger.TRACE_INT, format, objects);
    }

    public static void trace(Class<?> clazz, String format, Throwable throwable)
    {
        msg(clazz, LocationAwareLogger.TRACE_INT, format, throwable);
    }

    public static void debug(Class<?> clazz, String msg)
    {
        msg(clazz, LocationAwareLogger.DEBUG_INT, msg, null);
    }

    public static void debug(Class<?> clazz, @Nonnull String format, @Nonnull Object... objects)
    {
        fmt(clazz, LocationAwareLogger.DEBUG_INT, format, objects);
    }

    public static void debug(Class<?> clazz, String format, Throwable throwable)
    {
        msg(clazz, LocationAwareLogger.DEBUG_INT, format, throwable);
    }

    public static void info(Class<?> clazz, String msg)
    {
        msg(clazz, LocationAwareLogger.INFO_INT, msg, null);
    }

    public static void info(Class<?> clazz, @Nonnull String format, @Nonnull Object... objects)
    {
        fmt(clazz, LocationAwareLogger.INFO_INT, format, objects);
    }

    public static void info(Class<?> clazz, String format, Throwable throwable)
    {
        msg(clazz, LocationAwareLogger.INFO_INT, format, throwable);
    }

    public static void warn(Class<?> clazz, String msg)
    {
        msg(clazz, LocationAwareLogger.WARN_INT, msg, null);
    }

    public static void warn(Class<?> clazz, @Nonnull String format, @Nonnull Object... objects)
    {
        fmt(clazz, LocationAwareLogger.WARN_INT, format, objects);
    }

    public static void warn(Class<?> clazz, String format, Throwable throwable)
    {
        msg(clazz, LocationAwareLogger.WARN_INT, format, throwable);
    }

    public static void error(Class<?> clazz, String msg)
    {
        msg(clazz, LocationAwareLogger.ERROR_INT, msg, null);
    }

    public static void error(Class<?> clazz, @Nonnull String format, @Nonnull Object... objects)
    {
        fmt(clazz, LocationAwareLogger.ERROR_INT, format, objects);
    }

    public static void error(Class<?> clazz, String format, Throwable throwable)
    {
        msg(clazz, LocationAwareLogger.ERROR_INT, format, throwable);
    }

}