/*
 * @(#)Close0.java		Created at 2013-12-21
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.io;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.Nullable;

import org.azolla.l.ling.exception.code.AzollaCode;
import org.azolla.l.ling.text.Fmt0;
import org.azolla.l.ling.util.KV;
import org.azolla.l.ling.util.Log0;
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
            Log0.error(Close0.class, Fmt0.LOG_EC_P_M, AzollaCode.IOEXCEPTION, KV.ins("closeable", closeable), e);
        }
    }
}
