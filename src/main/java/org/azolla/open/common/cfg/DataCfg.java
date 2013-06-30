/*
 * @(#)DataCfg.java		Created at 2013-2-24
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.cfg;

import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * XSD (XML Schemas Definition) Model
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class DataCfg
{
	private static final Logger									LOG		= LoggerFactory.getLogger(DataCfg.class);

	private static final LoadingCache<Class<?>, JAXBContext>	CACHE	= CacheBuilder.newBuilder().softValues()
																				.build(new ConfigLoader());

	private static class ConfigLoader extends CacheLoader<Class<?>, JAXBContext>
	{

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

	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(Class<T> clazz, String filePath)
	{
		Preconditions.checkNotNull(clazz);
		Preconditions.checkNotNull(filePath);
		try
		{
			return (T) getJAXBContext(clazz).createUnmarshaller().unmarshal(new File(filePath));
		}
		catch(Exception e)
		{
			LOG.error("clazz = [{}], filePath = [{}]", clazz, filePath, e.toString(), e);
			return null;
		}
	}

	public static <T> void marshal(T t, String filePath)
	{
		Preconditions.checkNotNull(t);
		Preconditions.checkNotNull(filePath);
		try
		{
			getJAXBContext(t.getClass()).createMarshaller().marshal(t, new File(filePath));
		}
		catch(Exception e)
		{
			LOG.error("t = [{}], filePath = [{}]", t, filePath, e.toString(), e);	//既然已记录日志不应再抛出
			//			throw new AzollaException(AzollaCode.MODELHELPER_MARSHAL, e).set("t", t).set("filePath", filePath);
		}
	}

	public static void reset()
	{
		CACHE.cleanUp();
	}
}
