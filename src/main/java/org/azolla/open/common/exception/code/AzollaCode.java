/*
 * @(#)AzollaCode.java		Created at 2013-2-23
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.common.exception.code;

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
	FTP_LOGIN_ERROR(10001),			//[Connection is not open][Connection unexpectedly closed][Connection closed without indication]...
	FTP_LOGIN_FAILED(10002),		//[Username or Password incorrect]...
	FTP_SET_FILETYPE_ERROR(10003),	//[Connection is not open][Connection unexpectedly closed][Connection closed without indication]...

	UNKNOWN(1000000000);

	private final int	code;

	/**
	 * This is a constructor
	 *
	 */
	private AzollaCode(int code)
	{
		this.code = code;
	}

	/**
	 * @see org.azolla.open.common.exception.code.ErrorCoder#getCode()
	 * @return
	 */
	@Override
	public int getCode()
	{
		return code;
	}

}
