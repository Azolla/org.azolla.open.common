/*
 * @(#)FileHelper.java		Created at 2013-2-24
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * Help for file
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
public class FileHelper
{
	public static final String	ILLEGAL_FILENAME_REGEX	= "[{/\\\\:*?\"<>|}]";

	/**
	 * @see org.azolla.open.common.io.FileHelper#allFiles(File)
	 */
	public static List<String> allFilePaths(File file)
	{
		return Lists.transform(allFiles(file), new Function<File, String>()
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
	 * @see org.azolla.open.common.io.FileHelper#allFilePaths(File)
	 */
	public static List<String> allFilePaths(String path)
	{
		List<String> rtnList = Lists.newArrayList();
		if(!Strings.isNullOrEmpty(path))
		{
			File f = new File(path);
			if(f.exists())
			{
				rtnList = allFilePaths(f);
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
	 * Throw exception with the file is null.
	 * 
	 * @param file document or directory
	 * @return the container with document
	 */
	public static List<File> allFiles(File file)
	{
		List<File> rtnList = Lists.newArrayList();

		if(null != file && file.exists())
		{
			if(file.isDirectory() && file.list() != null)
			{
				for(File f : file.listFiles())
				{
					rtnList.addAll(allFiles(f));
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
	 * @see org.azolla.open.common.io.FileHelper#allFiles(File)
	 */
	public static List<File> allFiles(String path)
	{
		List<File> rtnList = Lists.newArrayList();
		if(!Strings.isNullOrEmpty(path))
		{
			File f = new File(path);
			if(f.exists())
			{
				rtnList = allFiles(f);
			}
		}
		return rtnList;
	}

	/**
	 * Delete all empty document under this directory and sub directory.
	 * if the file is empty document delete it.
	 * 
	 * return false when some empty document delete failure,but delete is go on
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
	 * @see org.azolla.open.common.io.FileHelper#delAllEmptyFiles(File)
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
	 * @see org.azolla.open.common.io.FileHelper#delDirectory(File)
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
	 * return false when some empty document delete failure,but delete is go on
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
	 * @see org.azolla.open.common.io.FileHelper#delEmptyFiles(File)
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
	 * @see org.azolla.open.common.io.FileHelper#delFiles(File)
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
	 * @see org.azolla.open.common.io.FileHelper#getFilesByTypes(List, List)
	 */
	public static List<File> getFilesByType(String fileType, List<File> files)
	{
		//		Preconditions.checkNotNull(fileType);
		List<String> rtnList = Lists.newArrayList();

		// BE Best-effort
		if(!Strings.isNullOrEmpty(fileType))
		{
			rtnList.add(fileType);
		}

		return getFilesByTypes(rtnList, files);
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
		List<File> fileList = Lists.newArrayList();

		// BE Best-effort
		if(null == fileTypes || fileTypes.isEmpty() || null == files)
		{
			return fileList;
		}

		for(File f : files)
		{
			if(!f.isDirectory())
			{
				if(fileTypes.contains(getFileType(f)))
				{
					fileList.add(f);
				}
			}
		}
		return fileList;
	}

	/**
	 * @see org.azolla.open.common.io.FileHelper#getFilesByType(String, List)
	 * @see org.azolla.open.common.io.FileHelper#allFiles(String)
	 */
	public static List<File> getFilesByType(String fileType, String path)
	{
		return getFilesByType(fileType, allFiles(path));
	}

	/**
	 * @see org.azolla.open.common.io.FileHelper#getFilesByTypes(List, List)
	 */
	public static List<File> getFilesByTypes(List<String> fileTypes, String path)
	{
		return getFilesByTypes(fileTypes, allFiles(path));
	}

	/**
	 * @see org.azolla.open.common.io.FileHelper#getFileType(String)
	 */
	public static String getFileType(File file)
	{
		Preconditions.checkNotNull(file);
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
		Preconditions.checkNotNull(fileName);
		int lastPointIndex = fileName.lastIndexOf(".");
		return -1 == lastPointIndex ? fileName : fileName.substring(lastPointIndex + 1);
	}

	/**
	 * @see org.azolla.open.common.io.FileHelper#toLegalFileName(String, String)
	 */
	public static String toLegalFileName(String fileName)
	{
		return toLegalFileName(fileName, null);
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
		Preconditions.checkNotNull(fileName);
		return fileName.replaceAll(ILLEGAL_FILENAME_REGEX, Strings.isNullOrEmpty(legalString) ? "_" : legalString);
	}

	public static String getUserDir()
	{
		return System.getProperty("user.dir");
	}

	public static File newFile(String... strings)
	{
		Preconditions.checkNotNull(strings);
		return new File(Joiner.on(File.separator).join(strings));
	}

	public static File newFile(File parent, String... strings)
	{
		Preconditions.checkNotNull(parent);
		Preconditions.checkNotNull(strings);
		return new File(parent, Joiner.on(File.separator).join(strings));
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
