/*
 * @(#)JSON0.java		Created at 15/6/22
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.azolla.l.ling.io.Close0;
import org.azolla.l.ling.lang.Class0;
import org.azolla.l.ling.lang.String0;
import org.azolla.l.ling.util.Log0;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class JSON0
{
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String object2String(Object object)
    {
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        String rtnString = String0.EMPTY;
        try
        {
            jsonGenerator = objectMapper.getFactory().createGenerator(writer);
            objectMapper.writeValue(jsonGenerator, object);
            rtnString = writer.toString();
        }
        catch(Exception e)
        {
            Log0.error(JSON0.class, e.toString(), e);
        }
        finally
        {
            Close0.close(jsonGenerator);
            Close0.close(writer);
        }
        return rtnString;
    }
}
