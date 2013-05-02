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
import java.util.TreeMap;

import org.azolla.open.common.exception.code.AzollaCode;
import org.azolla.open.common.exception.code.ErrorCoder;

/**
 * The exception for Azolla
 * 
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class AzollaException extends RuntimeException
{
	private static final long			serialVersionUID	= 6975777768178330101L;

	private ErrorCoder					code;
	private final Map<String, Object>	properties			= new TreeMap<String, Object>();

	/**
	 * Constructs a AzollaException with the specified detail code. 
	 */
	public AzollaException(ErrorCoder code)
	{
		setCode(code);
	}

	/**
	 * Constructs a AzollaException with the specified detail code and message. 
	 */
	public AzollaException(ErrorCoder code, String message)
	{
		super(message);
		setCode(code);
	}

	/**
	 * Constructs a AzollaException with the specified detail code and cause.
	 */
	public AzollaException(ErrorCoder code, Throwable cause)
	{
		super(cause);
		setCode(code);
	}

	/**
	 * Constructs a AzollaException with the specified detail code, message and cause.
	 */
	public AzollaException(ErrorCoder code, String message, Throwable cause)
	{
		super(message, cause);
		setCode(code);
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
	 * @param code
	 * @return AzollaException
	 */
	public static AzollaException wrap(Throwable cause, ErrorCoder code)
	{
		ErrorCoder ec = null == code ? AzollaCode.UNAZOLLA : code;
		if(null == cause)
		{
			//			if(null == code)
			//			{
			//				return new AzollaException(AzollaCode.UNAZOLLA);
			//			}
			//			else
			//			{
			//				return new AzollaException(code);
			//			}
			return new AzollaException(ec);
		}
		else if(cause instanceof AzollaException)
		{
			AzollaException se = (AzollaException) cause;

			if(code != null && code != se.getCode())
			{
				//				return new AzollaException(code, cause.getMessage(), cause);
				se.setCode(code);
			}

			return se;
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

			s.println("\t--------Azolla Exception Properties--------");
			if(code != null)
			{
				s.println("\t" + code + ":" + code.getClass().getName());
			}
			for(String key : properties.keySet())
			{
				s.println("\t" + key + "=[" + properties.get(key) + "]");
			}
			s.println("\t-------------------------------------------");

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

			s.println("\t--------Azolla Exception Properties--------");
			if(code != null)
			{
				s.println("\t" + code + ":" + code.getClass().getName());
			}
			for(String key : properties.keySet())
			{
				s.println("\t" + key + "=[" + properties.get(key) + "]");
			}
			s.println("\t-------------------------------------------");

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
	 * this is a getter method for code
	 *
	 * @return the code
	 */
	public ErrorCoder getCode()
	{
		if(null == code)
		{
			code = AzollaCode.UNAZOLLA;
		}
		return code;
	}

	/**
	 * this is a setter method for code
	 *
	 * @param code the code to set
	 */
	public void setCode(ErrorCoder code)
	{
		this.code = null == code ? AzollaCode.UNAZOLLA : code;
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
