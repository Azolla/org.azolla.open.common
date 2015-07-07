/*
 * @(#)Integer0.java		Created at 2013-7-5
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.lang;

import org.azolla.l.ling.util.Log0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

/**
 * The coder is very lazy, nothing to write for this Integer0 class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Integer0
{
    public static boolean isInt(String s)
    {
        boolean rtnBoolean = false;
        if(!Strings.isNullOrEmpty(s))
        {
            try
            {
                Integer.parseInt(s);
                rtnBoolean = true;
            }
            catch(Exception e)
            {
                Log0.warn(Integer0.class, e.toString(), e);
            }
        }
        return rtnBoolean;
    }

    public static int nullToZero(Integer i)
    {
        return i == null ? 0 : i;
    }
}
