/*
 * @(#)Cfg0.java		Created at 2013-2-24
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.cfg;

import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.azolla.open.ling.io.Encoding0;
import org.azolla.open.ling.text.Fmt0;
import org.azolla.open.ling.util.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Cfg0
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Cfg0
{
    private static final Logger                              LOG   = LoggerFactory.getLogger(Cfg0.class);

    private static final LoadingCache<Class<?>, JAXBContext> CACHE = CacheBuilder.newBuilder().softValues().build(ConfigLoader.single());

    private static class ConfigLoader extends CacheLoader<Class<?>, JAXBContext>
    {
        private static ConfigLoader instance;

        private ConfigLoader()
        {

        }

        public static ConfigLoader single()
        {
            return null == instance ? new ConfigLoader() : instance;
        }

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
    public static <T> T unmarshal(Class<T> clazz, File file)
    {
        T rtnT = null;

        try
        {
            rtnT = (T) getJAXBContext(clazz).createUnmarshaller().unmarshal(file);
        }
        catch(Exception e)
        {
            LOG.error(Fmt0.LOG_P, KV.ins("clazz", clazz).put("filePath", file), e);
            rtnT = null;
        }

        return rtnT;
    }

    public static <T> boolean marshal(T t, File file)
    {
        return marshal(t, file, Encoding0.UTF_8);
    }

    /**
     * The coder is very lazy for this marshal method
     * 
     * @param t
     * @param file
     * @param encoding [null to {@code Encoding#UTF8}]
     * @return boolean
     */
    public static <T> boolean marshal(T t, File file, @Nullable String encoding)
    {
        boolean rtnBoolean = true;
        encoding = Strings.isNullOrEmpty(encoding) ? Encoding0.UTF_8 : encoding;
        try
        {
            Marshaller m = getJAXBContext(t.getClass()).createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_ENCODING, encoding);
            m.marshal(t, file);
        }
        catch(Exception e)
        {
            LOG.error(Fmt0.LOG_P, KV.ins("t", t).put("file", file).put("encoding", encoding), e);
            rtnBoolean = false;
        }

        return rtnBoolean;
    }
}
