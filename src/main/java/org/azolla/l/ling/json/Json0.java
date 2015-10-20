/*
 * @(#)JSON0.java		Created at 15/6/22
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.json;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.azolla.l.ling.io.Close0;
import org.azolla.l.ling.util.KV;
import org.azolla.l.ling.util.Log0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Warp Json Function
 * fastjson is very fast
 * gson is very integral
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

    //the url file content just can be one row
    public static <T> T fromUrl(String jsonUrl, Class<T> classOfT)
    {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try
        {
            url = new URL(jsonUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            return new Gson().fromJson(bufferedReader, classOfT);
        }
        catch (IOException e)
        {
            Log0.error(Json0.class, KV.ins("jsonUrl", jsonUrl).put("classOfT", classOfT).toString(), e);
        }
        finally
        {
            Close0.close(bufferedReader);
            Close0.close(inputStreamReader);
            Close0.close(inputStream);
            Close0.disconnect(httpURLConnection);
        }
        return null;
    }

    //the url file content just can be one row
    public static <T> T fromUrl(String jsonUrl, Type typeOfT)
    {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try
        {
            url = new URL(jsonUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            return new Gson().fromJson(bufferedReader, typeOfT);
        }
        catch (IOException e)
        {
            Log0.error(Json0.class, KV.ins("jsonUrl", jsonUrl).put("typeOfT", typeOfT).toString(), e);
        }
        finally
        {
            Close0.close(bufferedReader);
            Close0.close(inputStreamReader);
            Close0.close(inputStream);
            Close0.disconnect(httpURLConnection);
        }
        return null;
    }
}
