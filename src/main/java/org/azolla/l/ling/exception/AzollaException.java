/*
 * @(#)AzollaException.java		Created at 2013-2-23
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Nullable;

import org.azolla.l.ling.exception.code.AzollaCode;
import org.azolla.l.ling.exception.code.ErrorCoder;

import com.google.common.collect.Maps;

/**
 * The exception for Azolla
 * 
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class AzollaException extends RuntimeException
{
    private static final long         serialVersionUID = 6975777768178330101L;

    private ErrorCoder                errorCode;
    private final Map<String, Object> properties       = Maps.newTreeMap();   //sort

    public AzollaException(ErrorCoder errorCode)
    {
        setErrorCode(errorCode);
    }

    public AzollaException(ErrorCoder errorCode, String message)
    {
        super(message);
        setErrorCode(errorCode);
    }

    public AzollaException(ErrorCoder errorCode, Throwable cause)
    {
        super(cause);
        setErrorCode(errorCode);
    }

    public AzollaException(ErrorCoder errorCode, String message, Throwable cause)
    {
        super(message, cause);
        setErrorCode(errorCode);
    }

    public static AzollaException wrap(Throwable cause)
    {
        return wrap(cause, AzollaCode.UNAZOLLA);
    }

    public static AzollaException wrap(Throwable cause, ErrorCoder errorCode)
    {
        if(cause instanceof AzollaException)
        {
            AzollaException se = (AzollaException) cause;
            ErrorCoder currentErrorCoder = se.getErrorCode();

            return errorCode != currentErrorCoder ? new AzollaException(errorCode, cause) : se;
        }
        return new AzollaException(errorCode, cause);
    }

    @Override
    public void printStackTrace(PrintStream s)
    {
        synchronized(s)
        {
            s.println(this);

            s.println();
            s.println("\t--------Azolla Exception Properties--------");
            if(errorCode != null)
            {
                s.println("\t" + errorCode + ":" + errorCode.getClass().getName());
            }
            for(Map.Entry<String, Object> entry : properties.entrySet())
            {
                s.println("\t" + entry.getKey() + "=[" + entry.getValue() + "]");
            }
            s.println("\t-------------------------------------------");
            s.println();

            StackTraceElement[] trace = getStackTrace();
            for(int i = 0; i < trace.length; i++)
            {
                s.println("\tat " + trace[i]);
            }

            Throwable ourCause = getCause();
            if(ourCause != null)
            {
                ourCause.printStackTrace(s);
            }
            s.flush();
        }
    }

    @Override
    public void printStackTrace(PrintWriter s)
    {
        synchronized(s)
        {
            s.println(this);

            s.println();
            s.println("\t--------Azolla Exception Properties--------");
            if(errorCode != null)
            {
                s.println("\t" + errorCode + ":" + errorCode.getClass().getName());
            }
            for(Map.Entry<String, Object> entry : properties.entrySet())
            {
                s.println("\t" + entry.getKey() + "=[" + entry.getValue() + "]");
            }
            s.println("\t-------------------------------------------");
            s.println();

            StackTraceElement[] trace = getStackTrace();
            for(int i = 0; i < trace.length; i++)
            {
                s.println("\tat " + trace[i]);
            }

            Throwable ourCause = getCause();
            if(ourCause != null)
            {
                ourCause.printStackTrace(s);
            }
            s.flush();
        }
    }

    public Object get(String key)
    {
        return properties.get(key);
    }

    public AzollaException set(String key, Object value)
    {
        properties.put(key, value);
        return this;
    }

    public ErrorCoder getErrorCode()
    {
        return errorCode = null == errorCode ? AzollaCode.UNAZOLLA : errorCode;
    }

    public void setErrorCode(@Nullable ErrorCoder errorCode)
    {
        this.errorCode = null == errorCode ? AzollaCode.UNAZOLLA : errorCode;
    }

    public Map<String, Object> getProperties()
    {
        return properties;
    }
}
