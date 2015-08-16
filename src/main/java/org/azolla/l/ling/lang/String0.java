/*
 * @(#)String0.java		Created at 15/4/12
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.lang;

import com.google.common.base.Strings;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.azolla.l.ling.util.Log0;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class String0
{
    public static final String SLASH     = String.valueOf(Char0.SLASH);
    public static final String POINT     = String.valueOf(Char0.DOT);
    public static final String UNDERLINE = String.valueOf(Char0.UNDERLINE);
    public static final String EQUAL     = String.valueOf(Char0.EQUAL);
    public static final String COMMA     = String.valueOf(Char0.COMMA);

    public static final String EMPTY = "";

    public static final String SUCCEED = "Succeed";
    public static final String FAILED  = "Failed";

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITAL  = "0123456789";

    private static final HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();

    static
    {
        hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    public static String pinyin(String chineseString)
    {
        StringBuffer rtnStringBuffer = new StringBuffer();
        String[] pinyinStringArray = null;
        char pinyinChar = Char0.MINUS;

        for (int i = 0; i < chineseString.length(); i++)
        {
            pinyinChar = chineseString.charAt(i);

            if (' ' == pinyinChar)
            {
                rtnStringBuffer.append(Char0.MINUS);
            }
            else if (Char0.isAlphabetOrDigital(pinyinChar))
            {
                rtnStringBuffer.append(pinyinChar);
            }
            else
            {
                try
                {
                    pinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(pinyinChar, hanyuPinyinOutputFormat);
                    if ((null != pinyinStringArray) && (pinyinStringArray.length > 0))
                    {
                        rtnStringBuffer.append(pinyinStringArray[0]);
                    }
                    else
                    {
                        rtnStringBuffer.append(Char0.UNDERLINE);
                    }
                }
                catch (BadHanyuPinyinOutputFormatCombination e)
                {
                    Log0.warn(String0.class, e.toString(), e);
                }
            }
        }

        return rtnStringBuffer.toString().toLowerCase();
    }

    public static String nullOrEmptyTo(String string, String to)
    {
        return Strings.isNullOrEmpty(string) ? to : string;
    }

    public static String amp(String string)
    {
        return string.replaceAll("&", "&amp;");
    }

    public static String lt(String string)
    {
        return string.replaceAll("<", "&lt;");
    }

    public static String gt(String string)
    {
        return string.replaceAll(">", "&gt;");
    }

    public static String quot(String string)
    {
        return string.replaceAll("\"", "&quot;");
    }

    public static String html(String string)
    {
        return String0.quot(String0.gt(String0.lt(String0.amp(string))));
    }
}
