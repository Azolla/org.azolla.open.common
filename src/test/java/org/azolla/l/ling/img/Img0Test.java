/*
 * @(#)Img0Test.java		Created at 15/4/13
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.img;

import org.azolla.l.ling.net.Url0;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class Img0Test
{
    @Test
    public void testQrcode()
    {
        System.out.println(Img0.qrcode("我是彭金胜，我的邮箱是：sk@shaneking.org",64,64, Paths.get("./target/sk@shaneking.org.png")));
    }
}
