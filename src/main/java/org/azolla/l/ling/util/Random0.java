/*
 * @(#)Random0.java		Created at 2014年10月28日
 * 
 * Copyright (c) 2011-2014 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.util;

import java.security.SecureRandom;

/**
 * The coder is very lazy for this Random0 class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class Random0
{
    private static final SecureRandom SR = new SecureRandom();

    public static int nextInt()
    {
        return SR.nextInt();
    }

    public static int nextAbsInt()
    {
        return Math.abs(SR.nextInt());
    }

    /**
     * Return [0,max)
     * 
     * range代表的是一个可以测量的范围，在这个范围内包括一系列可变化的数量。range还特指视力、听力所能达到的距离和枪炮的射程。
     * scope指人们所处理、研究的事物的“范围”以及所掌握、控制的“面积”。scope的比喻含义指的是所掌握的知识的宽度，即“眼界，见识”。
     * 
     * @param max max
     * @return int
     */
    public static int nextRangeInt(int max)
    {
        return Math.abs(SR.nextInt()) % max;
    }
}
