/*
 * System               : GEHC 
 * System Version       : 1.0
 * File                 : CRJavaUtil.java
 * Copyright            : Copyright (c) 2011 GE Healthcare.
 *                        This software is the confidential and proprietary
 *                        information of GEHC.
 * Version   Date        Developer          Description
 *  1.0    11-JULY-12    Praneeth MHS       Initial version
 */
package com.gehc.raw.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.PropertyConfigurator;

/**
 * Implementation of Utilities containing methods that are commonly used.
 */
public class CRJavaUtil {

	private static PropertyResourceBundle objAppResources = null;

	/*
	 * Initializes static variables.
	 */
	static {
		objAppResources = (PropertyResourceBundle) PropertyResourceBundle
				.getBundle("commonproperties.CRRAWApp");
	}

	/**
	 * Returns a value that corresponds to the specified key in the Resource
	 * Bundle GEHCConstants.properties
	 * 
	 * @param strKey
	 *            a key for the desired string
	 * @return the string for the given key
	 */
	public static String getApplicationProperties(String strKey) {

		return objAppResources.getString(strKey);
	}

	/**
	 * Returns a localized Message that corresponds to the specified key in the
	 * Resource Bundle GEHCMessages.properties
	 * 
	 * @param strKey
	 *            - the key for the desired string
	 * @param args
	 *            - Array of Arguments to append
	 * @return the string for the given key
	 */
	public static String getLocalizedMessage(ResourceBundle objMsgResources,
			String strKey, Object[] args) {

		String strLocalized = objMsgResources.getString(strKey);

		return java.text.MessageFormat.format(strLocalized, (Object[]) args);
	}

	/**
	 * Initializes the Logger for the Application.
	 * 
	 * @param objClassName
	 *            - name of the java class
	 * @param strFileName
	 *            - log4j Configuration file
	 */
	public static void initAppLogger(Class<?> objClassName, String strFileName) {
		try {
			Properties objProperties = new Properties();
			InputStream objInputStream = objClassName.getClassLoader()
					.getResourceAsStream(strFileName);
			if (objInputStream == null) {
				System.out
						.println("Could not locate the Config file for Log4j.");
			} else
				objProperties.load(objInputStream);
			PropertyConfigurator.configure(objProperties);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

	}

	/**
	 * Converts the stack trace of an Throwable to String
	 * 
	 * @param objThrowable
	 *            an error or exception message
	 * @return stack trace as String
	 */
	public static String exception2String(Throwable objThrowable) {

		StringWriter objStringWriter = new StringWriter();
		PrintWriter objPrintWriter = new PrintWriter(objStringWriter, true);
		objThrowable.printStackTrace(objPrintWriter);
		objPrintWriter.flush();
		objPrintWriter.close();
		return objStringWriter.toString();
	}

}
