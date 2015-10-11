/*
 * @(#)I18N0.java		Created at 15/10/11
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.i18n;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.azolla.l.ling.cfg.PropCfg;
import org.azolla.l.ling.lang.String0;
import org.azolla.l.ling.net.Url0;
import org.azolla.l.ling.util.Array0;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 */
public class I18N0
{
    public static Integer PREFIX_LENGTH = "org.azolla.".length();
    public static String  SUFFIX_STRING = ".I18N.Azolla.properties";

    private static Map<String, I18N> packageI18NMap          = Maps.newHashMap();
    private static List<String>      reversedPackageNameList = Lists.newArrayList();


    public static I18N i18n(@Nonnull Class<?> clazz)
    {
        String packageName = clazz.getPackage().getName().substring(PREFIX_LENGTH);
        I18N i18n = packageI18NMap.get(packageName);
        if(i18n != null)
        {
            return i18n;
        }
        List<String> packageNameList = Arrays.asList(packageName.split("\\."));
        Collections.reverse(packageNameList);
        String reversedPackageName = Joiner.on(String0.POINT).join(packageNameList);
        reversedPackageNameList.clear();
        i18n = getI18N(reversedPackageName);
        if (i18n == null)
        {
            i18n = new I18N(null);
        }
        for (String forReversedPackageName : reversedPackageNameList)
        {
            packageI18NMap.put(forReversedPackageName, i18n);
        }
        packageI18NMap.put(packageName, i18n);

        return i18n;
    }

    //p.chenghu.cs.startup
    private static I18N getI18N(String reversedPackageName)
    {
        I18N rtnI18N = packageI18NMap.get(reversedPackageName);
        if (rtnI18N == null && !packageI18NMap.keySet().contains(reversedPackageName))
        {
            reversedPackageNameList.add(reversedPackageName);
            if (Url0.getURL(reversedPackageName + SUFFIX_STRING) != null)
            {
                rtnI18N = new I18N(PropCfg.cfg(reversedPackageName + SUFFIX_STRING));
            }
            else if (reversedPackageName.indexOf(String0.POINT) != -1)
            {
                rtnI18N = getI18N(reversedPackageName.substring(reversedPackageName.indexOf(String0.POINT) + 1));
            }
        }
        return rtnI18N;
    }

}
