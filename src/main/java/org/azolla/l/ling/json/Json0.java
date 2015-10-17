/*
 * @(#)JSON0.java		Created at 15/6/22
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.json;

import com.alibaba.fastjson.JSON;

/**
 * Warp Json Function
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class Json0
{
    public static String toJSONString(Object object)
    {
        return JSON.toJSONString(object);
    }


}
