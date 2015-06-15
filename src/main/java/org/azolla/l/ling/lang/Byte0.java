/*
 * @(#)Byte0.java		Created at 15/5/3
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.lang;

import org.azolla.l.ling.text.Fmt0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class Byte0
{
    private static final Logger LOG = LoggerFactory.getLogger(Byte0.class);

    public static final  String MD5 = "MD5";

    public static String md5(byte[] bytes)
    {
        String rtnString = null;
        try
        {
            rtnString = new BigInteger(1, MessageDigest.getInstance(MD5).digest(bytes)).toString(16);
        }
        catch (Exception e)
        {
            LOG.error(Fmt0.LOG_P, e);
        }

        return rtnString;
    }
}
