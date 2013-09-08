/*
 * @(#)Fmts.java		Created at 2013-7-4
 * 
 * Copyright (c) 2011-2013 azolla.org All rights reserved.
 * Azolla PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package org.azolla.open.ling.text;

/**
 * The coder is very lazy, nothing to write for this Fmts class
 *
 * @author 	sk@azolla.org
 * @since 	ADK1.0
 */
public final class Fmt0
{
	public static final String	LOG_EC_P_M	= "[ErrorCode={}][{}]\n{}";	//EC=ErrorCode;P=Properties;M=Message

	public static final String	LOG_EC_P	= "[ErrorCode={}][{}]";	//EC=ErrorCode;P=Properties

	public static final String	LOG_P_M		= "[{}]\n{}";				//P=Properties;M=Message

	public static final String	LOG_P		= "[{}]";					//P=Properties
}
