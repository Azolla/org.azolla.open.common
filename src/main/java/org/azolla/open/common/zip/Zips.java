/*
 * @(#)Zips.java		Created at 2013-7-3
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.azolla.open.common.exception.code.AzollaCode;
import org.azolla.open.common.io.Encoding;
import org.azolla.open.common.io.File0;
import org.azolla.open.common.text.Fmts;
import org.azolla.open.common.util.Date0;
import org.azolla.open.common.util.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;

/**
 * ZipHelper
 * 
 * <p>Support Chinese:Encoding.GBK
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class Zips
{
	private static final Logger	LOG		= LoggerFactory.getLogger(Zips.class);

	private static int			bufSize	= 8096;								//size of bytes

	public static boolean unzip(File zip)
	{
		return null == zip ? false : unzip(zip.getPath(), "");
	}

	public static boolean unzip(File zip, Encoding encoding)
	{
		return null == zip ? false : unzip(zip.getPath(), null, encoding);
	}

	public static boolean unzip(File zip, String dest)
	{
		return null == zip ? false : unzip(zip.getPath(), dest);
	}

	public static boolean unzip(String zip)
	{
		return unzip(zip, "");
	}

	public static boolean unzip(String zip, Encoding encoding)
	{
		return unzip(zip, null, Encoding.UTF8);
	}

	public static boolean unzip(String zip, String dest)
	{
		return unzip(zip, dest, Encoding.UTF8);
	}

	public static boolean unzip(String zip, String dest, Encoding encoding)
	{
		boolean rtnBoolean = true;
		if(!Strings.isNullOrEmpty(zip))
		{
			File zipFile = new File(zip);
			if(Strings.isNullOrEmpty(dest))
			{
				dest = zipFile.getParent();
			}

			File destFile = null;
			ZipFile zf = null;
			try
			{
				zf = new ZipFile(zipFile, encoding.getEncoding());

				for(Enumeration<ZipEntry> entries = zf.getEntries(); entries.hasMoreElements();)
				{
					ZipEntry entry = entries.nextElement();
					destFile = new File(dest, entry.getName());

					unzipFile(destFile, zf, entry); //执行解压
				}
			}
			catch(IOException e)
			{
				rtnBoolean = false;
				LOG.error(Fmts.LOG_EC_P_M_FMT, AzollaCode.ZIP_ZIP_ERROR, KV.newKV().set("zip", zip).set("dest", dest).set("encoding", encoding.getEncoding()), e.toString());
			}
		}
		return rtnBoolean;
	}

	private static boolean unzipFile(File destFile, ZipFile zipFile, ZipEntry entry)
	{
		boolean rtnBoolean = true;
		if(entry.isDirectory())
		{
			destFile.mkdirs();
		}
		else
		{
			File parent = destFile.getParentFile();
			if(parent != null && !parent.exists())
			{
				parent.mkdirs();
			}
			InputStream is = null;
			FileOutputStream fos = null;
			try
			{
				is = zipFile.getInputStream(entry);
				fos = new FileOutputStream(destFile);
				byte[] buf = new byte[bufSize];
				int readedBytes;
				while((readedBytes = is.read(buf)) > 0)
				{
					fos.write(buf, 0, readedBytes);
				}
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmts.LOG_EC_P_M_FMT, AzollaCode.ZIP_ZIP_ERROR, KV.newKV().set("destFile", destFile.getAbsolutePath()), e.toString());
			}
			finally
			{
				Closeables.closeQuietly(fos);
				Closeables.closeQuietly(is);
			}
		}
		return rtnBoolean;
	}

	public static boolean zip(List<File> fileList)
	{
		return zip(fileList, null);
	}

	public static boolean zip(List<File> fileList, String zip)
	{
		return zip(fileList, zip, Encoding.UTF8);
	}

	public static boolean zip(List<File> fileList, String zip, Encoding encoding)
	{
		boolean rtnBoolean = false;
		if(null != fileList && null != encoding)
		{
			if(Strings.isNullOrEmpty(zip))
			{
				zip = Date0.toString(Date0.Y__M__D_H_MI_S);
			}
			if(!Lists.newArrayList(File0.ZIP_FILETYPE, File0.JAR_FILETYPE, File0.WAR_FILETYPE).contains(File0.getFileType(zip).toLowerCase()))
			{
				zip += File0.ZIP_FILETYPE_WITH_POINT;
			}

			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			ZipOutputStream zos = null;
			try
			{
				fos = new FileOutputStream(zip);
				bos = new BufferedOutputStream(fos);
				zos = new ZipOutputStream(bos);

				zos.setEncoding(encoding.getEncoding());

				for(File file : fileList)
				{
					zipFiles(file, zos, "");
				}

				rtnBoolean = true;
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmts.LOG_EC_P_M_FMT, AzollaCode.ZIP_ZIP_ERROR, KV.newKV().set("fileList", Joiner.on("|").join(fileList)).set("zip", zip).set("encoding", encoding.getEncoding()), e.toString());
			}
			finally
			{
				Closeables.closeQuietly(zos);
				Closeables.closeQuietly(bos);
				Closeables.closeQuietly(fos);
			}
		}
		return rtnBoolean;
	}

	public static boolean zip(String dir)
	{
		return zip(dir, false);
	}

	public static boolean zip(String dir, boolean self)
	{
		return Strings.isNullOrEmpty(dir) ? false : zip(dir, new File(dir).getPath(), self);
	}

	public static boolean zip(String dir, Encoding encoding)
	{
		return Strings.isNullOrEmpty(dir) ? false : zip(dir, new File(dir).getPath(), encoding);
	}

	public static boolean zip(String dir, String zip, boolean self)
	{
		return zip(dir, zip, Encoding.UTF8, self);
	}

	public static boolean zip(String dir, String zip, Encoding encoding)
	{
		return zip(dir, zip, encoding, false);
	}

	public static boolean zip(String dir, String zip, Encoding encoding, boolean self)
	{
		boolean rtnBoolean = false;
		if(!Strings.isNullOrEmpty(dir))
		{
			File dirFile = new File(dir);
			List<File> fileList = Lists.newArrayList();
			if(self || dirFile.isFile())
			{
				fileList.add(dirFile);
			}
			else
			{
				fileList.addAll(Lists.newArrayList(dirFile.listFiles()));
			}
			rtnBoolean = zip(fileList, zip, encoding);
		}
		return rtnBoolean;
	}

	private static boolean zipFiles(File file, ZipOutputStream zipOutput, String pathName)
	{
		boolean rtnBoolean = true;

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try
		{
			String fileName = pathName + file.getName();
			if(file.isDirectory())
			{
				fileName = fileName + "/";
				zipOutput.putNextEntry(new ZipEntry(fileName));
				String fileNames[] = file.list();
				if(fileNames != null)
				{
					for(int i = 0; i < fileNames.length; i++)
					{
						zipFiles(new File(file, fileNames[i]), zipOutput, fileName);
					}

				}
			}
			else
			{
				ZipEntry jarEntry = new ZipEntry(fileName);
				zipOutput.putNextEntry(jarEntry);

				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);

				byte[] buf = new byte[bufSize];
				int len;
				while((len = bis.read(buf)) >= 0)
				{
					zipOutput.write(buf, 0, len);
				}
				zipOutput.closeEntry();
			}
		}
		catch(Exception e)
		{
			rtnBoolean = false;
			LOG.error(Fmts.LOG_EC_P_M_FMT, AzollaCode.ZIP_ZIP_ERROR, KV.newKV().set("file", file.getAbsolutePath()).set("pathName", pathName), e.toString());
		}
		finally
		{
			Closeables.closeQuietly(bis);
			Closeables.closeQuietly(fis);
		}
		return rtnBoolean;
	}
}
