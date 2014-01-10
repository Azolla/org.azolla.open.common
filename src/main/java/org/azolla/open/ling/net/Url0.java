/*
 * @(#)Url0.java		Created at 2013-10-31
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.net;

import java.net.URL;

/**
 * The coder is very lazy, nothing to write for this Url0 class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class Url0
{
    /**
     * Example:[project or jar]/img/test.gif => path=/img/test.gif 
     * 
     * @param path
     * @return URL
     */
    public static URL getURL(String path)
    {
        return Url0.class.getClass().getResource(path);
    }
}
