/*
 * @(#)List0.java		Created at 2013-7-29
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.util;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.azolla.l.ling.lang.Char0;
import org.azolla.l.ling.lang.String0;

/**
 * The coder is very lazy, nothing to write for this List0 class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class List0
{
    public static <T> String list2String(List<T> list)
    {
        return list2String(list, null);
    }

    public static <T> String list2StringWithoutNull(List<T> list)
    {
        return list2StringWithoutNull(list, null);
    }

    /**
     * @see org.azolla.l.ling.json.Json0#object2String(Object)
     * @param list
     * @param separator
     * @return
     */
    @Deprecated
    public static <T> String list2String(List<T> list, @Nullable String separator)
    {
        separator = Strings.isNullOrEmpty(separator) ? String.valueOf(Char0.SEMICOLON) : separator;
        if(null == list)
        {
            return Null0.string;
        }
        StringBuffer rtn = new StringBuffer();
        for(T t : list)
        {
            rtn.append(separator).append(String.valueOf(t));
        }
        return rtn.length() > 0 ? rtn.substring(separator.length()) : String0.EMPTY;
    }

    /**
     * @see org.azolla.l.ling.json.Json0#object2String(Object)
     * @param list
     * @param separator
     * @return
     */
    @Deprecated
    public static <T> String list2StringWithoutNull(List<T> list, @Nullable String separator)
    {
        separator = Strings.isNullOrEmpty(separator) ? String.valueOf(Char0.SEMICOLON) : separator;
        if(null == list)
        {
            return Null0.string;
        }
        StringBuffer rtn = new StringBuffer();
        for(T t : list)
        {
            if(t != null)
            {
                rtn.append(separator).append(String.valueOf(t));
            }
        }
        return rtn.length() > 0 ? rtn.substring(separator.length()) : String0.EMPTY;
    }

    public static <T> List<T> listNotExistInOther(List<T> list, List<T> other)
    {
        List<T> rtnList = Lists.newArrayList();
        for(T t : list)
        {
            if(!other.contains(t))
            {
                rtnList.add(t);
            }
        }
        return rtnList;
    }

    public static <T> List<T> listExistInOther(List<T> list, List<T> other)
    {
        List<T> rtnList = Lists.newArrayList();
        for(T t : list)
        {
            if(other.contains(t))
            {
                rtnList.add(t);
            }
        }
        return rtnList;
    }
}
