/*
 * @(#)Ftps.java		Created at 2013-7-3
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.azolla.open.common.exception.AzollaException;
import org.azolla.open.common.exception.code.AzollaCode;
import org.azolla.open.common.io.Encoding;
import org.azolla.open.common.text.Fmt0;
import org.azolla.open.common.util.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;

/**
 * Ftps
 * 
 * @see http://blog.csdn.net/cuiran/article/details/7186621
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Ftps
{
	private static final Logger	LOG					= LoggerFactory.getLogger(Ftps.class);

	public static final int		DEFAULT_PORT		= 21;
	public static final int		DEFAULT_FILE_TYPE	= FTPClient.BINARY_FILE_TYPE;
	public static final int		DEFAULT_MODE		= FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE;
	public static final int		DEFAULT_TIMEOUT		= 3000;

	private static Ftps			instance			= null;

	private String				host;
	private String				username;
	private String				password;

	private int					port				= DEFAULT_PORT;
	private int					fileType			= DEFAULT_FILE_TYPE;
	private int					mode				= DEFAULT_MODE;
	private int					timeout				= DEFAULT_TIMEOUT;

	private Encoding			encoding			= Encoding.UTF8;

	private FTPClient			client;

	public static synchronized Ftps ins(String host, String username, String password)
	{
		return ins(host, username, password, DEFAULT_PORT);
	}

	public static synchronized Ftps ins(String host, String username, String password, int port)
	{
		return ins(host, username, password, port, DEFAULT_FILE_TYPE);
	}

	public static synchronized Ftps ins(String host, String username, String password, int port, int fileType)
	{
		return ins(host, username, password, port, fileType, DEFAULT_MODE);
	}

	public static synchronized Ftps ins(String host, String username, String password, int port, int fileType, int mode)
	{
		return ins(host, username, password, port, fileType, mode, DEFAULT_TIMEOUT);
	}

	public static synchronized Ftps ins(String host, String username, String password, int port, int fileType, int mode, int timeout)
	{
		return ins(host, username, password, port, fileType, mode, timeout, Encoding.UTF8);
	}

	public static synchronized Ftps ins(String host, String username, String password, int port, int fileType, int mode, int timeout, Encoding encoding)
	{
		return null == instance ? new Ftps(host, username, password, port, fileType, mode, timeout, encoding) : instance;
	}

	public Ftps(String host, String username, String password)
	{
		this(host, username, password, DEFAULT_PORT);
	}

	public Ftps(String host, String username, String password, int port)
	{
		this(host, username, password, DEFAULT_PORT, DEFAULT_FILE_TYPE);
	}

	public Ftps(String host, String username, String password, int port, int fileType)
	{
		this(host, username, password, port, fileType, DEFAULT_MODE);
	}

	public Ftps(String host, String username, String password, int port, int fileType, int mode)
	{
		this(host, username, password, port, fileType, mode, DEFAULT_TIMEOUT);
	}

	public Ftps(String host, String username, String password, int port, int fileType, int mode, int timeout)
	{
		this(host, username, password, port, fileType, mode, timeout, Encoding.UTF8);
	}

	public Ftps(String host, String username, String password, int port, int fileType, int mode, int timeout, Encoding encoding)
	{
		connect(host, username, password, port, fileType, mode, timeout, encoding);
	}

	private void activeClient()
	{
		disconnect();
		connect(host, username, password, port, fileType, mode, timeout, encoding);
	}

	private void connect(String host, String username, String password, int port, int fileType, int mode, int timeout, Encoding encoding)
	{
		client = new FTPClient();

		//0.encoding
		setEncoding(encoding);

		//1.connect
		try
		{
			client.connect(host, port);

			setHost(host);
			setPort(port);
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_CONNECT_ERROR, KV.ins("host", host).set("port", port), e.toString(), e);
			throw AzollaException.wrap(e, AzollaCode.FTP_CONNECT_ERROR).set("host", host).set("port", port);
		}

		int code = client.getReplyCode();	//get return code after connect
		if(FTPReply.isPositiveCompletion(code))
		{
			//2.login
			boolean logined = true;
			try
			{
				logined = client.login(username, password);	//test username and password
			}
			catch(Exception e)
			{
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LOGIN_ERROR, KV.ins("username", username).set("password", password), e.toString(), e);
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
				LOG.error(Fmt0.LOG_EC_P, AzollaCode.FTP_LOGIN_FAILED, KV.ins("username", username).set("password", password));
				throw new AzollaException(AzollaCode.FTP_LOGIN_FAILED).set("username", username).set("password", password);
			}
		}
		else
		{
			disconnect();
		}

		//3.mode
		setMode(mode);

		//4.fileType
		setFileType(fileType);

		//5.timeout
		setTimeout(timeout);
	}

	public boolean disconnect()
	{
		boolean rtnBoolean = true;
		if(null != client)
		{
			try
			{
				if(client.isConnected())
				{
					client.logout();
				}
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LOGOUT_ERROR, KV.ins("host", host), e.toString(), e);
			}

			if(client.isConnected())
			{
				try
				{
					client.disconnect();
				}
				catch(Exception e)
				{
					rtnBoolean = false;
					LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_DISCONNECT_ERROR, KV.ins("host", host), e.toString(), e);
				}
			}
		}

		return rtnBoolean;
	}

	/**
	 * delete file from ftp
	 * 
	 * @param remotePath
	 * @return boolean
	 */
	public boolean deleteFile(String remotePath)
	{
		boolean rtnBoolean = true;

		if(!Strings.isNullOrEmpty(remotePath))
		{
			try
			{
				activeClient();

				rtnBoolean = client.deleteFile(remotePath);
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_DELETE_FILE_ERROR, KV.ins("remotePath", remotePath), e.toString(), e);
			}
			finally
			{
				disconnect();
			}
		}
		return rtnBoolean;
	}

	/**
	 * delete files from ftp
	 * 
	 * @param remotePathList
	 * @return boolean
	 */
	public boolean deleteFile(List<String> remotePathList)
	{
		boolean rtnBoolean = true;

		if(null != remotePathList)
		{
			try
			{
				activeClient();

				for(String pathname : remotePathList)
				{
					rtnBoolean = rtnBoolean && client.deleteFile(pathname);
				}
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_DELETE_FILE_ERROR, KV.ins("remotePathList", Joiner.on("|").join(remotePathList)), e.toString(), e);
			}
			finally
			{
				disconnect();
			}
		}
		return rtnBoolean;
	}

	/**
	 * download file to output stream
	 * 
	 * @param remotePath
	 * @param output
	 * @return boolean
	 */
	public boolean retrieveFile(String remotePath, OutputStream output)
	{
		boolean rtnBoolean = true;

		if(!Strings.isNullOrEmpty(remotePath) && null != output)
		{
			try
			{
				activeClient();

				rtnBoolean = client.retrieveFile(remotePath, output);
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_RETRIEVE_FILE_ERROR, KV.ins("remotePath", remotePath), e.toString(), e);
			}
			finally
			{
				disconnect();
			}
		}
		return rtnBoolean;

	}

	/**
	 * download file to local
	 * 
	 * @param remotePath
	 * @param localPath
	 * @return boolean
	 */
	public boolean retrieveFile(String remotePath, String localPath)
	{
		boolean rtnBoolean = true;

		if(!Strings.isNullOrEmpty(remotePath) && !Strings.isNullOrEmpty(localPath))
		{
			OutputStream output = null;
			try
			{
				activeClient();

				output = new FileOutputStream(localPath);
				rtnBoolean = client.retrieveFile(remotePath, output);
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_RETRIEVE_FILE_ERROR, KV.ins("remotePath", remotePath).set("localPath", localPath), e.toString(), e);
			}
			finally
			{
				Closeables.closeQuietly(output);
				disconnect();
			}
		}
		return rtnBoolean;

	}

	/**
	 * file is exist
	 * 
	 * @param remotePath
	 * @return boolean
	 */
	public boolean exist(String remotePath)
	{
		boolean rtnBoolean = false;

		if(!Strings.isNullOrEmpty(remotePath))
		{
			try
			{
				activeClient();

				File file = new File(remotePath);
				String remoteFolderPath = remotePath.substring(0, (remotePath.indexOf(file.getName()) - 1));
				String[] pathArray = client.listNames(remoteFolderPath);
				if(null != pathArray)
				{
					List<String> pathList = Lists.newArrayList(pathArray);
					rtnBoolean = pathList.contains(remotePath);
				}
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e.toString(), e);
			}
			finally
			{
				disconnect();
			}
		}
		return rtnBoolean;
	}

	/**
	 * @see org.azolla.open.common.ftp.Ftps#listNames(String)
	 */
	public List<String> listNames()
	{
		return listNames(null);
	}

	/**
	 * list of remotePath
	 * 
	 * @param remotePath
	 * @return List<String>
	 */
	public List<String> listNames(String remotePath)
	{
		List<String> rtnList = Lists.newArrayList();

		try
		{
			activeClient();

			String[] pathArray = client.listNames(remotePath);
			if(null != pathArray)
			{
				rtnList = Lists.newArrayList(pathArray);
			}
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e.toString(), e);
		}
		finally
		{
			disconnect();
		}

		return rtnList;
	}

	public List<String> allNames()
	{
		return allNames(null);
	}

	public List<String> allNames(String remotePath)
	{
		return allNames(remotePath, null);
	}

	public List<String> allNames(String remotePath, FTPFileFilter filter)
	{
		List<String> rtnList = Lists.newArrayList();

		try
		{
			activeClient();

			FTPFile[] pathArray = null == filter ? client.listFiles(remotePath) : client.listFiles(remotePath, filter);
			if(null != pathArray)
			{
				String filePrefix = (null == remotePath) ? "" : (remotePath.startsWith("/") ? remotePath : "/" + remotePath);
				String folderPrefix = filePrefix.endsWith("/") ? filePrefix : filePrefix + "/";
				for(FTPFile f : pathArray)
				{
					if(f.isDirectory())
					{
						rtnList.addAll(allNames(folderPrefix + f.getName(), filter));
					}
					else
					{
						if(pathArray.length > 1)
						{
							rtnList.add(folderPrefix + f.getName());
						}
						else
						{
							int index = filePrefix.lastIndexOf("/");
							rtnList.add(f.getName().equals(-1 == index ? filePrefix : filePrefix.substring(index + 1)) ? filePrefix : filePrefix + "/" + f.getName());
						}

					}
				}
			}
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e.toString(), e);
		}
		finally
		{
			disconnect();
		}

		return rtnList;
	}

	/**
	 * @see org.azolla.open.common.ftp.Ftps#listFiles(String)
	 */
	public List<FTPFile> listFiles()
	{
		return listFiles(null);
	}

	/**
	 * @see org.azolla.open.common.ftp.Ftps#listFiles(String,FTPFileFilter)
	 */
	public List<FTPFile> listFiles(String remotePath)
	{
		return listFiles(remotePath, null);
	}

	/**
	 * list of remotePath
	 * 
	 * @param remotePath
	 * @return List<FTPFile>
	 */
	public List<FTPFile> listFiles(String remotePath, FTPFileFilter filter)
	{
		List<FTPFile> rtnList = Lists.newArrayList();

		try
		{
			activeClient();

			FTPFile[] pathArray = null == filter ? client.listFiles(remotePath) : client.listFiles(remotePath, filter);
			if(null != pathArray)
			{
				rtnList = Lists.newArrayList(pathArray);
			}
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e.toString(), e);
		}
		finally
		{
			disconnect();
		}

		return rtnList;
	}

	public boolean storeFile(String remotePath, File localFile)
	{
		return null == localFile ? false : storeFile(remotePath, localFile.getAbsolutePath());
	}

	/**
	 * upload file to ftp
	 * 
	 * @param remotePath
	 * @param localPath
	 * @return boolean
	 */
	public boolean storeFile(String remotePath, String localPath)
	{
		boolean rtnBoolean = true;

		if(!Strings.isNullOrEmpty(remotePath) && !Strings.isNullOrEmpty(localPath))
		{
			InputStream input = null;
			try
			{
				activeClient();

				input = new FileInputStream(localPath);
				rtnBoolean = client.storeFile(remotePath, input);
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_STORE_FILE_ERROR, KV.ins("remotePath", remotePath).set("localPath", localPath), e.toString(), e);
			}
			finally
			{
				Closeables.closeQuietly(input);
				disconnect();
			}
		}

		return rtnBoolean;
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
	public Ftps setFileType(int fileType)
	{
		try
		{
			client.setFileType(fileType);
			this.fileType = fileType;
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_FILETYPE_ERROR, KV.ins("fileType", fileType), e.toString(), e);
			throw AzollaException.wrap(e, AzollaCode.FTP_SET_FILETYPE_ERROR).set("fileType", fileType);
		}
		return this;
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
	public Ftps setMode(int mode)
	{
		boolean successed = true;
		switch(mode)
		{
			case FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE :
				client.enterLocalPassiveMode();
				break;
			case FTPClient.ACTIVE_LOCAL_DATA_CONNECTION_MODE :
				client.enterLocalActiveMode();
				break;
			case FTPClient.PASSIVE_REMOTE_DATA_CONNECTION_MODE :
				try
				{
					successed = client.enterRemotePassiveMode();
				}
				catch(Exception e)
				{
					successed = false;
					LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_MODE_ERROR, KV.ins("mode", mode), e.toString(), e);
					throw AzollaException.wrap(e, AzollaCode.FTP_SET_MODE_ERROR).set("mode", mode);
				}
				break;
			case FTPClient.ACTIVE_REMOTE_DATA_CONNECTION_MODE :
				try
				{
					successed = client.enterRemoteActiveMode(InetAddress.getByName(host), port);
				}
				catch(Exception e)
				{
					successed = false;
					LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_MODE_ERROR, KV.ins("mode", mode), e.toString(), e);
					throw AzollaException.wrap(e, AzollaCode.FTP_SET_MODE_ERROR).set("mode", mode);
				}
				break;
			default :
				throw new AzollaException(AzollaCode.FTP_SET_MODE_FAILED).set("mode", mode);
		}

		if(successed)
		{
			this.mode = mode;
		}
		return this;
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
	public Ftps setEncoding(Encoding encoding)
	{
		Encoding e = null == encoding ? Encoding.UTF8 : encoding;
		client.setControlEncoding(e.getEncoding());
		this.encoding = e;
		return this;
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
	public Ftps setTimeout(int timeout)
	{
		try
		{
			client.setSoTimeout(timeout);
			this.timeout = timeout;
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_TIMEOUT_ERROR, KV.ins("timeout", timeout), e.toString(), e);
			throw AzollaException.wrap(e, AzollaCode.FTP_SET_TIMEOUT_ERROR).set("timeout", timeout);
		}
		return this;
	}

	/**
	 * this is a getter method for client
	 *
	 * @return the client
	 */
	public FTPClient getClient()
	{
		return client;
	}

}
