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
	AZOLLA(11);

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
