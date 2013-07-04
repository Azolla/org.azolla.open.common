/*
 * @(#)Ftps.java		Created at 2013-7-3
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.azolla.open.common.exception.AzollaException;
import org.azolla.open.common.exception.code.AzollaCode;
import org.azolla.open.common.io.Encoding;
import org.azolla.open.common.text.Fmts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The coder is very lazy, nothing to write for this Ftps class
 * 
 * @see http://blog.csdn.net/cuiran/article/details/7186621
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class FtpHelper
{
	private static final Logger	LOG			= LoggerFactory.getLogger(FtpHelper.class);

	private static FtpHelper	instance	= null;

	private String				host;
	private String				username;
	private String				password;

	private int					port		= 21;
	private int					fileType	= FTPClient.BINARY_FILE_TYPE;
	private int					mode		= FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE;
	private Encoding			encoding	= Encoding.UTF8;
	private int					timeout		= 3000;

	private FTPClient			client;

	public FtpHelper(String host, String username, String password)
	{

	}

	public FtpHelper(String host, String username, String password, int port, int fileType)
	{
		client = new FTPClient();

		try
		{
			client.connect(host, port);

			setHost(host);
			setPort(port);
		}
		catch(Exception e)
		{
			//TODO
			LOG.error(Fmts.LOG_WITH_KEY_FMT, e.toString(), AzollaCode.FTP_CONNECT_ERROR);
			throw AzollaException.wrap(e, AzollaCode.FTP_CONNECT_ERROR).set("host", host).set("port", port);
		}

		int code = client.getReplyCode();	//get return code after connect
		if(FTPReply.isPositiveCompletion(code))
		{
			boolean logined = true;
			try
			{
				logined = client.login(username, password);	//test username and password
			}
			catch(IOException e)
			{
				throw AzollaException.wrap(e, AzollaCode.FTP_LOGIN_ERROR).set("username", username).set("password", password);
			}

			if(logined)
			{
				setUsername(username);
				setPassword(password);
				setFileType(fileType);
			}
			else
			{
				throw new AzollaException(AzollaCode.FTP_LOGIN_FAILED).set("username", username).set("password", password);
			}
		}
	}

	public static synchronized FtpHelper ins(String host, String username, String password)
	{
		return null == instance ? new FtpHelper(host, username, password) : instance;
	}

	/**
	 * this is a getter method for host
	 *
	 * @return the host
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * this is a setter method for host
	 *
	 * @param host the host to set
	 */
	public void setHost(String host)
	{
		this.host = host;
	}

	/**
	 * this is a getter method for username
	 *
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * this is a setter method for username
	 *
	 * @param username the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * this is a getter method for password
	 *
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * this is a setter method for password
	 *
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * this is a getter method for port
	 *
	 * @return the port
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * this is a setter method for port
	 *
	 * @param port the port to set
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * this is a getter method for fileType
	 *
	 * @return the fileType
	 */
	public int getFileType()
	{
		return fileType;
	}

	/**
	 * this is a setter method for fileType
	 *
	 * @param fileType the fileType to set
	 */
	public void setFileType(int fileType)
	{
		try
		{
			client.setFileType(fileType);
			this.fileType = fileType;
		}
		catch(IOException e)
		{
			throw AzollaException.wrap(e, AzollaCode.FTP_SET_FILETYPE_ERROR).set("original fileType", this.fileType).set("new fileType", fileType);
		}
	}

	/**
	 * this is a getter method for mode
	 *
	 * @return the mode
	 */
	public int getMode()
	{
		return mode;
	}

	/**
	 * this is a setter method for mode
	 *
	 * @param mode the mode to set
	 */
	public void setMode(int mode)
	{
		this.mode = mode;
	}

	/**
	 * this is a getter method for encoding
	 *
	 * @return the encoding
	 */
	public Encoding getEncoding()
	{
		return encoding;
	}

	/**
	 * this is a setter method for encoding
	 *
	 * @param encoding the encoding to set
	 */
	public void setEncoding(Encoding encoding)
	{
		this.encoding = encoding;
	}

	/**
	 * this is a getter method for timeout
	 *
	 * @return the timeout
	 */
	public int getTimeout()
	{
		return timeout;
	}

	/**
	 * this is a setter method for timeout
	 *
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}
}
