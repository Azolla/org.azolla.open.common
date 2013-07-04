/*
 * @(#)AzollaException.java		Created at 2013-2-23
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;

import org.azolla.open.common.exception.code.AzollaCode;
import org.azolla.open.common.exception.code.ErrorCoder;

import com.google.common.collect.Maps;

/**
 * The exception for Azolla
 * 
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class AzollaException extends RuntimeException
{
	private static final long			serialVersionUID	= 6975777768178330101L;

	private ErrorCoder					errorCode;
	private final Map<String, Object>	properties			= Maps.newTreeMap();	//sort

	/**
	 * Constructs a AzollaException with the specified detail errorCode. 
	 */
	public AzollaException(ErrorCoder errorCode)
	{
		setErrorCode(errorCode);
	}

	/**
	 * Constructs a AzollaException with the specified detail errorCode and message. 
	 */
	public AzollaException(ErrorCoder errorCode, String message)
	{
		super(message);
		setErrorCode(errorCode);
	}

	/**
	 * Constructs a AzollaException with the specified detail errorCode and cause.
	 */
	public AzollaException(ErrorCoder errorCode, Throwable cause)
	{
		super(cause);
		setErrorCode(errorCode);
	}

	/**
	 * Constructs a AzollaException with the specified detail errorCode, message and cause.
	 */
	public AzollaException(ErrorCoder errorCode, String message, Throwable cause)
	{
		super(message, cause);
		setErrorCode(errorCode);
	}

	/**
	 * 
	 * The coder is very lazy, nothing to write for this wrap method
	 * 
	 * @param cause
	 * @return AzollaException
	 */
	public static AzollaException wrap(Throwable cause)
	{
		return wrap(cause, null);
	}

	/**
	 * 
	 * The coder is very lazy, nothing to write for this wrap method
	 * 
	 * @param cause
	 * @param errorCode
	 * @return AzollaException
	 */
	public static AzollaException wrap(Throwable cause, ErrorCoder errorCode)
	{
		ErrorCoder ec = null == errorCode ? AzollaCode.UNAZOLLA : errorCode;
		if(null == cause)
		{
			return new AzollaException(ec);
		}
		else if(cause instanceof AzollaException)
		{
			AzollaException se = (AzollaException) cause;
			ErrorCoder currentErrorCoder = se.getErrorCode();

			//keep history
			//se.set(currentErrorCoder.getClass().getName() + "." + currentErrorCoder.toString(), currentErrorCoder.getCode());
			//se.setErrorCode(errorCode);
			return (errorCode != null && errorCode != currentErrorCoder) ? new AzollaException(errorCode, cause.getMessage(), cause) : se;
		}
		return new AzollaException(ec, cause.getMessage(), cause);
	}

	/**
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 * @param s
	 */
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

	/**
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
	 * @param s
	 */
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

	/**
	 * this is a getter method for errorCode
	 *
	 * @return the errorCode
	 */
	public ErrorCoder getErrorCode()
	{
		if(null == errorCode)
		{
			errorCode = AzollaCode.UNAZOLLA;
		}
		return errorCode;
	}

	/**
	 * this is a setter method for errorCode
	 *
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(ErrorCoder errorCode)
	{
		this.errorCode = null == errorCode ? AzollaCode.UNAZOLLA : errorCode;
	}

	/**
	 * this is a getter method for properties
	 *
	 * @return the properties
	 */
	public Map<String, Object> getProperties()
	{
		return properties;
	}
}
