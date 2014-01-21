/*
 * @(#)File0.java		Created at 2013-2-24
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.List;

import javax.annotation.Nullable;

import org.azolla.open.ling.text.Fmt0;
import org.azolla.open.ling.util.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * FileHelper
 * 
 * The concept about some object:
 * 1.file/path		:<code>File</code>/<code>String</code> object, maybe document,maybe directory and maybe null.
 * 2.document		:<code>File</code>/<code>String</code> object, but it does not contain directory
 * 3.directory		:<code>File</code>/<code>String</code> object, just directory
 * 
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class File0
{
    private static final Logger LOG                     = LoggerFactory.getLogger(File0.class);

    public static final String  ILLEGAL_FILENAME_REGEX  = "[{/\\\\:*?\"<>|}]";

    public static final String  POINT                   = ".";
    public static final String  UNDERLINE               = "_";
    public static final String  MD5                     = "MD5";
    public static final String  USER_DIR                = "user.dir";
    public static final String  USER_HOME               = "user.home";

    public static final String  ZIP_FILETYPE            = "zip";
    public static final String  ZIP_FILETYPE_WITH_POINT = POINT + ZIP_FILETYPE;

    public static final String  JAR_FILETYPE            = "jar";
    public static final String  JAR_FILETYPE_WITH_POINT = POINT + JAR_FILETYPE;

    public static final String  WAR_FILETYPE            = "war";
    public static final String  WAR_FILETYPE_WITH_POINT = POINT + WAR_FILETYPE;

    public static final String  TXT_FILETYPE            = "txt";
    public static final String  TXT_FILETYPE_WITH_POINT = POINT + TXT_FILETYPE;

    public static final String  BAK_FILETYPE            = "bak";
    public static final String  BAK_FILETYPE_WITH_POINT = POINT + BAK_FILETYPE;

    public static File newFile(String... strings)
    {
        return new File(Joiner.on(File.separator).join(strings));
    }

    public static File newFile(File parent, String... strings)
    {
        return new File(parent, Joiner.on(File.separator).join(strings));
    }

    /**
     * Delete file(contain sub file)
     * 
     * @param file	documnet or directory
     * @return boolean
     */
    public static boolean delFile(File file)
    {
        boolean rtnBoolean = true;

        if(null != file && file.exists())
        {
            rtnBoolean = delFile0(file);
        }

        return rtnBoolean;
    }

    private static boolean delFile0(File file)
    {
        boolean rtnBoolean = true;

        if(file.isDirectory())
        {
            for(File subFile : file.listFiles())
            {
                rtnBoolean = rtnBoolean && delFile0(subFile);
            }
        }
        rtnBoolean = rtnBoolean && file.delete();

        return rtnBoolean;
    }

    /**
     * empty File
     * 
     * @param file	documnet or directory
     * @return boolean
     */
    public static boolean emptyFile(File file)
    {
        boolean rtnBoolean = true;

        if(null != file && file.exists())
        {
            if(file.isDirectory())
            {
                for(File subFile : file.listFiles())
                {
                    rtnBoolean = rtnBoolean && delFile0(subFile);
                }
            }
            else
            {
                File bakFile = bakFileName(file);
                rtnBoolean = rtnBoolean && file.renameTo(bakFile);
                if(rtnBoolean)
                {
                    try
                    {
                        rtnBoolean = rtnBoolean && file.createNewFile();
                        if(rtnBoolean)
                        {
                            bakFile.delete();	//ignore failed to delete
                        }
                    }
                    catch(Exception e)
                    {
                        LOG.error(Fmt0.LOG_P, KV.ins("file", file), e);
                        bakFile.renameTo(file);
                        rtnBoolean = false;
                    }
                }
            }
        }

        return rtnBoolean;
    }

    /**
     * Return file's bakName
     * 
     * sample.txt -> sample.txt.bak
     * 
     * @param file
     * @return File
     */
    public static File bakFileName(File file)
    {
        File rtnFile = null;

        if(null != file)
        {
            rtnFile = newFile(file.getParentFile(), file.getName() + BAK_FILETYPE_WITH_POINT);
        }

        return rtnFile;
    }

    /**
     * all of file contain sub file
     * 
     * @param file
     * @return List<File>	
     */
    public static List<File> allFile(File file)
    {
        List<File> rtnList = Lists.newArrayList();

        if(null != file && file.exists())
        {
            rtnList.addAll(allFile0(file));
        }

        return rtnList;
    }

    /**
     * all of file contain sub file
     * 
     * @param file existed file
     * @return List<File>
     */
    private static List<File> allFile0(File file)
    {
        List<File> rtnList = Lists.newArrayList();

        rtnList.add(file);
        if(file.isDirectory())
        {
            for(File subFile : file.listFiles())
            {
                rtnList.addAll(allFile0(subFile));
            }
        }

        return rtnList;
    }

    /**
     * @see org.azolla.open.ling.io.File0#fileType(String)
     */
    public static String fileType(File file)
    {
        if(null == file)
        {
            return String.valueOf(file);
        }
        else
        {
            return fileType(file.getName());
        }
    }

    /**
     * return type of file by file name
     * Example:test.txt -> txt
     * 
     * @param fileName file name
     * @return type of file
     */
    public static String fileType(String fileName)
    {
        fileName = String.valueOf(fileName);

        int lastPointIndex = fileName.lastIndexOf(POINT);
        return -1 == lastPointIndex ? fileName : fileName.substring(lastPointIndex + 1);
    }

    /**
     * @see org.azolla.open.ling.io.File0#toLegalFileName(String, String)
     */
    public static String toLegalFileName(String fileName)
    {
        return toLegalFileName(fileName, UNDERLINE);
    }

    /**
     * transform legal file name
     * 
     * @param fileName file name
     * @param legalString legal string
     * @return legal file name
     */
    public static String toLegalFileName(String fileName, @Nullable String legalString)
    {
        fileName = String.valueOf(fileName);
        legalString = Strings.isNullOrEmpty(legalString) ? UNDERLINE : legalString;

        return fileName.replaceAll(ILLEGAL_FILENAME_REGEX, legalString);
    }

    public static boolean copy(File from, File to)
    {
        if(from == null || to == null || !from.exists())
        {
            return false;
        }

        return copy0(from, to);
    }

    private static boolean copy0(File from, File to)
    {
        boolean rtnBoolean = true;

        if(from.isDirectory())
        {
            to.mkdirs();
            for(File f : from.listFiles())
            {
                rtnBoolean = rtnBoolean && copy0(f, File0.newFile(to, f.getName()));
            }
        }
        else
        {
            try
            {
                Files.copy(from, to);
            }
            catch(Exception e)
            {
                LOG.error(Fmt0.LOG_P, KV.ins("from", from).put("to", to), e);
                rtnBoolean = false;
            }
        }

        return rtnBoolean;
    }

    public static String getUserDir()
    {
        return System.getProperty(USER_DIR);
    }

    public static String getUserHome()
    {
        return System.getProperty(USER_HOME);
    }

    public static String getEncoding(String filePath)
    {
        try
        {
            return java.nio.file.Files.probeContentType(Paths.get(filePath));
        }
        catch(IOException e)
        {
            return Encode0.SINGLETON.getFileEncoding(filePath);
        }
    }

    public static String getMD5(File file)
    {
        String rtnString = null;
        if(file != null && file.isFile())
        {
            int len;
            int bufferSize = 256 * 1024;
            MessageDigest digest = null;
            FileInputStream in = null;
            byte buffer[] = new byte[bufferSize];
            try
            {
                digest = MessageDigest.getInstance(MD5);
                in = new FileInputStream(file);
                while((len = in.read(buffer, 0, bufferSize)) != -1)
                {
                    digest.update(buffer, 0, len);
                }
                in.close();
            }
            catch(Exception e)
            {
                LOG.error(Fmt0.LOG_P, KV.ins("file", file), e);
            }
            finally
            {
                Close0.close(in);
            }
            rtnString = new BigInteger(1, digest.digest()).toString(16);
        }

        return rtnString;
    }

    /**
     * Windows(1.0.0.0)
     */
    public static String getVer(File file)
    {
        String rtnString = "";
        RandomAccessFile raf = null;
        byte[] buffer;
        String str;
        try
        {
            raf = new RandomAccessFile(file, "r");
            buffer = new byte[64];
            raf.read(buffer);
            str = "" + (char) buffer[0] + (char) buffer[1];
            if(!"MZ".equals(str))
            {
                return rtnString;
            }

            int peOffset = unpack(new byte[] {buffer[60], buffer[61], buffer[62], buffer[63]});
            if(peOffset < 64)
            {
                return rtnString;
            }

            raf.seek(peOffset);
            buffer = new byte[24];
            raf.read(buffer);
            str = "" + (char) buffer[0] + (char) buffer[1];
            if(!"PE".equals(str))
            {
                return rtnString;
            }
            int machine = unpack(new byte[] {buffer[4], buffer[5]});
            if(machine != 332)
            {
                return rtnString;
            }

            int noSections = unpack(new byte[] {buffer[6], buffer[7]});
            int optHdrSize = unpack(new byte[] {buffer[20], buffer[21]});
            raf.seek(raf.getFilePointer() + optHdrSize);
            boolean resFound = false;
            for(int i = 0; i < noSections; i++)
            {
                buffer = new byte[40];
                raf.read(buffer);
                str = "" + (char) buffer[0] + (char) buffer[1] + (char) buffer[2] + (char) buffer[3] + (char) buffer[4];
                if(".rsrc".equals(str))
                {
                    resFound = true;
                    break;
                }
            }
            if(!resFound)
            {
                return rtnString;
            }

            int infoVirt = unpack(new byte[] {buffer[12], buffer[13], buffer[14], buffer[15]});
            int infoSize = unpack(new byte[] {buffer[16], buffer[17], buffer[18], buffer[19]});
            int infoOff = unpack(new byte[] {buffer[20], buffer[21], buffer[22], buffer[23]});
            raf.seek(infoOff);
            buffer = new byte[infoSize];
            raf.read(buffer);
            int nameEntries = unpack(new byte[] {buffer[12], buffer[13]});
            int idEntries = unpack(new byte[] {buffer[14], buffer[15]});
            boolean infoFound = false;
            int subOff = 0;
            for(int i = 0; i < (nameEntries + idEntries); i++)
            {
                int type = unpack(new byte[] {buffer[i * 8 + 16], buffer[i * 8 + 17], buffer[i * 8 + 18], buffer[i * 8 + 19]});
                if(type == 16)
                {                          //FILEINFO resource  
                    infoFound = true;
                    subOff = unpack(new byte[] {buffer[i * 8 + 20], buffer[i * 8 + 21], buffer[i * 8 + 22], buffer[i * 8 + 23]});
                    break;
                }
            }
            if(!infoFound)
            {
                return rtnString;
            }

            subOff = subOff & 0x7fffffff;
            infoOff = unpack(new byte[] {buffer[subOff + 20], buffer[subOff + 21], buffer[subOff + 22], buffer[subOff + 23]}); //offset of first FILEINFO  
            infoOff = infoOff & 0x7fffffff;
            infoOff = unpack(new byte[] {buffer[infoOff + 20], buffer[infoOff + 21], buffer[infoOff + 22], buffer[infoOff + 23]});    //offset to data  
            int dataOff = unpack(new byte[] {buffer[infoOff], buffer[infoOff + 1], buffer[infoOff + 2], buffer[infoOff + 3]});
            dataOff = dataOff - infoVirt;

            int version1 = unpack(new byte[] {buffer[dataOff + 48], buffer[dataOff + 48 + 1]});
            int version2 = unpack(new byte[] {buffer[dataOff + 48 + 2], buffer[dataOff + 48 + 3]});
            int version3 = unpack(new byte[] {buffer[dataOff + 48 + 4], buffer[dataOff + 48 + 5]});
            int version4 = unpack(new byte[] {buffer[dataOff + 48 + 6], buffer[dataOff + 48 + 7]});
            rtnString = version2 + "." + version1 + "." + version4 + "." + version3;
        }
        catch(Exception e)
        {
            LOG.error(Fmt0.LOG_P, KV.ins("file", file), e);
        }
        finally
        {
            Close0.close(raf);
        }
        return rtnString;
    }

    private static int unpack(byte[] b)
    {
        int num = 0;
        for(int i = 0; i < b.length; i++)
        {
            num = 256 * num + (b[b.length - 1 - i] & 0xff);
        }
        return num;
    }

    @SuppressWarnings("null")
    public static void main(String[] args)
    {
        String s1 = "";
        System.out.println(new File(s1).getAbsolutePath());
        // NullPointerException
        List<String> stringList = null;
        for(String s : stringList)
        {
            System.out.println(s);
        }
    }
}
