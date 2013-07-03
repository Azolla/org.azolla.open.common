/*
 * @(#)Ftps.java		Created at 2013-7-3
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.io;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
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

	public FtpHelper(String host, String username, String password, int port, int fileType) throws SocketException, IOException
	{
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
		this.fileType = fileType;

		client = new FTPClient();

		client.connect(host, port);
		int code = client.getReplyCode();	//get return code after connect
		if(FTPReply.isPositiveCompletion(code))
		{
			if(client.login(username, password))	//test username and password
			{
				client.setFileType(fileType);
			}
		}
	}

	public static synchronized FtpHelper ins(String host, String username, String password)
	{
		return null == instance ? new FtpHelper(host, username, password) : instance;
	}
}
