/*
 * @(#)Class0.java		Created at 2013-10-31
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.lang;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.azolla.open.ling.exception.code.AzollaCode;
import org.azolla.open.ling.text.Fmt0;
import org.azolla.open.ling.util.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;

/**
 * The coder is very lazy, nothing to write for this Class0 class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public class Class0
{
    private static final Logger LOG      = LoggerFactory.getLogger(Class0.class);

    private Class<?>            clazz    = null;

    private Object              instance = null;

    public Class0(String className, Object... args) throws Exception
    {
        this(className, null, args);
    }

    @SuppressWarnings("rawtypes")
    public Class0(String className, @Nullable Class<?>[] argTypes, Object... args) throws Exception
    {
        // clazz = ClassLoader.getSystemClassLoader().loadClass(className);
        clazz = loadClass(className);
        if(argTypes == null)
        {
            instance = clazz.getConstructor(Lists.transform(Lists.newArrayList(args), new Function<Object, Class>()
            {
                @Override
                public Class apply(Object input)
                {
                    return input.getClass();
                }
            }).toArray(new Class[args.length])).newInstance(args);
        }
        else
        {
            instance = clazz.getConstructor(argTypes).newInstance(args);
        }
    }

    public <T> T execMethod(Class<T> returnType, String methodName, Object... args) throws Exception
    {
        return execMethod(returnType, methodName, null, args);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> T execMethod(Class<T> returnType, String methodName, @Nullable Class<?>[] argTypes, Object... args) throws Exception
    {
        Method m = null;
        if(argTypes == null)
        {
            m = clazz.getMethod(methodName, Lists.transform(Lists.newArrayList(args), new Function<Object, Class>()
            {
                @Override
                public Class apply(Object input)
                {
                    return input.getClass();
                }
            }).toArray(new Class[args.length]));

        }
        else
        {
            m = clazz.getMethod(methodName, argTypes);
        }
        return (T) m.invoke(Modifier.isStatic(m.getModifiers()) ? null : instance, args);
    }

    public Class<?> loadClass(String className) throws Exception
    {
        return Class.forName(className);
    }

    public static boolean exist(String className)
    {
        boolean rtnBoolean = true;
        try
        {
            Class.forName(className);
        }
        catch(ClassNotFoundException e)
        {
            LOG.warn(Fmt0.LOG_EC_P_M, AzollaCode.CLASSNOTFOUNDEXCEPTION, KV.ins("className", className), e);
            rtnBoolean = false;
        }
        return rtnBoolean;
    }
}
