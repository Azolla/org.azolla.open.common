/*
 * @(#)Cfg0.java		Created at 2013-2-24
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.cfg;

import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.azolla.open.ling.io.Encoding;
import org.azolla.open.ling.text.Fmt0;
import org.azolla.open.ling.util.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * XSD (XML Schemas Definition) Model
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Cfg0
{
	private static final Logger									LOG		= LoggerFactory.getLogger(Cfg0.class);

	private static final LoadingCache<Class<?>, JAXBContext>	CACHE	= CacheBuilder.newBuilder().softValues().build(ConfigLoader.single());

	private static class ConfigLoader extends CacheLoader<Class<?>, JAXBContext>
	{
		private static ConfigLoader	instance;

		private ConfigLoader()
		{

		}

		public static ConfigLoader single()
		{
			return null == instance ? new ConfigLoader() : instance;
		}

		/**
		 * @see com.google.common.cache.CacheLoader#load(java.lang.Object)
		 * @param key
		 * @return
		 * @throws ExceptionF
		 */
		@Override
		public JAXBContext load(Class<?> key) throws Exception
		{
			return JAXBContext.newInstance(key);
		}
	}

	private static JAXBContext getJAXBContext(Class<?> clazz) throws ExecutionException
	{
		return CACHE.get(clazz);
	}

	/**
	 * Unmarshal XML data from the specified file
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(Class<T> clazz, File file)
	{
		T rtnT = null;

		try
		{
			rtnT = (T) getJAXBContext(clazz).createUnmarshaller().unmarshal(file);
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_P_M, KV.ins("clazz", clazz).set("filePath", file), e.toString(), e);
			rtnT = null;
		}

		return rtnT;
	}

	/**
	 * Marshal Ojbect to the specified file
	 */
	public static <T> boolean marshal(T t, File file)
	{
		return marshal(t, file, Encoding.UTF8);
	}

	/**
	 * Marshal Ojbect to File with Encoding
	 */
	public static <T> boolean marshal(T t, File file, Encoding encoding)
	{
		boolean rtnBoolean = true;
		try
		{
			Marshaller m = getJAXBContext(t.getClass()).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.setProperty(Marshaller.JAXB_ENCODING, encoding.getEncoding());
			m.marshal(t, file);
		}
		catch(Exception e)
		{
			LOG.error(Fmt0.LOG_P_M, KV.ins("t", t).set("file", file).set("encoding", encoding), e.toString(), e);	//既然已记录日志不应再抛出
			//			throw new AzollaException(AzollaCode.MODELHELPER_MARSHAL, e).set("t", t).set("filePath", filePath);
			rtnBoolean = false;
		}
		return rtnBoolean;
	}

	public static void reset()
	{
		CACHE.cleanUp();
	}
}
