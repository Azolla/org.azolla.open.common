/*
 * @(#)AzollaCode.java		Created at 2013-2-23
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.exception.code;

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

	/** Component:FTP[10000,10099]*/
	FTP_CONNECT_ERROR(10000),		//[Host or Port error]...
	FTP_CONNECT_FAILED(10001),
	FTP_DISCONNECT_ERROR(10002),
	FTP_DISCONNECT_FAILED(10003),

	FTP_LOGIN_ERROR(10004),			//[Connection is not open][Connection unexpectedly closed][Connection closed without indication]...
	FTP_LOGIN_FAILED(10005),		//[Username or Password incorrect]...
	FTP_LOGOUT_ERROR(10006),
	FTP_LOGOUT_FAILED(10007),

	FTP_SET_FILETYPE_ERROR(10008),	//[Connection is not open][Connection unexpectedly closed][Connection closed without indication]...
	FTP_SET_FILETYPE_FAILED(10009),

	FTP_SET_MODE_ERROR(10010),
	FTP_SET_MODE_FAILED(10011),

	FTP_SET_TIMEOUT_ERROR(10012),
	FTP_SET_TIMEOUT_FAILED(10013),

	FTP_DELETE_FILE_ERROR(10014),
	FTP_DELETE_FILE_FAILED(10015),

	FTP_RETRIEVE_FILE_ERROR(10016),
	FTP_RETRIEVE_FILE_FAILED(10017),

	FTP_LIST_FILE_ERROR(10018),
	FTP_LIST_FILE_FAILED(10019),

	FTP_STORE_FILE_ERROR(10020),
	FTP_STORE_FILE_FAILED(10021),

	/** Component:ZIP[10100,10199]*/
	ZIP_ZIP_ERROR(10100),		//[Host or Port error]...

	/** Component:COMMAND[10200,10299]*/
	COMMAND_ERROR(10200),

	UNKNOWN(1000000000);

	private final int	code;

	/**
	 * This is a constructor
	 */
	private AzollaCode(int code)
	{
		this.code = code;
	}

	/**
	 * @see org.azolla.open.ling.exception.code.ErrorCoder#getCode()
	 * @return
	 */
	@Override
	public int getCode()
	{
		return code;
	}

}
