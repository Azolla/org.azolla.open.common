/*
 * @(#)Img0.java		Created at 15/4/13
 * 
 * Copyright (c) azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.img;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.azolla.l.ling.io.Encoding0;
import org.azolla.l.ling.io.File0;
import org.azolla.l.ling.util.Log0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;

/**
 * The coder is very lazy, nothing to write for this class
 *
 * @author sk@azolla.org
 * @since ADK1.0
 */
public class Img0
{
    private static final      Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();

    static
    {
        hints.put(EncodeHintType.CHARACTER_SET, Encoding0.UTF_8);
        hints.put(EncodeHintType.MARGIN,0);
    }

    public static boolean qrcode(String content, int width, int height, Path path)
    {
        boolean rtn = true;
        try
        {
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//            matrix = CutWhiteBorder(matrix);
            MatrixToImageWriter.writeToPath(matrix, File0.PNG_FILETYPE, path);
        }
        catch (Exception e)
        {
            Log0.error(Img0.class, e.toString(), e);
            rtn = false;
        }

        return rtn;
    }
}
