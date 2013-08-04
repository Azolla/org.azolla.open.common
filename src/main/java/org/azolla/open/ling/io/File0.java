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

import com.google.common.base.Function;
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

	/**
	 * @see org.azolla.open.ling.io.File0#files(File)
	 */
	public static List<String> paths(File file)
	{
		return Lists.transform(files(file), new Function<File, String>()
		{
			@Override
			public String apply(File input)
			{
				try
				{
					return input.getCanonicalPath();
				}
				catch(IOException e)
				{
					return input.getAbsolutePath();
				}
			}
		});
	}

	/**
	 * @see org.azolla.open.ling.io.File0#paths(File)
	 */
	public static List<String> paths(String path)
	{
		List<String> rtnList = Lists.newArrayList();
		if(!Strings.isNullOrEmpty(path))
		{
			File f = new File(path);
			if(f.exists())
			{
				rtnList = paths(f);
			}
		}
		return rtnList;
	}

	/**
	 * Return all document under this directory and sub directory.
	 * if the file is document return it.
	 * 
	 * Return empty when:
	 * 1.file not exist
	 * 2.file is an empty directory
	 * 
	 * inner -> outer
	 * 
	 * @param file document or directory
	 * @return the container with document
	 */
	public static List<File> files(File file)
	{
		List<File> rtnList = Lists.newArrayList();

		if(null != file && file.exists())
		{
			if(file.isDirectory() && file.list() != null)
			{
				for(File f : file.listFiles())
				{
					rtnList.addAll(files(f));
				}
			}
			else
			{
				rtnList.add(file);
			}
		}
		return rtnList;
	}

	/**
	 * @see org.azolla.open.ling.io.File0#files(File)
	 */
	public static List<File> files(String path)
	{
		List<File> rtnList = Lists.newArrayList();
		if(!Strings.isNullOrEmpty(path))
		{
			File f = new File(path);
			if(f.exists())
			{
				rtnList = files(f);
			}
		}
		return rtnList;
	}

	/**
	 * Delete all empty document under this directory and sub directory.
	 * if the file is empty document delete it.
	 * 
	 * return false when some empty document delete failure,delete stop
	 * 
	 * inner -> outer
	 * 
	 * @param file document or directory
	 * @return will return true with out false when some empty document delete failure
	 */
	public static boolean delAllEmptyFiles(File file)
	{
		boolean rtn = true;

		if(null != file && file.exists())
		{
			if(file.isDirectory() && file.list() != null)
			{
				for(File f : file.listFiles())
				{
					rtn = rtn && delAllEmptyFiles(f);
				}
			}
			else
			{
				if(file.length() == 0)
				{
					rtn = rtn && file.delete();
				}
			}
		}
		return rtn;
	}

	/**
	 * @see org.azolla.open.ling.io.File0#delAllEmptyFiles(File)
	 */
	public static boolean delAllEmptyFiles(String path)
	{
		boolean rtn = true;
		if(!Strings.isNullOrEmpty(path))
		{
			File f = new File(path);
			if(f.exists())
			{
				rtn = delAllEmptyFiles(f);
			}
		}
		return rtn;
	}

	/**
	 * Delete file sub files and  under this directory and sub directory.
	 * if this file is document or empty directory delete it.
	 * 
	 * inner -> outer
	 * 
	 * @param file document or directory
	 * @return will return true with out false when some file delete failure
	 */
	public static boolean delDirectory(File file)
	{
		boolean rtn = true;

		if(null != file && file.exists())
		{
			if(file.isDirectory() && file.list() != null)
			{
				for(File f : file.listFiles())
				{
					rtn = rtn && delDirectory(f);
				}
				rtn = rtn && file.delete();
			}
			else
			{
				rtn = rtn && file.delete();
			}
		}
		return rtn;
	}

	/**
	 * @see org.azolla.open.ling.io.File0#delDirectory(File)
	 */
	public static boolean delDirectory(String path)
	{
		boolean rtn = true;
		if(!Strings.isNullOrEmpty(path))
		{
			File f = new File(path);
			if(f.exists())
			{
				rtn = delDirectory(f);
			}
		}
		return rtn;
	}

	/**
	 * Delete all empty document under this directory.
	 * if the file is empty document delete it.
	 * 
	 * return false when some empty document delete failure,delete stop
	 * 
	 * @param file document or directory
	 * @return will return true with out false when some empty document delete failure
	 */
	public static boolean delEmptyFiles(File file)
	{
		boolean rtn = true;

		if(null != file && file.exists())
		{
			if(file.isDirectory() && file.list() != null)
			{
				for(File f : file.listFiles())
				{
					if(!f.isDirectory())
					{
						if(f.length() == 0)
						{
							rtn = rtn && f.delete();
						}
					}
				}
			}
			else
			{
				if(file.length() == 0)
				{
					rtn = rtn && file.delete();
				}
			}
		}
		return rtn;
	}

	/**
	 * @see org.azolla.open.ling.io.File0#delEmptyFiles(File)
	 */
	public static boolean delEmptyFiles(String path)
	{
		boolean rtn = true;
		if(!Strings.isNullOrEmpty(path))
		{
			File f = new File(path);
			if(f.exists())
			{
				rtn = delEmptyFiles(f);
			}
		}
		return rtn;
	}

	/**
	 * Delete all document under this directory.
	 * if the file is document delete it.
	 * 
	 * return false when some document delete failure,delete stop
	 * 
	 * @param file document or directory
	 * @return will return true with out false when some empty document delete failure
	 */
	public static boolean delFiles(File file)
	{
		boolean rtn = true;

		if(null != file && file.exists())
		{
			if(file.isDirectory() && file.list() != null)
			{
				for(File f : file.listFiles())
				{
					if(!f.isDirectory())
					{
						rtn = rtn && f.delete();
					}
				}
			}
			else
			{
				rtn = rtn && file.delete();
			}
		}
		return rtn;
	}

	/**
	 * @see org.azolla.open.ling.io.File0#delFiles(File)
	 */
	public static boolean delFiles(String path)
	{
		boolean rtn = true;
		if(!Strings.isNullOrEmpty(path))
		{
			File f = new File(path);
			if(f.exists())
			{
				rtn = delFiles(f);
			}
		}
		return rtn;
	}

	/**
	 * @see org.azolla.open.ling.io.File0#getFilesByTypes(List, List)
	 */
	public static List<File> getFilesByType(String fileType, List<File> files)
	{
		//		Preconditions.checkNotNull(fileType);
		List<String> typeList = Lists.newArrayList();

		// BE Best-effort
		if(!Strings.isNullOrEmpty(fileType))
		{
			typeList.add(fileType);
		}

		return getFilesByTypes(typeList, files);
	}

	/**
	 * filter files by file type list.
	 * 
	 * @param fileTypes file type list
	 * @param files	file list
	 * @return  file list of the file type in file type list
	 */
	public static List<File> getFilesByTypes(List<String> fileTypes, List<File> files)
	{
		//		Preconditions.checkNotNull(files);
		//		Preconditions.checkNotNull(fileTypes);
		List<File> rtnList = Lists.newArrayList();

		// BE Best-effort
		if(null == fileTypes || fileTypes.isEmpty() || null == files)
		{
			return rtnList;
		}

		for(File f : files)
		{
			if(!f.isDirectory())
			{
				if(fileTypes.contains(getFileType(f)))
				{
					rtnList.add(f);
				}
			}
		}
		return rtnList;
	}

	/**
	 * @see org.azolla.open.ling.io.File0#getFilesByType(String, List)
	 * @see org.azolla.open.ling.io.File0#files(String)
	 */
	public static List<File> getAllFilesByType(String fileType, String path)
	{
		return getFilesByType(fileType, files(path));
	}

	/**
	 * @see org.azolla.open.ling.io.File0#getFilesByTypes(List, List)
	 */
	public static List<File> getAllFilesByTypes(List<String> fileTypes, String path)
	{
		return getFilesByTypes(fileTypes, files(path));
	}

	/**
	 * @see org.azolla.open.ling.io.File0#getFileType(String)
	 */
	public static String getFileType(File file)
	{
		return getFileType(file.getName());
	}

	/**
	 * return type of file by file name
	 * example:test.txt -> txt
	 * 
	 * @param fileName file name
	 * @return type of file
	 */
	public static String getFileType(String fileName)
	{
		int lastPointIndex = fileName.lastIndexOf(".");
		return -1 == lastPointIndex ? fileName : fileName.substring(lastPointIndex + 1);
	}

	/**
	 * @see org.azolla.open.ling.io.File0#toLegalFileName(String, String)
	 */
	public static String toLegalFileName(String fileName)
	{
		return toLegalFileName(fileName, "_");
	}

	/**
	 * transform legal file name
	 * 
	 * @param fileName file name
	 * @param legalString legal string, if null or empty to "_"
	 * @return legal file name
	 */
	public static String toLegalFileName(String fileName, String legalString)
	{
		return fileName.replaceAll(ILLEGAL_FILENAME_REGEX, legalString);
	}

	public static String getUserDir()
	{
		return System.getProperty("user.dir");
	}

	public static String getUserHome()
	{
		return System.getProperty("user.home");
	}

	public static File newFile(String... strings)
	{
		return new File(Joiner.on(File.separator).join(strings));
	}

	public static File newFile(File parent, String... strings)
	{
		return new File(parent, Joiner.on(File.separator).join(strings));
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
