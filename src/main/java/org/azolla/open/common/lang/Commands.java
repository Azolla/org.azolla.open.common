/*
 * @(#)Commands.java		Created at 2013-7-12
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.lang;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.azolla.open.common.exception.code.AzollaCode;
import org.azolla.open.common.text.Fmts;
import org.azolla.open.common.util.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.io.Closeables;

/**
 * The coder is very lazy, nothing to write for this Commands class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Commands
{
	private static final Logger	LOG						= LoggerFactory.getLogger(Commands.class);

	private static final String	INPUT					= "INPUT";

	private static final String	ERROR					= "ERROR";

	public static final String	CMD_PAUSE_FLAG			= ">pause";

	public static final String	SH_PAUSE_FLAG			= ">read -n 1 -p";

	/**
	 * 33 Minutes
	 */
	public static final long	DEFAULT_TIMEOUT_MINUTES	= 33;

	/**
	 * @see org.azolla.open.common.lang.Commands#exec(String, long)
	 */
	public static boolean exec(String command)
	{
		return exec(command, DEFAULT_TIMEOUT_MINUTES);
	}

	/**
	 * @see org.azolla.open.common.lang.Commands#exec(String, long, boolean, String)
	 */
	public static boolean exec(String command, long timeout)
	{
		return exec(command, timeout, false, null);
	}

	/**
	 * @see org.azolla.open.common.lang.Commands#exec(String, long, boolean, String)
	 */
	public static boolean exec(String command, boolean value4pause, String pauseFlag)
	{
		return exec(command, DEFAULT_TIMEOUT_MINUTES, value4pause, pauseFlag);
	}

	/**
	 * The coder is very lazy, nothing to write for this exec method
	 * 
	 * @param command	command or bat or shell
	 * @param timeout
	 * @param value4pause	the value for meet pause
	 * @param pauseFlag
	 * @return boolean
	 */
	public static boolean exec(String command, long timeout, boolean value4pause, String pauseFlag)
	{
		LOG.info(command);

		boolean rtnBoolean = true;

		//java.lang.NullPointerException
		//java.lang.IllegalArgumentException: Empty command
		if(!Strings.isNullOrEmpty(command))
		{
			Process process = null;
			Future<Boolean> iFuture = null;
			Future<Boolean> eFuture = null;
			try
			{
				process = Runtime.getRuntime().exec(command);
				ExecutorService es = Executors.newFixedThreadPool(2);
				iFuture = es.submit(new CommandTask(process.getInputStream(), INPUT, value4pause, pauseFlag));
				eFuture = es.submit(new CommandTask(process.getErrorStream(), ERROR, value4pause, pauseFlag));
				rtnBoolean = iFuture.get(timeout, TimeUnit.MINUTES) && eFuture.get(timeout, TimeUnit.MINUTES) && process.waitFor() == 0;
			}
			catch(Exception e)
			{
				LOG.error(Fmts.LOG_EC_P_M, AzollaCode.COMMAND_ERROR, KV.new0("command", command).set("timeout", timeout).set("value4pause", value4pause).set("pauseFlag", pauseFlag), e.toString(), e);
				rtnBoolean = false;
			}
			finally
			{
				if(null != iFuture)
				{
					iFuture.cancel(true);
				}
				if(null != eFuture)
				{
					eFuture.cancel(true);
				}
				if(null != process)
				{
					process.destroy();
				}
			}
		}

		return rtnBoolean;
	}

	private static class CommandTask implements Callable<Boolean>
	{
		private static final Logger	LOG	= LoggerFactory.getLogger(CommandTask.class);
		private InputStream			inputStream;
		private String				type;
		private boolean				value4pause;
		private String				pauseFlag;

		//		public CommandTask(InputStream inputStream, String type)
		//		{
		//			this(inputStream, type, false, null);
		//		}

		public CommandTask(InputStream inputStream, String type, boolean value4pause, String pauseFlag)
		{
			super();
			this.inputStream = inputStream;
			this.type = type;
			this.value4pause = value4pause;
			this.pauseFlag = pauseFlag;
		}

		/**
		 * @see java.util.concurrent.Callable#call()
		 * @return
		 * @throws Exception
		 */
		@Override
		public Boolean call()
		{
			boolean rtnBoolean = true;

			LineNumberReader lineNumberReader = null;
			InputStreamReader inputStreamReader = null;
			String line = null;

			try
			{
				inputStreamReader = new InputStreamReader(inputStream);
				lineNumberReader = new LineNumberReader(inputStreamReader);

				if(Strings.isNullOrEmpty(pauseFlag))
				{
					while((line = lineNumberReader.readLine()) != null)
					{
						LOG.info(type + "=" + line);
					}
				}
				else
				{
					while((line = lineNumberReader.readLine()) != null)
					{
						LOG.info(type + "=" + line);
						if(line.toLowerCase(Locale.ENGLISH).indexOf(pauseFlag) > -1)
						{
							rtnBoolean = value4pause;
							break;
						}
					}
				}
			}
			catch(Exception e)
			{
				LOG.error(Fmts.LOG_EC_P_M, AzollaCode.COMMAND_ERROR, KV.new0("type", type).set("value4pause", value4pause).set("pauseFlag", pauseFlag), e.toString(), e);
				rtnBoolean = false;
			}
			finally
			{
				Closeables.closeQuietly(lineNumberReader);
				Closeables.closeQuietly(inputStreamReader);
			}

			return rtnBoolean;
		}
	}
}
