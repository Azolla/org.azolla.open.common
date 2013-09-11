/*
 * @(#)Ftp0.java		Created at 2013-7-3
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.azolla.open.ling.exception.AzollaException;
import org.azolla.open.ling.exception.code.AzollaCode;
import org.azolla.open.ling.io.Encoding;
import org.azolla.open.ling.io.File0;
import org.azolla.open.ling.text.Fmt0;
import org.azolla.open.ling.util.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;

/**
 * Ftp0
 * 
 * @see http://blog.csdn.net/cuiran/article/details/7186621
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Ftp0
{
	private static final Logger	LOG					= LoggerFactory.getLogger(Ftp0.class);

	public static final int		DEFAULT_PORT		= 21;
	public static final int		DEFAULT_FILE_TYPE	= FTPClient.BINARY_FILE_TYPE;
	public static final int		DEFAULT_MODE		= FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE;
	public static final int		DEFAULT_TIMEOUT		= 3000;
	public static final int		DEFAULT_BUFFER_SIZE	= 100000;

	private static Ftp0			instance			= null;

	private String				host;
	private String				username;
	private String				password;

	private int					port				= DEFAULT_PORT;
	private int					fileType			= DEFAULT_FILE_TYPE;
	private int					mode				= DEFAULT_MODE;
	private int					timeout				= DEFAULT_TIMEOUT;
	private int					bufferSize			= DEFAULT_BUFFER_SIZE;

	private String				encoding			= Encoding.UTF8;

	private FTPClient			client;

	public static synchronized Ftp0 single(String host, String username, String password)
	{
		return single(host, username, password, DEFAULT_PORT);
	}

	public static synchronized Ftp0 single(String host, String username, String password, int port)
	{
		return single(host, username, password, port, DEFAULT_FILE_TYPE);
	}

	public static synchronized Ftp0 single(String host, String username, String password, int port, int fileType)
	{
		return single(host, username, password, port, fileType, DEFAULT_MODE);
	}

	public static synchronized Ftp0 single(String host, String username, String password, int port, int fileType, int mode)
	{
		return single(host, username, password, port, fileType, mode, DEFAULT_TIMEOUT);
	}

	public static synchronized Ftp0 single(String host, String username, String password, int port, int fileType, int mode, int timeout)
	{
		return single(host, username, password, port, fileType, mode, timeout, null);
	}

	public static synchronized Ftp0 single(String host, String username, String password, int port, int fileType, int mode, int timeout, @Nullable String encoding)
	{
		return instance == null ? new Ftp0(host, username, password, port, fileType, mode, timeout, encoding) : instance;
	}

	public Ftp0(String host, String username, String password)
	{
		this(host, username, password, DEFAULT_PORT);
	}

	public Ftp0(String host, String username, String password, int port)
	{
		this(host, username, password, DEFAULT_PORT, DEFAULT_FILE_TYPE);
	}

	public Ftp0(String host, String username, String password, int port, int fileType)
	{
		this(host, username, password, port, fileType, DEFAULT_MODE);
	}

	public Ftp0(String host, String username, String password, int port, int fileType, int mode)
	{
		this(host, username, password, port, fileType, mode, DEFAULT_TIMEOUT);
	}

	public Ftp0(String host, String username, String password, int port, int fileType, int mode, int timeout)
	{
		this(host, username, password, port, fileType, mode, timeout, null);
	}

	public Ftp0(String host, String username, String password, int port, int fileType, int mode, int timeout, @Nullable String encoding)
	{
		connect(host, username, password, port, fileType, mode, timeout, encoding);
	}

	private FTPClient activeClient()
	{
		disconnect();
		connect(host, username, password, port, fileType, mode, timeout, encoding);
		return client;
	}

	private void connect(String host, String username, String password, int port, int fileType, int mode, int timeout, @Nullable String encoding)
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
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_CONNECT_ERROR, KV.ins("host", host).put("port", port), e);
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
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LOGIN_ERROR, KV.ins("username", username).put("password", password), e);
				throw AzollaException.wrap(e, AzollaCode.FTP_LOGIN_ERROR).set("username", username).set("password", password);
			}

			if(logined)
			{
				setUsername(username);
				setPassword(password);

				setFileType(fileType);
				setBufferSize(bufferSize);
			}
			else
			{
				LOG.error(Fmt0.LOG_EC_P, AzollaCode.FTP_LOGIN_FAILED, KV.ins("username", username).put("password", password));
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
		if(client != null)
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
				LOG.warn(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LOGOUT_ERROR, KV.ins("host", host), e);
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
					LOG.warn(Fmt0.LOG_EC_P_M, AzollaCode.FTP_DISCONNECT_ERROR, KV.ins("host", host), e);
				}
			}
		}

		return rtnBoolean;
	}

	/**
	 * @see org.azolla.open.ling.ftp.Ftp0#deleteFile(FTPClient, String)
	 */
	public boolean deleteFile(String remotePath)
	{
		return deleteFile(null, remotePath);
	}

	/**
	 * 
	 * Delete file(document or directory) from ftp
	 * 
	 * @param client
	 * @param remotePath
	 * @return boolean
	 */
	public boolean deleteFile(FTPClient client, String remotePath)
	{
		boolean rtnBoolean = false;

		//If remotePath is null or empty will be very dangerous
		if(!Strings.isNullOrEmpty(remotePath))
		{
			try
			{
				client = client == null ? activeClient() : client;

				if(isDirectory(client, remotePath))
				{
					rtnBoolean = deleteFile0(client, remotePath);
				}
				else
				{
					rtnBoolean = client.deleteFile(remotePath);
				}
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_DELETE_FILE_ERROR, KV.ins("remotePath", remotePath), e);
			}
		}

		return rtnBoolean;
	}

	/**
	 * 
	 * @param client available client
	 * @param remotePath exsited path
	 * @return boolean
	 */
	private boolean deleteFile0(FTPClient client, String remotePath)
	{
		boolean rtnBoolean = true;

		try
		{
			for(FTPFile file : client.listFiles(remotePath))
			{
				String prefix = remotePath + (remotePath.endsWith("/") ? "" : "/");
				if(file.isDirectory())
				{
					rtnBoolean = rtnBoolean && deleteFile0(client, prefix + file.getName());
				}
				rtnBoolean = rtnBoolean && client.deleteFile(prefix + file.getName());
			}
			rtnBoolean = rtnBoolean && client.deleteFile(remotePath);
		}
		catch(IOException e)
		{
			rtnBoolean = false;
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_DELETE_FILE_ERROR, KV.ins("remotePath", remotePath), e);
		}

		return rtnBoolean;
	}

	/**
	 * @see org.azolla.open.ling.ftp.Ftp0#retrieveFile(FTPClient, String, File)
	 */
	public boolean retrieveFile(String remotePath, File localFile)
	{
		return retrieveFile(null, remotePath, localFile);
	}

	/**
	 * 
	 * download file to local
	 * 
	 * @param client
	 * @param remotePath
	 * @param localPath
	 * @return boolean
	 */
	public boolean retrieveFile(FTPClient client, String remotePath, File localFile)
	{
		boolean rtnBoolean = false;

		//If remotePath is null or empty will be very dangerous
		if(!Strings.isNullOrEmpty(remotePath) && localFile != null)
		{
			try
			{
				client = client == null ? activeClient() : client;

				if((isDirectory(client, remotePath) && localFile.isDirectory()) || (!isDirectory(client, remotePath) && !localFile.isDirectory()))
				{
					rtnBoolean = retrieveFile0(client, remotePath, localFile);
				}
				else if(isExist(client, remotePath) && !isDirectory(client, remotePath) && localFile.isDirectory())
				{
					File file = File0.newFile(localFile, listFiles(client, remotePath).get(0).getName());
					rtnBoolean = file.createNewFile() && retrieveFile0(client, remotePath, file);
				}
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_RETRIEVE_FILE_ERROR, KV.ins("remotePath", remotePath).put("localFile", localFile), e);
			}
		}
		return rtnBoolean;
	}

	/**
	 * both directory or both document
	 * 
	 * @param client available client
	 * @param remotePath exsited path
	 * @param localFile exsited file
	 * @return boolean
	 */
	private boolean retrieveFile0(FTPClient client, String remotePath, File localFile)
	{
		boolean rtnBoolean = true;

		BufferedOutputStream bos = null;
		OutputStream os = null;
		try
		{
			if(isDirectory(client, remotePath) && localFile.isDirectory())
			{
				String prefix = remotePath + (remotePath.endsWith("/") ? "" : "/");
				for(FTPFile file : client.listFiles(remotePath))
				{
					String newPath = prefix + file.getName();
					File newFile = File0.newFile(localFile, file.getName());
					rtnBoolean = rtnBoolean && file.isDirectory() ? newFile.mkdirs() : newFile.createNewFile();
					rtnBoolean = rtnBoolean && retrieveFile0(client, newPath, newFile);
				}
			}
			else
			{
				os = new FileOutputStream(localFile);
				bos = new BufferedOutputStream(os);
				rtnBoolean = rtnBoolean && client.retrieveFile(remotePath, bos);
			}

		}
		catch(Exception e)
		{
			rtnBoolean = false;
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_RETRIEVE_FILE_ERROR, KV.ins("remotePath", remotePath).put("localFile", localFile), e);
		}
		finally
		{
			Closeables.closeQuietly(os);
			Closeables.closeQuietly(bos);
		}

		return rtnBoolean;
	}

	/**
	 * @see org.azolla.open.ling.ftp.Ftp0#isExist(FTPClient, String)
	 */
	public boolean isExist(String remotePath)
	{
		return isExist(null, remotePath);
	}

	/**
	 * is file existed
	 * 
	 * @param client
	 * @param remotePath
	 * @return boolean
	 */
	public boolean isExist(FTPClient client, String remotePath)
	{
		boolean rtnBoolean = false;

		if(remotePath != null)
		{
			try
			{
				client = client == null ? activeClient() : client;

				String[] pathArray = client.listNames(remotePath);
				if(pathArray != null && pathArray.length > 0)
				{
					rtnBoolean = true;
				}
				else
				{
					//1.empty root:"/"
					if(Strings.isNullOrEmpty(remotePath) && "/".equalsIgnoreCase(remotePath))
					{
						rtnBoolean = true;
					}
					else
					{
						//2.empty folder:"/folder","/folder/","folder","folder/","folder/subfolder"
						remotePath = (remotePath.startsWith("/") ? "" : "/") + remotePath;
						remotePath = remotePath + (remotePath.endsWith("/") ? "" : "/");
						String[] nameArray = remotePath.split("/");
						FTPFile[] array = client.listFiles("/" + Joiner.on("/").join(Arrays.copyOfRange(nameArray, 1, nameArray.length - 1)));
						if(array != null && array.length > 0)
						{
							for(FTPFile file : array)
							{
								if(file.getName().equals(nameArray[nameArray.length - 1]))
								{
									rtnBoolean = true;
									break;
								}
							}
						}
					}
				}
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e);
			}
		}
		return rtnBoolean;
	}

	public boolean isDirectory(String remotePath)
	{
		return isDirectory(null, remotePath);
	}

	public boolean isDirectory(FTPClient client, String remotePath)
	{
		boolean rtnBoolean = false;

		if(remotePath != null)
		{
			try
			{
				client = client == null ? activeClient() : client;

				if(isExist(client, remotePath))
				{
					//1.empty root:"/"
					if(Strings.isNullOrEmpty(remotePath) && "/".equalsIgnoreCase(remotePath))
					{
						rtnBoolean = true;
					}
					else
					{
						//2.empty folder:"/folder","/folder/","folder","folder/","folder/subfolder"
						remotePath = (remotePath.startsWith("/") ? "" : "/") + remotePath;
						remotePath = remotePath + (remotePath.endsWith("/") ? "" : "/");
						String[] nameArray = remotePath.split("/");
						FTPFile[] array = client.listFiles("/" + Joiner.on("/").join(Arrays.copyOfRange(nameArray, 1, nameArray.length - 1)));
						if(array != null && array.length > 0)
						{
							for(FTPFile file : array)
							{
								if(file.isDirectory() && file.getName().equals(nameArray[nameArray.length - 1]))
								{
									rtnBoolean = true;
									break;
								}
							}
						}
					}
				}
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e);
			}
		}
		return rtnBoolean;
	}

	public List<String> listNames()
	{
		return listNames(null);
	}

	/**
	 * @see org.azolla.open.ling.ftp.Ftp0#listNames(FTPClient, String)
	 */
	public List<String> listNames(@Nullable String remotePath)
	{
		return listNames(null, remotePath);
	}

	/**
	 * List of fullpath under this folder, if remotePath is document return it
	 * 
	 * @param client
	 * @param remotePath
	 * @return List<String>	like "/test/ftp.test.txt"
	 */
	public List<String> listNames(FTPClient client, @Nullable String remotePath)
	{
		List<String> rtnList = Lists.newArrayList();

		try
		{
			client = client == null ? activeClient() : client;

			String[] pathArray = client.listNames(remotePath);
			rtnList = pathArray == null ? rtnList : Lists.newArrayList(pathArray);
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e);
		}

		return rtnList;
	}

	public List<String> allNames()
	{
		return allNames(null);
	}

	public List<String> allNames(@Nullable String remotePath)
	{
		return allNames(remotePath, null);
	}

	public List<String> allNames(FTPClient client, @Nullable String remotePath)
	{
		return allNames(client, remotePath, null);
	}

	public List<String> allNames(@Nullable String remotePath, @Nullable FTPFileFilter filter)
	{
		return allNames(null, remotePath, filter);
	}

	/**
	 * All of fullpath under this folder, if remotePath is document return it
	 * 
	 * @param client
	 * @param remotePath 
	 * @param filter
	 * @return List<String>
	 */
	public List<String> allNames(FTPClient client, @Nullable String remotePath, @Nullable FTPFileFilter filter)
	{
		List<String> rtnList = Lists.newArrayList();

		try
		{
			client = client == null ? activeClient() : client;

			FTPFile[] pathArray = filter == null ? client.listFiles(remotePath) : client.listFiles(remotePath, filter);
			if(pathArray != null && pathArray.length > 0)
			{
				String filePrefix = Strings.isNullOrEmpty(remotePath) ? "" : (remotePath.startsWith("/") ? remotePath : "/" + remotePath);
				String folderPrefix = filePrefix.endsWith("/") ? filePrefix : filePrefix + "/";
				for(FTPFile f : pathArray)
				{
					if(f.isDirectory())
					{
						rtnList.addAll(allNames(folderPrefix + f.getName(), filter));
					}
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
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e);
		}

		return rtnList;
	}

	public List<FTPFile> listFiles()
	{
		return listFiles(null);
	}

	public List<FTPFile> listFiles(@Nullable String remotePath)
	{
		return listFiles(remotePath, null);
	}

	public List<FTPFile> listFiles(FTPClient client, @Nullable String remotePath)
	{
		return listFiles(client, remotePath, null);
	}

	public List<FTPFile> listFiles(@Nullable String remotePath, @Nullable FTPFileFilter filter)
	{
		return listFiles(null, remotePath, filter);
	}

	/**
	 * List of FTPFile under this folder, if remotePath is document return it
	 * 
	 * @param client
	 * @param remotePath If remotePath is document return it
	 * @param filter
	 * @return List<FTPFile>
	 */
	public List<FTPFile> listFiles(FTPClient client, @Nullable String remotePath, @Nullable FTPFileFilter filter)
	{
		List<FTPFile> rtnList = Lists.newArrayList();

		try
		{
			client = client == null ? activeClient() : client;

			FTPFile[] pathArray = filter == null ? client.listFiles(remotePath) : client.listFiles(remotePath, filter);
			rtnList = pathArray == null ? rtnList : Lists.newArrayList(pathArray);
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e);
		}

		return rtnList;
	}

	public boolean storeFile(String remotePath, File localFile)
	{
		return storeFile(null, remotePath, localFile);
	}

	/**
	 * upload file to ftp
	 * 
	 * @param client
	 * @param remotePath
	 * @param localPath
	 * @return boolean
	 */
	public boolean storeFile(FTPClient client, String remotePath, File localFile)
	{
		boolean rtnBoolean = false;

		//If remotePath is null or empty will be very dangerous
		if(!Strings.isNullOrEmpty(remotePath) && localFile != null)
		{
			try
			{
				client = client == null ? activeClient() : client;

				if((isDirectory(client, remotePath) && localFile.isDirectory()) || (!isDirectory(client, remotePath) && !localFile.isDirectory()))
				{
					rtnBoolean = storeFile0(client, remotePath, localFile);
				}
				else if(isDirectory(client, remotePath) && localFile.exists() && !localFile.isDirectory())
				{
					String prefix = remotePath + (remotePath.endsWith("/") ? "" : "/");
					rtnBoolean = storeFile0(client, prefix + localFile.getName(), localFile);
				}
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_STORE_FILE_ERROR, KV.ins("remotePath", remotePath).put("localPath", localFile), e);
			}
		}

		return rtnBoolean;
	}

	/**
	 * both directory or both document
	 * 
	 * @param client available client
	 * @param remotePath exsited path
	 * @param localFile exsited file
	 * @return boolean
	 */
	private boolean storeFile0(FTPClient client, String remotePath, File localFile)
	{
		boolean rtnBoolean = true;

		BufferedInputStream bis = null;
		InputStream is = null;
		try
		{
			if(isDirectory(client, remotePath) && localFile.isDirectory())
			{
				String prefix = remotePath + (remotePath.endsWith("/") ? "" : "/");
				for(File newFile : localFile.listFiles())
				{
					String newPath = prefix + newFile.getName();
					rtnBoolean = rtnBoolean && newFile.isDirectory() ? client.makeDirectory(newPath) : true;
					rtnBoolean = rtnBoolean && storeFile0(client, newPath, newFile);
				}
			}
			else
			{
				is = new FileInputStream(localFile);
				bis = new BufferedInputStream(is);
				rtnBoolean = rtnBoolean && client.storeFile(remotePath, bis);
			}

		}
		catch(Exception e)
		{
			rtnBoolean = false;
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_RETRIEVE_FILE_ERROR, KV.ins("remotePath", remotePath).put("localFile", localFile), e);
		}
		finally
		{
			Closeables.closeQuietly(is);
			Closeables.closeQuietly(bis);
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
	public Ftp0 setFileType(int fileType)
	{
		try
		{
			client.setFileType(fileType);
			this.fileType = fileType;
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_FILETYPE_ERROR, KV.ins("fileType", fileType), e);
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
	public Ftp0 setMode(int mode)
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
					LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_MODE_ERROR, KV.ins("mode", mode), e);
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
					LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_MODE_ERROR, KV.ins("mode", mode), e);
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
	public String getEncoding()
	{
		return encoding;
	}

	/**
	 * this is a setter method for encoding
	 *
	 * @param encoding the encoding to set
	 */
	public Ftp0 setEncoding(@Nullable String encoding)
	{
		encoding = Strings.isNullOrEmpty(encoding) ? Encoding.UTF8 : encoding;
		client.setControlEncoding(encoding);
		this.encoding = encoding;
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
	public Ftp0 setTimeout(int timeout)
	{
		try
		{
			client.setSoTimeout(timeout);
			this.timeout = timeout;
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_TIMEOUT_ERROR, KV.ins("timeout", timeout), e);
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

	/**
	 * this is a getter method for bufferSize
	 *
	 * @return the bufferSize
	 */
	public int getBufferSize()
	{
		return bufferSize;
	}

	/**
	 * this is a setter method for bufferSize
	 *
	 * @param bufferSize the bufferSize to set
	 */
	public void setBufferSize(int bufferSize)
	{
		client.setBufferSize(bufferSize);
		this.bufferSize = bufferSize;
	}

}
