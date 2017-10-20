/**
 * System               : GEHC RAW
 * System Version       : 1.0
 * File                 : AttachmentsUpload.java
 * Copyright            : Copyright (c) 2011 GE Healthcare.
 *                        This software is the confidential and proprietary
 *                        information of GEHC.
 * Version   Date        Developer          Description
 *  1.0    11-JULY-12    Praneeth MHS       Initial version
 */
package com.gehc.raw.pc.px;


import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IItem;
import com.agile.api.INode;
import com.agile.api.ItemConstants;
import com.agile.px.ActionResult;
import com.agile.px.EventActionResult;
import com.agile.px.IEventAction;
import com.agile.px.IEventInfo;
import com.gehc.raw.common.constants.RAWConstants;
import com.gehc.raw.common.utils.CRAgileSDKUtil;
import com.gehc.raw.common.utils.CRJavaUtil;
import com.gehc.raw.pc.handler.AttachmentsUploadHandler;

/**
 * 
 * Class to upload the attachments to a location
 */

public class AttachmentsUpload implements IEventAction {

	@SuppressWarnings("unused")
	private static ResourceBundle objResourceBundle = null;
	private static Logger objLogger = null;
	private StringBuilder sbPxMessage = new StringBuilder();

	/**
	 * Initialize Logger and ResourceBundle
	 */

	static {
		if (objLogger == null) {
			objLogger = Logger.getLogger(AttachmentsUpload.class);
		}
		CRJavaUtil.initAppLogger(AttachmentsUpload.class, CRJavaUtil
				.getApplicationProperties(RAWConstants.LOG4J_PROPERTIES));
		objResourceBundle = PropertyResourceBundle
				.getBundle("propertiesPX.AttachmentsUpload");
	}

	/**
	 * Event Starting point
	 * 
	 * @param agileSession
	 *            agile session passed by the system.
	 * @param node
	 *            agile node passed by the system.
	 * @param dataObject
	 *            agile data object passed by the system.
	 * 
	 * @return the ActionResult to be passed back to the system
	 */

	public EventActionResult doAction(IAgileSession objAgileSession,
			INode objNode, IEventInfo objEventInfo) {
		// TODO Auto-generated method stub
		
		try {
			//IUpdateTableEventInfo attachmentEvnt = (IUpdateTableEventInfo) objEventInfo;
			//String strCRObj = attachmentEvnt.getDataObject().toString();
			
			IItem crOBJ = (IItem)objAgileSession.getObject(ItemConstants.CLASS_DOCUMENTS_CLASS, "CR-000004");
			String strCRObj = crOBJ.toString();
			objLogger.info(" the CR object is: " + strCRObj);
			
			sbPxMessage.append(new AttachmentsUploadHandler(objAgileSession)
					.init(strCRObj));
			objLogger.info(sbPxMessage);
		} catch (APIException apiException) {
			CRJavaUtil.exception2String(apiException);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			CRJavaUtil.exception2String(exception);
		}

		ActionResult actionResult = new ActionResult(ActionResult.STRING,
				sbPxMessage.toString());
		return new EventActionResult(objEventInfo, actionResult);
	}

	/*
	 * Stand Alone
	 */

	public static void main(String[] args) {
		objLogger.info("Connecting to Agile Server......");
		objLogger.info("Please Stand by...");
		try {
			IAgileSession agileSession = new CRAgileSDKUtil()
					.createAgileSession(
							"http://qis-integration.health.ge.com/Agile",
							"502336084", "agile456$");
			System.out.println("testing");
			objLogger.info("Connected to agile-->>>");
			new AttachmentsUpload().doAction(agileSession, null, null);
		} catch (Exception ex) {
			CRJavaUtil.exception2String(ex);
		}
	}

}
