/*
 * @(#)Zip0.java		Created at 2013-7-3
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.azolla.open.ling.exception.code.AzollaCode;
import org.azolla.open.ling.io.Encoding;
import org.azolla.open.ling.io.File0;
import org.azolla.open.ling.text.Fmt0;
import org.azolla.open.ling.util.Date0;
import org.azolla.open.ling.util.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;

/**
 * Zip0
 * 
 * <p>Support Chinese:Encoding.GBK
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Zip0
{
	private static final Logger	LOG		= LoggerFactory.getLogger(Zip0.class);

	private static int			bufSize	= 8096;								//size of bytes

	public static boolean unzip(File zipFile)
	{
		return unzip(zipFile, null);
	}

	public static boolean unzip(File zipFile, @Nullable String dest)
	{
		return unzip(zipFile, dest, null);
	}

	public static boolean unzip(File zipFile, @Nullable String dest, @Nullable String encoding)
	{
		boolean rtnBoolean = true;
		if(zipFile != null)
		{
			dest = Strings.isNullOrEmpty(dest) ? zipFile.getParent() : dest;
			encoding = Strings.isNullOrEmpty(encoding) ? Encoding.UTF8 : encoding;

			File destFile = null;
			ZipFile zf = null;
			try
			{
				zf = new ZipFile(zipFile, encoding);

				for(Enumeration<ZipEntry> entries = zf.getEntries(); entries.hasMoreElements();)
				{
					ZipEntry entry = entries.nextElement();
					destFile = new File(dest, entry.getName());

					unzipFile(destFile, zf, entry); //执行解压
				}
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.ZIP_ZIP_ERROR, KV.ins("zipFile", zipFile).put("dest", dest).put("encoding", encoding), e.toString(), e);
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
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.ZIP_ZIP_ERROR, KV.ins("destFile", destFile), e.toString(), e);
			}
			finally
			{
				Closeables.closeQuietly(fos);
				Closeables.closeQuietly(is);
			}
		}
		return rtnBoolean;
	}

	public static boolean zip(File dirFile)
	{
		return zip(dirFile, false);
	}

	public static boolean zip(File dirFile, boolean self)
	{
		return dirFile == null ? false : zip(dirFile, dirFile.getPath(), self);
	}

	public static boolean zip(File dirFile, @Nullable String encoding)
	{
		return dirFile == null ? false : zip(dirFile, dirFile.getPath(), encoding);
	}

	public static boolean zip(File dirFile, String zip, boolean self)
	{
		return zip(dirFile, zip, null, self);
	}

	public static boolean zip(File dirFile, String zip, @Nullable String encoding)
	{
		return zip(dirFile, zip, encoding, false);
	}

	public static boolean zip(File dirFile, String zip, @Nullable String encoding, boolean self)
	{
		boolean rtnBoolean = false;
		if(dirFile != null)
		{
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

	public static boolean zip(List<File> fileList)
	{
		return zip(fileList, null);
	}

	public static boolean zip(List<File> fileList, @Nullable String zip)
	{
		return zip(fileList, zip, null);
	}

	public static boolean zip(List<File> fileList, @Nullable String zip, @Nullable String encoding)
	{
		boolean rtnBoolean = false;
		if(null != fileList)
		{
			encoding = Strings.isNullOrEmpty(encoding) ? Encoding.UTF8 : encoding;
			zip = Strings.isNullOrEmpty(zip) ? Date0.toString(Date0.Y_M_D_H_MI_S) : zip;
			if(!Lists.newArrayList(File0.ZIP_FILETYPE, File0.JAR_FILETYPE, File0.WAR_FILETYPE).contains(File0.fileType(zip).toLowerCase()))
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

				zos.setEncoding(encoding);

				for(File file : fileList)
				{
					zipFiles(file, zos, "");
				}

				rtnBoolean = true;
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.ZIP_ZIP_ERROR, KV.ins("fileList", Joiner.on("|").join(fileList)).put("zip", zip).put("encoding", encoding), e.toString(), e);
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
			LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.ZIP_ZIP_ERROR, KV.ins("file", file).put("pathName", pathName), e.toString(), e);
		}
		finally
		{
			Closeables.closeQuietly(bis);
			Closeables.closeQuietly(fis);
		}
		return rtnBoolean;
	}
}
