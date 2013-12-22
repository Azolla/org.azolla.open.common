/*
 * @(#)Close0.java		Created at 2013-12-21
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.io;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.Nullable;

import org.azolla.open.ling.exception.code.AzollaCode;
import org.azolla.open.ling.text.Fmt0;
import org.azolla.open.ling.util.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The coder is very lazy for this Close0 class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Close0
{
    private static final Logger LOG = LoggerFactory.getLogger(Close0.class);

    public static void close(@Nullable Closeable closeable)
    {
        if(closeable == null)
        {
            return;
        }
        try
        {
            closeable.close();
        }
        catch(IOException e)
        {
            LOG.error(Fmt0.LOG_EC_P_M, AzollaCode.IOEXCEPTION, KV.ins("closeable", closeable), e);
        }
    }
}
