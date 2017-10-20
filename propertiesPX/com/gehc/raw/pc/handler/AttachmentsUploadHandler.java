/*
 * System               : GEHC RAW
 * System Version       : 1.0
 * File                 : AttachmentsUploadHandler.java
 * Copyright            : Copyright (c) 2011 GE Healthcare.
 *                        This software is the confidential and proprietary
 *                        information of GEHC.
 * Version   Date        Developer          Description
 *  1.0    11-JULY-12    Praneeth MHS       Initial version
 */

package com.gehc.raw.pc.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IAttachmentFile;
import com.agile.api.IItem;
import com.agile.api.IRow;
import com.agile.api.ITable;
import com.agile.api.ITwoWayIterator;
import com.agile.api.ItemConstants;
import com.gehc.raw.common.constants.AttachmentsUploadConstants;
import com.gehc.raw.common.constants.RAWConstants;
import com.gehc.raw.common.utils.CRJavaUtil;

public class AttachmentsUploadHandler {

	private static ResourceBundle objResourceBundle = null;
	private static Logger objLogger = null;
	private StringBuilder sbPxMessage = new StringBuilder();
	private IAgileSession objAgileSession = null;

	/**
	 * Initialize Logger and ResourceBundle
	 */

	static {
		if (objLogger == null)
			objLogger = Logger.getLogger(AttachmentsUploadHandler.class);
		CRJavaUtil.initAppLogger(AttachmentsUploadHandler.class, CRJavaUtil
				.getApplicationProperties(RAWConstants.LOG4J_PROPERTIES));
		objResourceBundle = PropertyResourceBundle
				.getBundle("propertiesPX.AttachmentsUpload");

	}

	/**
	 * Constructor
	 * 
	 * @param objAgileSession
	 */

	public AttachmentsUploadHandler(IAgileSession objAgileSession) {
		this.objAgileSession = objAgileSession;
	}

	/**
	 * To initiate the Attachments Extraction
	 * 
	 * @return StringBuilder sbPxMessage
	 * @throws APIException
	 * @throws IOException
	 */

	public StringBuilder init(String strCRNumber) {

		int read = 0;
		byte[] bytes = new byte[1024];
		String strAttachmentName = null;
		String strAttachmentLocation = objResourceBundle
				.getString(AttachmentsUploadConstants.DESTINATION_FOLDER);
		objLogger
				.info("the destination location is:  " + strAttachmentLocation);
		String strAttachmentDestinName = null;

	
		try {

			// Getting the current Clinical research object
			IItem strCRObj = (IItem) objAgileSession.getObject(
					ItemConstants.CLASS_DOCUMENT, strCRNumber);
			objLogger.info("the CR object is " + strCRObj);

			// Get the attachment table for file attachments
			ITable attTable = strCRObj.getAttachments();
			if (attTable != null) {
				ITwoWayIterator it = attTable.getTableIterator();
				objLogger.info("inside the if loop");

				// Get the first attachment in the table
				while (it.hasNext()) {
					IRow row = (IRow) it.next();
					objLogger.info("inside the While loop");
					strAttachmentName = row.getName();
					objLogger.info("The file name is: " + strAttachmentName);

					// setting the destination location and file name
					strAttachmentDestinName = strAttachmentLocation
							.concat(strAttachmentName);
					objLogger.info("The Destination file path is: "
							+ strAttachmentDestinName);

					// Read and saving the contents of the stream
					InputStream stream = ((IAttachmentFile) row).getFile();
					
					
					File strDestFile = new File(strAttachmentDestinName);
					OutputStream out = new FileOutputStream(strDestFile);

					while ((read = stream.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}

					stream.close();
					out.flush();
					out.close();
				}
			}
		} catch (APIException apiException) {
			objLogger.info(CRJavaUtil.exception2String(apiException));
			sbPxMessage.append(objResourceBundle
					.getString(AttachmentsUploadConstants.MSG_FAILURE));

		} catch (IOException ieException) {
			// TODO Auto-generated catch block
			objLogger.info(CRJavaUtil.exception2String(ieException));
			sbPxMessage.append(objResourceBundle
					.getString(AttachmentsUploadConstants.MSG_FAILURE));
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			objLogger.info(CRJavaUtil.exception2String(exception));
			sbPxMessage.append(objResourceBundle
					.getString(AttachmentsUploadConstants.MSG_FAILURE));
		}

		objLogger.info(objResourceBundle
				.getString(AttachmentsUploadConstants.MSG_SUCCESS));
		sbPxMessage.append(objResourceBundle
				.getString(AttachmentsUploadConstants.MSG_SUCCESS));
		return sbPxMessage;

	}	 
	
}
