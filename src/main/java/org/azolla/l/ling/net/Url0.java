/*
 * @(#)Url0.java		Created at 2013-10-31
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.net;

import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * The coder is very lazy, nothing to write for this Url0 class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class Url0
{
    /**
     * Example:[project or jar]/img/test.gif -&gt; path=/img/test.gif
     * <p/>
     *
     * full path:/img/test.gif
     * filename:i18n.properties
     *
     * @param path file path
     * @return URL
     */
    public static URL getURL(String path)
    {
        //callerFQC:The wrapper class' fully qualified class.
        URL rtnUrl = Thread.currentThread().getClass().getResource(path);
        if (rtnUrl != null)
        {
            return rtnUrl;
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null)
        {
            rtnUrl = classLoader.getResource(path);
            if (rtnUrl != null)
            {
                return rtnUrl;
            }
        }
        return ClassLoader.getSystemResource(path);
    }
}
