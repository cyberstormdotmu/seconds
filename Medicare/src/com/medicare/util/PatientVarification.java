package com.medicare.util;

import au.gov.hic.hiconline.client.api.*;
import au.gov.hic.hiconline.client.core.*;
import java.util.*;

// D:/WORKSPACES/Excel_Audit_Checker_Workspace/Medicare/resources/lib
// D:/WORKSPACES/Excel_Audit_Checker_Workspace/Medicare/resources/newSample.jks
// D:/WORKSPACES/Excel_Audit_Checker_Workspace/Medicare/WebContent/WEB-INF/LogicPack
/**
 *
 * @author polestar01
 */
public class PatientVarification {



	public void hicCreateDBS() {
		String versionId = null;
		String majorVersionId = null;
		int sessionId;
		String sessionIdStr;
		int rval = 0;
		String[] parms = null;
		Vector result = new Vector();

		// versionId of HicOnlineAPI
		versionId = EasyclaimAPI.getVersionId();
		System.out.println("getVersionId result : " + versionId);

		majorVersionId = EasyclaimAPI.getMajorVersionId();
		System.out.println("getMajorVersionId result : " + majorVersionId);
		
		sessionId = EasyclaimAPI.getInstance().createSessionEasyclaim("D:/WORKSPACES/Excel_Audit_Checker_Workspace/Medicare/resources/newSample.jks", "medicare");
		System.out.println("createEasyclaimSession result : " + sessionId);
		sessionIdStr = String.valueOf(sessionId);

		rval = EasyclaimAPI.getInstance().setSessionElement(sessionId,"Recipient", "ebus.test@medicareaustralia.gov.au");
		System.out.println("setSessionElement (Recipient, ebus.test@medicareaustralia.gov.au) result : "+ rval);

		rval = EasyclaimAPI.getInstance().setSessionElement(sessionId,"Server", "http://test.mcoe.humanservices.gov.au/ext");
		System.out.println("setSessionElement (Server, http://test.mcoe.humanservices.gov.au/ext) result : "+ rval);

		rval = EasyclaimAPI.getInstance().setSessionElement(sessionId,"LocationId", "HPC00000");
		System.out.println("setSessionElement (LocationId, HPC00000) result : "+ rval);

		rval = EasyclaimAPI.getInstance().setSessionElement(sessionId,"PmsProduct", "Hippocamp");
		System.out.println("setSessionElement (PmsProduct, Hippocamp) result : "+ rval);

		rval = EasyclaimAPI.getInstance().setSessionElement(sessionId,"PmsVersion", "1.0");
		System.out.println("setSessionElement (PmsVersion, 1.0) result : "+ rval);

		rval = EasyclaimAPI.getInstance().setSessionElement(sessionId,"TransmissionType", "P");
		System.out.println("setSessionElement (TransmissionType, P) result : "+ rval);

		rval = EasyclaimAPI.getInstance().setSessionElement(sessionId,"LogicPackDir","D:/WORKSPACES/Excel_Audit_Checker_Workspace/Medicare/WebContent/WEB-INF/LogicPack");
		System.out.println("setSessionElement (LogicPackDir, D:/WORKSPACES/Excel_Audit_Checker_Workspace/Medicare/resource/LogicPack result : "+ rval);

		result = new Vector(); // initialise the vector before passing it
		rval = EasyclaimAPI.getInstance().createBusinessObject(sessionId,"HIC/HolMedical/PatientVerificationRequest@1", "", "", result);
		System.out.println("createBusinessObject (HIC/HolClassic/Dbs@1,,) : result : "+ rval);

		if (result.size() > 0) {
			String boPath = (String) result.get(0);
			System.out.println("Path to this claim is : " + boPath);

			rval = EasyclaimAPI.getInstance().listBusinessObject(sessionId,
					boPath, "S", result);
			System.out.println("listBusinessObject : result : " + rval);
			System.out.println("listBusinessObject : result : " + result);

			rval = EasyclaimAPI.getInstance().setBusinessObjectElement(sessionId, boPath, "OPVTypeCde", "PVM");
			System.out.println("setBusinessObjectElement OPVTypeCde: result : "	+ rval);
			
			rval = EasyclaimAPI.getInstance().setBusinessObjectElement(sessionId, boPath, "PatientDateOfBirth", "15081998");
			System.out.println("setBusinessObjectElement PatientDateOfBirth: result : "	+ rval);
			
			rval = EasyclaimAPI.getInstance().setBusinessObjectElement(sessionId, boPath, "PatientFamilyName", "GEORGE");
			System.out.println("setBusinessObjectElement PatientFamilyName: result : "	+ rval);
			
			rval = EasyclaimAPI.getInstance().setBusinessObjectElement(sessionId, boPath, "PatientFirstName", "PAU12L");
			System.out.println("setBusinessObjectElement PatientFirstName: result : "+ rval);
			
			rval = EasyclaimAPI.getInstance().setBusinessObjectElement(sessionId, boPath, "PatientGender", "M");
			System.out.println("setBusinessObjectElement PatientGender: result : "	+ rval);
			
			rval = EasyclaimAPI.getInstance().setBusinessObjectElement(sessionId, boPath, "PatientMedicareCardNum", "3950325181");
			System.out.println("setBusinessObjectElement PatientMedicareCardNum: result : "	+ rval);
			
			rval = EasyclaimAPI.getInstance().setBusinessObjectElement(sessionId, boPath, "PatientReferenceNum", "1");
			System.out.println("setBusinessObjectElement PatientReferenceNum: result : "	+ rval);
			
			Vector resultAuthoriseContent = new Vector(); // initialise the vector before passing it
			rval = EasyclaimAPI.getInstance().authoriseContent(sessionId, "medicare", resultAuthoriseContent);
			System.out.println("authoriseContent : result : " + rval);
			System.out.println("authoriseContent : Value : "+ resultAuthoriseContent);

			Vector resultGetContent = new Vector(); // initialise the vector before passing it
			String getContentValue = null;
			rval = EasyclaimAPI.getInstance().getContent(sessionId,resultGetContent);
			System.out.println("getContent result : " + rval);
	
			getContentValue = (String) resultGetContent.get(0);
			System.out.println("getContent value result : "+ getContentValue);

			rval = EasyclaimAPI.getInstance().createTransmission(sessionId, "HIC/HolMedical/PatientVerificationRequest@1");
			System.out.println("createTransmission result >>>>>>>>>>> " + rval);
			
			rval = EasyclaimAPI.getInstance().includeContent(sessionId);
			System.out.println("include content result >>>>>>>>>>> " + rval);
			
			rval = EasyclaimAPI.getInstance().setTransmissionElement(sessionId, "LocationId", "HPC00000");
			System.out.println("setTransmissionElement LocationId result : "+ rval);

			rval = EasyclaimAPI.getInstance().setTransmissionElement(sessionId, "DateOfTransmission", "03102016");
			System.out.println("setTransmissionElement DateOfTransmission result : "+ rval);
			
			rval = EasyclaimAPI.getInstance().sendTransmission(sessionId);
			System.out.println("sendTransmission result : " + rval);
			
		}

		rval = EasyclaimAPI.getInstance().isReportAvailable(sessionId);
		System.out.println("isReportAvailable : result : " + rval);
		
		Vector resultReportData = new Vector();
		
		rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "../TransactionId",resultReportData);
		System.out.println("transaction id : result value: " + resultReportData);
		
		rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "../StatusCode",resultReportData);
		System.out.println("status code : result : " + rval);
		System.out.println("status code id : result value: " + resultReportData);
		
		rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "MedicareStatusCode",resultReportData);
		System.out.println("medicare  status  code : result : " + rval);
		System.out.println("medicare  status code id : result value: " + resultReportData);
		
		rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "CurrentPatientMedicareCardNum",resultReportData);
		System.out.println("currentMembership : result value: " + resultReportData);
		
		rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "CurrentPatientFirstName",resultReportData);
		System.out.println("CurrentPatientFirstName : result value: " + resultReportData);
		
		rval = EasyclaimAPI.getInstance().resetSession(sessionId);
		System.out.println("resetSession : result : " + rval);
	}

	public static void main(String[] args) {
		PatientVarification claim = new PatientVarification();
		claim.hicCreateDBS();
		System.exit(0);
	}

}
