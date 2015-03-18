/*
 * @(#)AzollaCode.java		Created at 2013-2-23
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.l.ling.exception.code;

/**
 * Azolla Exception Code
 * 
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public enum AzollaCode implements ErrorCoder
{
    /** This code(0) is for exception out of Azolla */
    UNAZOLLA(0),

    /** This code(11) is for exception AzollaException Error, It like your feet */
    AZOLLA(11),

    /** Component-Java:Exception[10000,10999]*/
    IOEXCEPTION(10000),
    CLASSNOTFOUNDEXCEPTION(10002),
    PARSEEXCEPTION(10003),

    /** Component-Ling:FTP[20000,20099]*/
    FTP_CONNECT_ERROR(20000),		//[Host or Port error]...
    FTP_CONNECT_FAILED(20001),
    FTP_DISCONNECT_ERROR(20002),
    FTP_DISCONNECT_FAILED(20003),
    FTP_LOGIN_ERROR(20004),			//[Connection is not open][Connection unexpectedly closed][Connection closed without indication]...
    FTP_LOGIN_FAILED(20005),		//[Username or Password incorrect]...
    FTP_LOGOUT_ERROR(20006),
    FTP_LOGOUT_FAILED(20007),
    FTP_SET_FILETYPE_ERROR(20008),	//[Connection is not open][Connection unexpectedly closed][Connection closed without indication]...
    FTP_SET_FILETYPE_FAILED(20009),
    FTP_SET_MODE_ERROR(20010),
    FTP_SET_MODE_FAILED(20011),
    FTP_SET_TIMEOUT_ERROR(20012),
    FTP_SET_TIMEOUT_FAILED(20013),
    FTP_DELETE_FILE_ERROR(20014),
    FTP_DELETE_FILE_FAILED(20015),
    FTP_RETRIEVE_FILE_ERROR(20016),
    FTP_RETRIEVE_FILE_FAILED(20017),
    FTP_LIST_FILE_ERROR(20018),
    FTP_LIST_FILE_FAILED(20019),
    FTP_STORE_FILE_ERROR(20020),
    FTP_STORE_FILE_FAILED(20021),

    /** Component-Ling:ZIP[20100,20199]*/
    ZIP_ZIP_ERROR(20100),		//[Host or Port error]...

    /** Component-Ling:COMMAND[20200,20299]*/
    COMMAND_ERROR(20200),

    UNKNOWN(1000000000);

    private final int code;

    private AzollaCode(int code)
    {
        this.code = code;
    }

    @Override
    public int getCode()
    {
        return code;
    }

}
