/*
 * @(#)Char0.java		Created at 15/4/13
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.lang;

import java.util.regex.Pattern;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class Char0
{
    public static boolean isAlphabet(char c)
    {
        return String0.ALPHABET.indexOf(c) > 0;
    }
}
