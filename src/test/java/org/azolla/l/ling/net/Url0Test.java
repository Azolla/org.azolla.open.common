/*
 * @(#)Url0Test.java		Created at 15/8/29
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.net;

import org.junit.Test;

import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class Url0Test
{
    @Test
    public void testClassLoader()
    {
        new Url0TestThread("A").start();
        new Url0TestThread("B").start();

        System.out.println(Thread.currentThread().getClass());
        System.out.println(Url0.class.getClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader());
        System.out.println(getTCL());
    }

    private static ClassLoader getTCL()
    {
        try
        {
            // Are we running on a JDK 1.2 or later system?
            Method method = null;
            try
            {
                method = Thread.class.getMethod("getContextClassLoader");
            }
            catch (NoSuchMethodException e)
            {
                // We are running on JDK 1.1
                return null;
            }
            return (ClassLoader) method.invoke(Thread.currentThread());
        }
        catch (InvocationTargetException e)
        {
            if (e.getTargetException() instanceof InterruptedException || e.getTargetException() instanceof InterruptedIOException)
            {
                Thread.currentThread().interrupt();
            }
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}

class Url0TestThread extends Thread
{
    Url0TestThread(String name)
    {
        super(name);
    }

    @Override
    public void run()
    {
        System.out.println(getName() + ":" + Thread.currentThread().getClass());
        System.out.println(getName() + ":" + Url0.class.getClassLoader());
        System.out.println(getName() + ":" + Thread.currentThread().getContextClassLoader());
        System.out.println(getName() + ":" + ClassLoader.getSystemClassLoader());
        System.out.println(getName() + ":" + getTCL());
    }

    private static ClassLoader getTCL()
    {
        try
        {
            // Are we running on a JDK 1.2 or later system?
            Method method = null;
            try
            {
                method = Thread.class.getMethod("getContextClassLoader");
            }
            catch (NoSuchMethodException e)
            {
                // We are running on JDK 1.1
                return null;
            }
            return (ClassLoader) method.invoke(Thread.currentThread());
        }
        catch (InvocationTargetException e)
        {
            if (e.getTargetException() instanceof InterruptedException || e.getTargetException() instanceof InterruptedIOException)
            {
                Thread.currentThread().interrupt();
            }
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
