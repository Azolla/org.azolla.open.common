/*
 * @(#)PropCfg.java		Created at 15/8/26
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.cfg;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.azolla.l.ling.io.Close0;
import org.azolla.l.ling.util.KV;
import org.azolla.l.ling.util.Log0;

import javax.annotation.Nonnull;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class PropCfg implements LibCfg
{
    private String                       pathStartWithSlash        = null;
    private Properties                   prop            = new Properties();
    private LoadingCache<String, String> cfgCacheBuilder = CacheBuilder.newBuilder().build(new CacheLoader<String, String>()
    {
        @Override
        public String load(@Nonnull String key) throws Exception
        {
            return prop.getProperty(key);
        }
    });

    private PropCfg(@Nonnull String pathStartWithSlash)
    {
        this.pathStartWithSlash = pathStartWithSlash;
        refresh();
    }

    public static PropCfg cfg(String pathStartWithSlash)
    {
        return new PropCfg(pathStartWithSlash);
    }

    public String get(String key)
    {
        try
        {
            return cfgCacheBuilder.get(key);
        }
        catch (Exception e)
        {
            Log0.error(this.getClass(), e.toString(), e);
            throw new RuntimeException("Can't find {" + key + "}.");
        }
    }

    public void refresh()
    {
        refresh(pathStartWithSlash);
        try
        {
            InputStream inputStream = null;
            BufferedInputStream bufferedInputStream = null;
            try
            {
                prop = new Properties();
                inputStream = cfgFileCacheBuilder.get(pathStartWithSlash).openStream();
                bufferedInputStream = new BufferedInputStream(inputStream);
                prop.load(bufferedInputStream);
            }
            catch (Exception e)
            {
                Log0.error(this.getClass(), e.toString(), e);
                throw new RuntimeException(e.toString());
            }
            finally
            {
                Close0.close(bufferedInputStream);
                Close0.close(inputStream);
            }
        }
        catch (Exception e)
        {
            Log0.error(PropCfg.class, KV.ins("pathStartWithSlash", pathStartWithSlash).toString(), e);
            throw new RuntimeException("Can't find {" + pathStartWithSlash + "}.");
        }
        cfgCacheBuilder.cleanUp();
    }
}
