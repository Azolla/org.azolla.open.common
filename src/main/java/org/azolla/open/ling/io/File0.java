/*
 * @(#)File0.java		Created at 2013-2-24
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Nullable;

import org.azolla.open.ling.text.Fmt0;
import org.azolla.open.ling.util.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

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
	private static final Logger	LOG						= LoggerFactory.getLogger(File0.class);

	public static final String	ILLEGAL_FILENAME_REGEX	= "[{/\\\\:*?\"<>|}]";

	public static final String	POINT					= ".";

	public static final String	ZIP_FILETYPE			= "zip";
	public static final String	ZIP_FILETYPE_WITH_POINT	= POINT + ZIP_FILETYPE;

	public static final String	JAR_FILETYPE			= "jar";
	public static final String	JAR_FILETYPE_WITH_POINT	= POINT + JAR_FILETYPE;

	public static final String	WAR_FILETYPE			= "war";
	public static final String	WAR_FILETYPE_WITH_POINT	= POINT + WAR_FILETYPE;

	public static final String	TXT_FILETYPE			= "txt";
	public static final String	TXT_FILETYPE_WITH_POINT	= POINT + TXT_FILETYPE;

	public static final String	BAK_FILETYPE			= "bak";
	public static final String	BAK_FILETYPE_WITH_POINT	= POINT + BAK_FILETYPE;

	/**
	 * New file
	 * 
	 * @param strings
	 * @return File
	 */
	public static File newFile(String... strings)
	{
		return new File(Joiner.on(File.separator).join(strings));
	}

	/**
	 * New file
	 * 
	 * @param parent
	 * @param strings
	 * @return File
	 */
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

	/**
	 * Delete file(contain sub file)
	 * 
	 * @param file existed
	 * @return boolean
	 */
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
		return fileType(file.getName());
	}

	/**
	 * return type of file by file name
	 * example:test.txt -> txt
	 * 
	 * @param fileName file name
	 * @return type of file
	 */
	public static String fileType(String fileName)
	{
		fileName = String.valueOf(fileName);

		int lastPointIndex = fileName.lastIndexOf(".");
		return -1 == lastPointIndex ? fileName : fileName.substring(lastPointIndex + 1);
	}

	/**
	 * @see org.azolla.open.ling.io.File0#toLegalFileName(String, String)
	 */
	public static String toLegalFileName(String fileName)
	{
		return toLegalFileName(fileName, null);
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
		legalString = Strings.isNullOrEmpty(legalString) ? "_" : legalString;

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
				com.google.common.io.Files.copy(from, to);
			}
			catch(Exception e)
			{
				rtnBoolean = false;
				LOG.error(Fmt0.LOG_P, KV.ins("from", from).put("to", to), e);
			}
		}

		return rtnBoolean;
	}

	public static String getUserDir()
	{
		return System.getProperty("user.dir");
	}

	public static String getUserHome()
	{
		return System.getProperty("user.home");
	}

	public static String getEncoding(String filePath)
	{
		try
		{
			return Files.probeContentType(Paths.get(filePath));
		}
		catch(IOException e)
		{
			return Encode0.SINGLETON.getFileEncoding(filePath);
		}
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
