/*
 * @(#)Ftp0.java		Created at 2013-7-3
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.ftp;

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
import org.azolla.l.ling.exception.AzollaException;
import org.azolla.l.ling.exception.code.AzollaCode;
import org.azolla.l.ling.io.Close0;
import org.azolla.l.ling.io.Encoding0;
import org.azolla.l.ling.io.File0;
import org.azolla.l.ling.lang.Char0;
import org.azolla.l.ling.lang.String0;
import org.azolla.l.ling.text.Fmt0;
import org.azolla.l.ling.util.KV;
import org.azolla.l.ling.util.Log0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * Ftp0
 * 
 * via http://blog.csdn.net/cuiran/article/details/7186621
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Ftp0
{
    public static final int     DEFAULT_PORT        = 21;
    public static final int     DEFAULT_FILE_TYPE   = FTPClient.BINARY_FILE_TYPE;
    public static final int     DEFAULT_MODE        = FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE;
    public static final int     DEFAULT_TIMEOUT     = 3000;
    public static final int     DEFAULT_BUFFER_SIZE = 100000;

    private static Ftp0         instance            = null;

    private String              host;
    private String              username;
    private String              password;

    private int                 port                = DEFAULT_PORT;
    private int                 fileType            = DEFAULT_FILE_TYPE;
    private int                 mode                = DEFAULT_MODE;
    private int                 timeout             = DEFAULT_TIMEOUT;
    private int                 bufferSize          = DEFAULT_BUFFER_SIZE;

    private String              encoding            = Encoding0.UTF_8;

    private FTPClient           client;

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
        return single(host, username, password, port, fileType, mode, timeout, Encoding0.UTF_8);
    }

    public static synchronized Ftp0 single(String host, String username, String password, int port, int fileType, int mode, int timeout, String encoding)
    {
        return instance == null ? new Ftp0(host, username, password, port, fileType, mode, timeout, encoding) : instance;
    }

    public Ftp0(String host, String username, String password)
    {
        this(host, username, password, DEFAULT_PORT);
    }

    public Ftp0(String host, String username, String password, int port)
    {
        this(host, username, password, port, DEFAULT_FILE_TYPE);
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
        this(host, username, password, port, fileType, mode, timeout, Encoding0.UTF_8);
    }

    public Ftp0(String host, String username, String password, int port, int fileType, int mode, int timeout, String encoding)
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
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_CONNECT_ERROR, KV.ins("host", host).put("port", port), e);
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
                Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_LOGIN_ERROR, KV.ins("username", username).put("password", password), e);
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
                Log0.error(this.getClass(), Fmt0.LOG_EC_P, AzollaCode.FTP_LOGIN_FAILED, KV.ins("username", username).put("password", password));
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

        if(client != null && client.isConnected())
        {
            try
            {
                client.logout();
            }
            catch(Exception e)
            {
                Log0.warn(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_LOGOUT_ERROR, KV.ins("host", host), e);
                rtnBoolean = false;
            }
        }

        if(client != null && client.isConnected())
        {
            try
            {
                client.disconnect();
            }
            catch(Exception e)
            {
                Log0.warn(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_DISCONNECT_ERROR, KV.ins("host", host), e);
                rtnBoolean = false;
            }
        }

        return rtnBoolean;
    }

    //Delete file(document or directory) from ftp
    public boolean deleteFile(@Nullable FTPClient client, String remotePath)
    {
        boolean rtnBoolean = true;

        try
        {
            //If remotePath is null or empty will be very dangerous
            Preconditions.checkArgument(!Strings.isNullOrEmpty(remotePath));

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
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_DELETE_FILE_ERROR, KV.ins("remotePath", remotePath), e);
            rtnBoolean = false;
        }

        return rtnBoolean;
    }

    private boolean deleteFile0(FTPClient client, String remotePath)
    {
        boolean rtnBoolean = true;

        try
        {
            for(FTPFile file : client.listFiles(remotePath))
            {
                String prefix = remotePath + (remotePath.endsWith(String0.SLASH) ? String0.EMPTY : String0.SLASH);
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
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_DELETE_FILE_ERROR, KV.ins("remotePath", remotePath), e);
            rtnBoolean = false;
        }

        return rtnBoolean;
    }

    //download file(document or directory) to local
    public boolean retrieveFile(@Nullable FTPClient client, String remotePath, File localFile)
    {
        boolean rtnBoolean = true;

        try
        {
            //If remotePath is null or empty will be very dangerous
            Preconditions.checkNotNull(localFile);
            Preconditions.checkArgument(!Strings.isNullOrEmpty(remotePath));

            client = client == null ? activeClient() : client;

            if((isDirectory(client, remotePath) && localFile.isDirectory()) || (!isDirectory(client, remotePath) && !localFile.isDirectory()))
            {
                //remote file(directory) to local file(directory)
                //remote file(document) to local file(document)
                rtnBoolean = retrieveFile0(client, remotePath, localFile);
            }
            else if(isExist(client, remotePath) && !isDirectory(client, remotePath) && localFile.isDirectory())
            {
                //remote file(document) to local file(directory)
                File file = File0.newFile(localFile, listFiles(client, remotePath, null).get(0).getName());
                rtnBoolean = file.createNewFile() && retrieveFile0(client, remotePath, file);
            }
            else
            {
                throw new IllegalArgumentException();
            }
        }
        catch(Exception e)
        {
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_RETRIEVE_FILE_ERROR, KV.ins("remotePath", remotePath).put("localFile", localFile), e);
            rtnBoolean = false;
        }

        return rtnBoolean;
    }

    //both directory or both document
    private boolean retrieveFile0(FTPClient client, String remotePath, File localFile)
    {
        boolean rtnBoolean = true;

        BufferedOutputStream bos = null;
        OutputStream os = null;
        try
        {
            if(isDirectory(client, remotePath) && localFile.isDirectory())
            {
                String prefix = remotePath + (remotePath.endsWith(String0.SLASH) ? String0.EMPTY : String0.SLASH);
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
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_RETRIEVE_FILE_ERROR, KV.ins("remotePath", remotePath).put("localFile", localFile), e);
            rtnBoolean = false;
        }
        finally
        {
            Close0.close(os);
            Close0.close(bos);
        }

        return rtnBoolean;
    }

    //file(document or directory) existed
    public boolean isExist(@Nullable FTPClient client, @Nullable String remotePath)
    {
        boolean rtnBoolean = false;

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
                rtnBoolean = isDirectory0(client, remotePath);
            }
        }
        catch(Exception e)
        {
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e);
            rtnBoolean = false;
        }

        return rtnBoolean;
    }

    //file(directory) existed
    public boolean isDirectory(@Nullable FTPClient client, @Nullable String remotePath)
    {
        boolean rtnBoolean = false;

        try
        {
            client = client == null ? activeClient() : client;

            if(isExist(client, remotePath))
            {
                rtnBoolean = isDirectory0(client, remotePath);
            }
        }
        catch(Exception e)
        {
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e);
            rtnBoolean = false;
        }

        return rtnBoolean;
    }

    private boolean isDirectory0(FTPClient client, String remotePath)
    {
        boolean rtnBoolean = false;

        try
        {
            //1.empty root:String0.SLASH
            if(Strings.isNullOrEmpty(remotePath) && String0.SLASH.equalsIgnoreCase(remotePath))
            {
                rtnBoolean = true;
            }
            else
            {
                //2.empty folder:"/folder","/folder/","folder","folder/","folder/subfolder"
                remotePath = (remotePath.startsWith(String0.SLASH) ? String0.EMPTY : String0.SLASH) + remotePath;
                remotePath = remotePath + (remotePath.endsWith(String0.SLASH) ? String0.EMPTY : String0.SLASH);
                String[] nameArray = remotePath.split(String0.SLASH);
                FTPFile[] array = client.listFiles(String0.SLASH + Joiner.on(String0.SLASH).join(Arrays.copyOfRange(nameArray, 1, nameArray.length - 1)));
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
        catch(Exception e)
        {
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e);
            rtnBoolean = false;
        }

        return rtnBoolean;
    }

    //List of fullpath under this folder, if remotePath is document return it
    public List<String> listNames(@Nullable FTPClient client, @Nullable String remotePath)
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
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e);
        }

        return rtnList;
    }

    //All of fullpath under this folder, if remotePath is document return it
    public List<String> allNames(@Nullable FTPClient client, @Nullable String remotePath, @Nullable FTPFileFilter filter)
    {
        List<String> rtnList = Lists.newArrayList();

        try
        {
            client = client == null ? activeClient() : client;

            FTPFile[] pathArray = filter == null ? client.listFiles(remotePath) : client.listFiles(remotePath, filter);
            if(pathArray != null && pathArray.length > 0)
            {
                String filePrefix = Strings.isNullOrEmpty(remotePath) ? String0.EMPTY : (remotePath.startsWith(String0.SLASH) ? remotePath : String0.SLASH + remotePath);
                String folderPrefix = filePrefix.endsWith(String0.SLASH) ? filePrefix : filePrefix + String0.SLASH;
                for(FTPFile f : pathArray)
                {
                    if(f.isDirectory())
                    {
                        rtnList.addAll(allNames(client, folderPrefix + f.getName(), filter));
                    }
                    if(pathArray.length > 1)
                    {
                        rtnList.add(folderPrefix + f.getName());
                    }
                    else
                    {
                        int index = filePrefix.lastIndexOf(String0.SLASH);
                        rtnList.add(f.getName().equals(-1 == index ? filePrefix : filePrefix.substring(index + 1)) ? filePrefix : filePrefix + String0.SLASH + f.getName());
                    }
                }
            }
        }
        catch(Exception e)
        {
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e);
        }

        return rtnList;
    }

    //List of FTPFile under this folder, if remotePath is document return it
    public List<FTPFile> listFiles(@Nullable FTPClient client, @Nullable String remotePath, @Nullable FTPFileFilter filter)
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
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_LIST_FILE_ERROR, KV.ins("remotePath", remotePath), e);
        }

        return rtnList;
    }

    //upload file to ftp
    public boolean storeFile(@Nullable FTPClient client, String remotePath, File localFile)
    {
        boolean rtnBoolean = false;

        try
        {
            //If remotePath is null or empty will be very dangerous
            Preconditions.checkNotNull(localFile);
            Preconditions.checkArgument(!Strings.isNullOrEmpty(remotePath));

            client = client == null ? activeClient() : client;

            if((isDirectory(client, remotePath) && localFile.isDirectory()) || (!isDirectory(client, remotePath) && !localFile.isDirectory()))
            {
                rtnBoolean = storeFile0(client, remotePath, localFile);
            }
            else if(isDirectory(client, remotePath) && localFile.exists() && !localFile.isDirectory())
            {
                String prefix = remotePath + (remotePath.endsWith(String0.SLASH) ? String0.EMPTY : String0.SLASH);
                rtnBoolean = storeFile0(client, prefix + localFile.getName(), localFile);
            }
        }
        catch(Exception e)
        {
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_STORE_FILE_ERROR, KV.ins("remotePath", remotePath).put("localPath", localFile), e);
            rtnBoolean = false;
        }

        return rtnBoolean;
    }

    private boolean storeFile0(FTPClient client, String remotePath, File localFile)
    {
        boolean rtnBoolean = true;

        BufferedInputStream bis = null;
        InputStream is = null;
        try
        {
            if(isDirectory(client, remotePath) && localFile.isDirectory())
            {
                String prefix = remotePath + (remotePath.endsWith(String0.SLASH) ? String0.EMPTY : String0.SLASH);
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
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_RETRIEVE_FILE_ERROR, KV.ins("remotePath", remotePath).put("localFile", localFile), e);
            rtnBoolean = false;
        }
        finally
        {
            Close0.close(is);
            Close0.close(bis);
        }

        return rtnBoolean;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public int getFileType()
    {
        return fileType;
    }

    public Ftp0 setFileType(int fileType)
    {
        try
        {
            client.setFileType(fileType);
            this.fileType = fileType;
        }
        catch(Exception e)
        {
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_FILETYPE_ERROR, KV.ins("fileType", fileType), e);
            throw AzollaException.wrap(e, AzollaCode.FTP_SET_FILETYPE_ERROR).set("fileType", fileType);
        }
        return this;
    }

    public int getMode()
    {
        return mode;
    }

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
                    Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_MODE_ERROR, KV.ins("mode", mode), e);
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
                    Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_MODE_ERROR, KV.ins("mode", mode), e);
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

    public String getEncoding()
    {
        return encoding;
    }

    public Ftp0 setEncoding(@Nullable String encoding)
    {
        encoding = Strings.isNullOrEmpty(encoding) ? Encoding0.UTF_8 : encoding;

        client.setControlEncoding(encoding);
        this.encoding = encoding;
        return this;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public Ftp0 setTimeout(int timeout)
    {
        try
        {
            client.setSoTimeout(timeout);
            this.timeout = timeout;
        }
        catch(Exception e)
        {
            Log0.error(this.getClass(), Fmt0.LOG_EC_P_M, AzollaCode.FTP_SET_TIMEOUT_ERROR, KV.ins("timeout", timeout), e);
            throw AzollaException.wrap(e, AzollaCode.FTP_SET_TIMEOUT_ERROR).set("timeout", timeout);
        }
        return this;
    }

    public FTPClient getClient()
    {
        return client;
    }

    public int getBufferSize()
    {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize)
    {
        client.setBufferSize(bufferSize);
        this.bufferSize = bufferSize;
    }

}
