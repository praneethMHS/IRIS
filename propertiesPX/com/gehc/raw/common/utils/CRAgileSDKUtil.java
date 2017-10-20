/*
 * System               : GEHC
 * System Version       : 1.0
 * File                 : AgileSDKUtil.java
 * Copyright            : Copyright (c) 2011 GE Healthcare.
 *                        This software is the confidential and proprietary
 *                        information of GEHC.
 * Version   Date        Developer          Description
 *  1.0    11-JULY-12    Praneeth MHS     Initial version
 */
package com.gehc.raw.common.utils;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.AgileSessionFactory;
import com.agile.api.IAgileSession;

/**
 * Implementation of Utilities containing Agile SDK methods that are commonly
 * used.
 */

public class CRAgileSDKUtil {

	private static Logger objAgileSDKLogger = null;
	private IAgileSession objAgileSession = null;
	/*
	 * Initializes static variables.
	 */
	static {
		objAgileSDKLogger = Logger.getLogger(CRAgileSDKUtil.class);
	}

	/**
	 * Creates a new instance of AgileSDKUtil
	 */
	public CRAgileSDKUtil() {
	}

	/**
	 * @return the objAgileSession
	 */
	public IAgileSession getObjAgileSession() {
		return objAgileSession;
	}

	/**
	 * @param objAgileSession
	 *            the objAgileSession to set
	 */
	public void setObjAgileSession(IAgileSession objAgileSession) {
		this.objAgileSession = objAgileSession;
	}

	/**
	 * Creates an Agile Session by from the Agile URL, Username and Password
	 * passed as parameters.
	 * 
	 * @param strAgileURL
	 *            containing Agile URL
	 * @param strUserName
	 *            containing Agile login username
	 * @param strPassword
	 *            containing Agile login password
	 * @return session a IAgileSession
	 */
	public IAgileSession createAgileSession(String strAgileURL,
			String strUserName, String strPassword) {
		HashMap<Integer, String> objUserParams = new HashMap<Integer, String>();

		objUserParams.put(AgileSessionFactory.URL, strAgileURL);
		objUserParams.put(AgileSessionFactory.USERNAME, strUserName);
		objUserParams.put(AgileSessionFactory.PASSWORD, strPassword);
		try {
			// Creating the session
			objAgileSession = AgileSessionFactory
					.createSessionEx(objUserParams);
		} catch (APIException apiEx) {
			objAgileSDKLogger.error(CRJavaUtil.exception2String(apiEx));
			apiEx.printStackTrace();
		}
		return objAgileSession;
	}

}
