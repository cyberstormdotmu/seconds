package com.medicare.services;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.medicare.util.Log4jInit;
import com.medicare.util.MedicareUtil;
import au.gov.hic.hiconline.client.api.EasyclaimAPI;
import au.gov.hic.hiconline.client.core.CreateReportController;

@Path("/patientService")
public class PatientService {

	private Logger log = Logger.getLogger(Log4jInit.class);
	int sessionId = 0;
	int rval = 0;
	String outputText = "";
	String indented_json;
	static String reportData;
	DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
	DateFormat timeFormat = new SimpleDateFormat("HHmmss");
	Date date = new Date();
	Vector<String> ErrortextVector = new Vector<>();

	//verify() method is used to verify a patient by medicare 
	@GET
	@Path("/verify")
	public String verifyPatient(@QueryParam("OPVTypeCde") String OPVTypeCde,
			@QueryParam("PatientDateOfBirth") String patientDateOfBirth,
			@QueryParam("PatientFamilyName") String patientFamilyName,
			@QueryParam("PatientFirstName") String patientFirstName,
			@QueryParam("PatientGender")String patientGender,
			@QueryParam("PatientMedicareCardNum")String patientMedicareCardNum,
			@QueryParam("PatientReferenceNum")String patientReferenceNum
			) throws JsonGenerationException, JsonMappingException, IOException{
		int sessionId = 0;
		int rval = 0;
		String patientVerification = "HIC/HolMedical/PatientVerificationRequest@2";
		sessionId=createSessionEasyclaimService();

		Vector<String> result = new Vector<String>(); // Initialize the vector before passing it
		rval = EasyclaimAPI.getInstance().createBusinessObject(sessionId,patientVerification, "", "", result);
		log.debug("createBusinessObject (HIC/HolMedical/PatientVerificationRequest@2,,) : result : "+ rval);
		outputText = checkErrorMessage(sessionId ,rval);

		if (result.size() > 0) {
			String boPath = (String) result.get(0);
			log.debug("Path to this claim is : " + boPath);

			rval = EasyclaimAPI.getInstance().listBusinessObject(sessionId,
					boPath, "S", result);
			outputText = checkErrorMessage(sessionId ,rval);
			log.debug("listBusinessObject : result : " + rval);
			log.debug("listBusinessObject : result : " + result);

			Map<String, String> patientVerificationMap = new HashMap<>();
			patientVerificationMap.put("OPVTypeCde", OPVTypeCde);
			patientVerificationMap.put("PatientDateOfBirth", patientDateOfBirth);
			patientVerificationMap.put("PatientFamilyName", patientFamilyName);
			patientVerificationMap.put("PatientFirstName", patientFirstName);
			patientVerificationMap.put("PatientGender",patientGender);
			patientVerificationMap.put("PatientMedicareCardNum", patientMedicareCardNum);
			patientVerificationMap.put("PatientReferenceNum", patientReferenceNum);

			rval = setBusinessObjectByMap(patientVerificationMap, boPath, sessionId);

			outputText = sendContent(sessionId, patientVerification);
			if(!outputText.equals("9501") && !outputText.equals("0") && !outputText.equals("")){
				return outputText;
			}
			rval = EasyclaimAPI.getInstance().isReportAvailable(sessionId);
			log.debug("isReportAvailable : result : " + rval);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, String> resultPatientMap = new HashMap<String, String>();
			Vector<String> resultReportData = new Vector<String>();

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "../TransactionId",resultReportData);
			log.debug("transaction id : result value: " + resultReportData);

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "../StatusCode",resultReportData);
			log.debug("status code : result : " + rval);
			log.debug("status code id : result value: " + resultReportData);

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "MedicareStatusCode",resultReportData);
			log.debug("medicare  status  code : result : " + rval);
			log.debug("medicare  status code id : result value: " + resultReportData);
			resultMap.put("MedicareStatusCode", resultReportData.get(0));

			if(resultReportData.get(0).equals("0")){
				resultMap.put("message", "Patient is verified");
				resultPatientMap.put("CurrentPatientMedicareCardNum", patientMedicareCardNum);
				resultPatientMap.put("CurrentPatientFirstName", patientFirstName);
			}

			else{
				EasyclaimAPI.getInstance().getErrorText(sessionId,resultReportData.get(0).toString(),ErrortextVector);
				log.debug(ErrortextVector.get(0));
				resultMap.put("message", ErrortextVector.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "CurrentPatientMedicareCardNum",resultReportData);
				log.debug("currentMembership : result value: " + resultReportData);
				if(rval == 0)
					resultPatientMap.put("CurrentPatientMedicareCardNum", resultReportData.get(0));
				else
					resultPatientMap.put("CurrentPatientMedicareCardNum", patientMedicareCardNum);

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "CurrentPatientFirstName",resultReportData);
				log.debug("CurrentPatientFirstName : result value: " + resultReportData);
				if(rval == 0)
					resultPatientMap.put("CurrentPatientFirstName", resultReportData.get(0));

			}

			resultPatientMap.put("CurrentPatientReferenceNum", patientReferenceNum);
			resultPatientMap.put("Family Name",patientFamilyName);
			resultMap.put("Patient", resultPatientMap);
			outputText = new ObjectMapper().writeValueAsString(resultMap);

			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(outputText, Object.class);
			indented_json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

		}

		rval = EasyclaimAPI.getInstance().resetSession(sessionId);
		log.debug("resetSession : result : " + rval);
		return indented_json;
	}



	@GET
	@Path("/DVAClaim") //To create DVA claim request
	public String patientDVAClaim(@QueryParam("PmsClaimId") String pmsClaimId,
			@QueryParam("PayeeProviderNum") String payeeProviderNum,
			@QueryParam("ClaimCertifiedDate")String claimCertifiedDate,
			@QueryParam("ClaimCertifiedInd")String claimCertifiedInd,
			@QueryParam("AuthorisationDate")String authorisationDate,
			@QueryParam("ServiceTypeCde")String serviceTypeCde,
			@QueryParam("ServicingProviderNum")String servicingProviderNum,
			@QueryParam("VeteranFileNum")String veteranFileNum,
			@QueryParam("PatientFirstName")String patientFirstName,
			@QueryParam("PatientFamilyName")String patientFamilyName,
			@QueryParam("PatientDateOfBirth")String patientDateOfBirth,
			@QueryParam("PatientGender")String patientGender,
			@QueryParam("PatientAliasFirstName")String patientAliasFirstName,
			@QueryParam("PatientAliasFamilyName")String patientAliasFamilyName,
			@QueryParam("PatientAddressLocality")String patientAddressLocality,
			@QueryParam("PatientAddressPostcode")String patientAddressPostcode,
			@QueryParam("TimeOfService")String timeOfService,
			/*@QueryParam("AcceptedDisabilityInd")String AcceptedDisabilityInd, 
			 * above commented element is optional */
			@QueryParam("TreatmentLocationCde")String treatmentLocationCde,
			@QueryParam("ChargeAmount")String chargeAmount,
			@QueryParam("ItemNum")String itemNum,
			@QueryParam("SCPId")String SCPId,
			@QueryParam("SelfDeemedCde")String selfDeemedCde
			){
		int sessionId = 0;
		int rval = 0;
		String DVAClaim = "HIC/HolDva/DVAClaimRequest@6";

		sessionId=createSessionEasyclaimService();
		log.debug("session Id : "+ sessionId);
		Vector<String> result = new Vector<String>(); // initialise the vector before passing it
		rval = EasyclaimAPI.getInstance().createBusinessObject(sessionId,DVAClaim, "", "", result);
		log.debug("createBusinessObject (HIC/HolDva/DVAClaimRequest@6,,) : result : "+ rval);

		if (result.size() > 0) {
			String boPath = (String) result.get(0);
			log.debug("Path to this claim is : " + boPath);

			rval = EasyclaimAPI.getInstance().listBusinessObject(sessionId, boPath, "S", result);
			log.debug("listBusinessObject : result : " + rval);
			log.debug("listBusinessObject : result : " + result);

			Map<String, String> dvaClaimMap = new HashMap<>();
			dvaClaimMap.put("PmsClaimId", pmsClaimId);
			dvaClaimMap.put("PayeeProviderNum", payeeProviderNum);
			dvaClaimMap.put("ClaimCertifiedDate", claimCertifiedDate);
			dvaClaimMap.put("ClaimCertifiedInd", claimCertifiedInd);
			dvaClaimMap.put("AuthorisationDate", authorisationDate);
			dvaClaimMap.put("ServiceTypeCde", serviceTypeCde);
			dvaClaimMap.put("ServicingProviderNum", servicingProviderNum);

			rval = setBusinessObjectByMap(dvaClaimMap, boPath, sessionId); 

			Vector<String> resultUniqueId = new Vector<String>();
			String boUniqueId = null;
			rval = EasyclaimAPI.getInstance().getUniqueId(sessionId,resultUniqueId);
			if (resultUniqueId.size() > 0) {
				boUniqueId = (String) resultUniqueId.get(0);
				log.debug("getUniqueId  result : " + boUniqueId);
			}

			rval = EasyclaimAPI.getInstance().setTransmissionElement(sessionId, "TransactionId",(String) resultUniqueId.get(0));
			log.debug("setTransmissionElement TransactionId: result: "+ rval);

			Vector<String> resultVoucher = new Vector<String>(); // initialise the vector before passing it
			rval = EasyclaimAPI.getInstance().createBusinessObject(sessionId,"DVAVoucher", boPath, "01", resultVoucher);
			log.debug("createBusinessObject Voucher : result : "+ rval);

			if (resultVoucher.size() > 0) {
				String boVucherPath = (String) resultVoucher.get(0);
				log.debug("Path to this Voucher is : " + boVucherPath);

				Map<String, String> dvaClaimVoucherMap  = new HashMap<>();
				dvaClaimVoucherMap.put("VeteranFileNum",veteranFileNum);
				dvaClaimVoucherMap.put("PatientFirstName",patientFirstName);
				dvaClaimVoucherMap.put("PatientFamilyName",patientFamilyName);
				dvaClaimVoucherMap.put("PatientDateOfBirth",patientDateOfBirth);
				dvaClaimVoucherMap.put("PatientGender",patientGender);
				dvaClaimVoucherMap.put("PatientAliasFirstName",patientAliasFirstName);
				dvaClaimVoucherMap.put("PatientAliasFamilyName",patientAliasFamilyName);
				dvaClaimVoucherMap.put("PatientAddressLocality",patientAddressLocality);
				dvaClaimVoucherMap.put("PatientAddressPostcode",patientAddressPostcode);

				/*dvaClaimVoucherMap.put("AcceptedDisabilityInd",AcceptedDisabilityInd);
				 *dvaClaimVoucherMap.put("PatientAddressLocality","Y");
				 *above fields are optional */

				dvaClaimVoucherMap.put("DateOfService",dateFormat.format(date));
				dvaClaimVoucherMap.put("TimeOfService",timeOfService);
				dvaClaimVoucherMap.put("TreatmentLocationCde",treatmentLocationCde);

				rval = setBusinessObjectByMap(dvaClaimVoucherMap, boVucherPath, sessionId);

				Vector<String> resultService = new Vector<String>(); // initialise the vector before passing it
				rval = EasyclaimAPI.getInstance().createBusinessObject(sessionId, "DVAService", boVucherPath, "0002",resultService);
				log.debug("createBusinessObject Service : result : "+ rval);

				if (resultService.size() > 0) {
					String boServicePath = (String) resultService.get(0);
					log.debug("Path to this Service is : "+ boServicePath);

					Map<String, String> dvaClaimServiceMap = new HashMap<>();
					dvaClaimServiceMap.put("ItemNum", itemNum);
					dvaClaimServiceMap.put("ChargeAmount", chargeAmount);
					dvaClaimServiceMap.put("SCPId", SCPId);
					dvaClaimServiceMap.put("SelfDeemedCde", selfDeemedCde);

					rval = setBusinessObjectByMap(dvaClaimServiceMap, boServicePath, sessionId);
					outputText = sendContent(sessionId,DVAClaim);
					if(outputText.equals("9501")){
						outputText = "Claim has been successfully submitted";
					}
				}	
			}
		}

		rval = EasyclaimAPI.getInstance().resetSession(sessionId);
		log.debug("resetSession : result : " + rval);

		rval = EasyclaimAPI.getInstance().unloadEasyclaim();
		log.debug("isReportAvailable : result : " + rval);


		return outputText;
	}

	@GET
	@Path("/DBsClaim") // This method is used to create BulkBill Claim 
	public String patientDirectBillClaim(@QueryParam("PmsClaimId") String pmsClaimId,
			@QueryParam("ServicingProviderNum") String servicingProviderNum,
			@QueryParam("PayeeProviderNum")String payeeProviderNum,
			@QueryParam("ServiceTypeCde")String serviceTypeCde,
			@QueryParam("BenefitAssignmentAuthorised")String benefitAssignmentAuthorised,
			@QueryParam("DateOfService")String dateOfService,
			@QueryParam("PatientDateOfBirth")String patientDateOfBirth,
			@QueryParam("PatientFamilyName")String patientFamilyName,
			@QueryParam("PatientMedicareCardNum")String patientMedicareCardNum,
			@QueryParam("PatientFirstName")String patientFirstName,
			@QueryParam("PatientReferenceNum")String patientReferenceNum,
			@QueryParam("ReferralOverrideTypeCde")String referralOverrideTypeCde,
			@QueryParam("TimeOfService")String timeOfService,
			@QueryParam("ChargeAmount")String chargeAmount,
			@QueryParam("ItemNum")String itemNum,
			@QueryParam("ChargeAmount1")String chargeAmount1,
			@QueryParam("ItemNum1")String itemNum1
			){
		int sessionId = 0;
		int rval = 0;
		String DBSClaim = "HIC/HolClassic/DirectBillClaim@1";

		sessionId=createSessionEasyclaimService();

		Vector<String> result = new Vector<String>(); // initialize the vector before passing it
		rval = EasyclaimAPI.getInstance().createBusinessObject(sessionId,DBSClaim, "", "", result);
		log.debug("createBusinessObject (HIC/HolClassic/Dbs@1,,) : result : "+ rval);

		if (result.size() > 0) {
			String boPath = (String) result.get(0);
			log.debug("Path to this claim is : " + boPath);

			rval = EasyclaimAPI.getInstance().listBusinessObject(sessionId,
					boPath, "S", result);
			log.debug("listBusinessObject : result : " + rval);
			log.debug("listBusinessObject : result : " + result);

			Map<String, String> dbsClaimMap = new HashMap<>();
			dbsClaimMap.put("PmsClaimId", pmsClaimId);
			dbsClaimMap.put("ServicingProviderNum", servicingProviderNum);
			dbsClaimMap.put("PayeeProviderNum", payeeProviderNum);
			dbsClaimMap.put("ServiceTypeCde", serviceTypeCde);

			rval = setBusinessObjectByMap(dbsClaimMap, boPath, sessionId);

			Vector<String> resultUniqueId = new Vector<String>();
			String boUniqueId = null;
			rval = EasyclaimAPI.getInstance().getUniqueId(sessionId,resultUniqueId);
			if (resultUniqueId.size() > 0) {
				boUniqueId = (String) resultUniqueId.get(0);
				log.debug("getUniqueId  result : " + boUniqueId);
			}

			rval = EasyclaimAPI.getInstance().setTransmissionElement(sessionId, "TransactionId",(String) resultUniqueId.get(0));
			log.debug("setTransmissionElement TransactionId: result: "+ rval);

			Vector<String> resultVoucher = new Vector<String>(); // initialise the vector before passing it
			rval = EasyclaimAPI.getInstance().createBusinessObject(sessionId,"Voucher", boPath, "01", resultVoucher);
			log.debug("createBusinessObject Voucher : result : "+ rval);

			if (resultVoucher.size() > 0) {
				String boVucherPath = (String) resultVoucher.get(0);
				log.debug("Path to this Voucher is : " + boVucherPath);

				Map<String, String> dbsClaimVoucherMap = new HashMap<>();
				dbsClaimVoucherMap.put("BenefitAssignmentAuthorised", benefitAssignmentAuthorised);
				dbsClaimVoucherMap.put("DateOfService", dateFormat.format(date));
				dbsClaimVoucherMap.put("PatientDateOfBirth",patientDateOfBirth);
				dbsClaimVoucherMap.put("PatientFamilyName", patientFamilyName);
				dbsClaimVoucherMap.put("PatientMedicareCardNum", patientMedicareCardNum);
				dbsClaimVoucherMap.put("PatientFirstName",patientFirstName);
				dbsClaimVoucherMap.put("PatientReferenceNum", patientReferenceNum);
				dbsClaimVoucherMap.put("ReferralOverrideTypeCde", referralOverrideTypeCde);
				dbsClaimVoucherMap.put("TimeOfService", timeOfService);

				rval = setBusinessObjectByMap(dbsClaimVoucherMap, boVucherPath, sessionId);

				Vector<String> resultService = new Vector<String>(); // initialise the vector before passing it
				rval = EasyclaimAPI.getInstance().createBusinessObject(sessionId, "Service", boVucherPath, "0001",resultService);
				log.debug("createBusinessObject Service : result : "+ rval);

				if (resultService.size() > 0) {

					String boServicePath = (String) resultService.get(0);
					log.debug("Path to this Service is : "+ boServicePath);

					Map<String, String> dbsClaimServiceMap = new HashMap<>();
					dbsClaimServiceMap.put("ChargeAmount", chargeAmount);
					dbsClaimServiceMap.put("ItemNum", itemNum);

					rval = setBusinessObjectByMap(dbsClaimServiceMap, boServicePath, sessionId);
					outputText = sendContent(sessionId , DBSClaim);
					if(outputText.equals("0")){
						outputText = "Claim has been successfully submitted";
					}
					log.debug("send content result :"+rval);

				}	
			}
		}

		rval = EasyclaimAPI.getInstance().resetSession(sessionId);
		log.debug("resetSession : result : " + rval);
		return outputText;
	}

	@GET
	@Path("/BulkBillReporting") //This method is for generating bulk bill report Payment or processing report
	public String bulkBillReport(@QueryParam("ReportName") String ReportName,
			@QueryParam("PayeeProviderNum") String PayeeProviderNum,
			@QueryParam("ClaimId") String ClaimId,
			@QueryParam("DateOfService") String DateOfService) throws JsonGenerationException, JsonMappingException, IOException{

		int sessionId = 0;
		int rval = 0;
		sessionId=createSessionEasyclaimService();
		String ClaimIDforReport=PayeeProviderNum+ClaimId+DateOfService;
		log.debug("Report Identification String pattern "+"PayeeProviderNumClaimIdDateOdService"+" : " + ClaimIDforReport);

		rval=EasyclaimAPI.getInstance().createReport(sessionId, ReportName, ClaimIDforReport);
		log.debug("Report Response for "+ReportName+" : " + rval); 
		outputText = checkErrorMessage(sessionId ,rval);	 
		if(rval!=0){
			return outputText;
		}

		int reportcount=0;
		while(reportcount == 0){

			Map<String, String> resultDataMap = new HashMap<String, String>();
			Vector<String> resultReportData = new Vector<String>();

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "../TransactionId",resultReportData);
			log.debug("transaction id : result value: " + resultReportData);

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "../StatusCode",resultReportData);
			log.debug("status code : result : " + rval);
			log.debug("status code id : result value: " + resultReportData);

			if(ReportName.equalsIgnoreCase("DBProcessingReport")){

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ChargeAmount",resultReportData);
				log.debug("ChargedAmount code : result : " + rval);
				log.debug("ChargedAmount : result value: " + resultReportData);
				resultDataMap.put("ChargeAmount", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ClaimBenefitPaid",resultReportData);
				log.debug("ClaimBenefitPaid code : result : " + rval);
				log.debug("ClaimBenefitPaid : result value: " + resultReportData);
				resultDataMap.put("ClaimBenefitPaid", resultReportData.get(0));

				if(Integer.parseInt(resultReportData.get(0))==0)
				{
					resultDataMap.put("PaymentStatus", "NOT DONE");
				}
				else 
				{
					resultDataMap.put("PaymentStatus", "DONE");
				}

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ServiceBenefitAmount",resultReportData);
				log.debug("ServiceBenefitAmount code : result : " + rval);
				log.debug("ServiceBenefitAmount : result value: " + resultReportData);
				resultDataMap.put("ServiceBenefitAmount", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "DateOfService",resultReportData);
				log.debug("DateOfService code : result : " + rval);
				log.debug("DateOfService : result value: " + resultReportData);
				resultDataMap.put("DateOfService", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ExplanationCode",resultReportData);
				log.debug("ExplanationCode code : result : " + rval);
				log.debug("ExplanationCode : result value: " + resultReportData);
				resultDataMap.put("ExplanationCode", resultReportData.get(0));	

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ItemNum",resultReportData);
				log.debug("ItemNum code : result : " + rval);
				log.debug("ItemNum : result value: " + resultReportData);
				resultDataMap.put("ItemNum", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PatientFirstName",resultReportData);
				log.debug("PatientFirstName code : result : " + rval);
				log.debug("PatientFirstName : result value: " + resultReportData);
				resultDataMap.put("PatientFirstName", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PatientFamilyName",resultReportData);
				log.debug("PatientFamilyName code : result : " + rval);
				log.debug("PatientFamilyName : result value: " + resultReportData);
				resultDataMap.put("PatientFamilyName", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ServiceId",resultReportData);
				log.debug("ServiceId code : result : " + rval);
				log.debug("ServiceId : result value: " + resultReportData);
				resultDataMap.put("ServiceId", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ServicingProviderNum",resultReportData);
				log.debug("ServicingProviderNum code : result : " + rval);
				log.debug("ServicingProviderNum : result value: " + resultReportData);
				resultDataMap.put("ServicingProviderNum", resultReportData.get(0));	

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ClaimChargeAmount",resultReportData); // Total amount of all services
				log.debug("ClaimChargeAmount code : result : " + rval);
				log.debug("ClaimChargeAmount : result value: " + resultReportData);
				resultDataMap.put("ClaimChargeAmount", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PatientReferenceNum",resultReportData);
				log.debug("PatientReferenceNum code : result : " + rval);
				log.debug("PatientReferenceNum  : result value: " + resultReportData);
				resultDataMap.put("PatientReferenceNum", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PatientMedicareCardNum",resultReportData);
				log.debug("PatientMedicareCardNum code : result : " + rval);
				log.debug("PatientMedicareCardNum : result value: " + resultReportData);
				resultDataMap.put("PatientMedicareCardNum", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "MedicareCardFlag",resultReportData);
				log.debug("MedicareCardFlag code : result : " + rval);
				log.debug("MedicareCardFlag : result value: " + resultReportData);
				resultDataMap.put("MedicareCardFlag", resultReportData.get(0)); //DBS

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PmsClaimId",resultReportData);
				log.debug("PmsClaimId code : result : " + rval);
				log.debug("PmsClaimId  : result value: " + resultReportData);
				resultDataMap.put("PmsClaimId", resultReportData.get(0));  //DBS

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "VoucherId",resultReportData);
				log.debug("VoucherId code : result : " + rval);
				log.debug("VoucherId : result value: " + resultReportData);
				resultDataMap.put("VoucherId", resultReportData.get(0)); //DBS

				resultDataMap.put("Claim Type", "BulkBill"); //DBS

			}

			else if(ReportName.equalsIgnoreCase("DBPaymentReport")) {

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "BSBCode",resultReportData);
				log.debug("BSBCode code : result : " + rval);
				log.debug("BSBCode : result value: " + resultReportData);
				resultDataMap.put("BSBCode", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "BankAccountNum",resultReportData);
				log.debug("BankAccountNum code : result : " + rval);
				log.debug("BankAccountNum : result value: " + resultReportData);
				resultDataMap.put("BankAccountNum", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "BankAccountName",resultReportData);
				log.debug("BankAccountName code : result : " + rval);
				log.debug("BankAccountName : result value: " + resultReportData);
				resultDataMap.put("BankAccountName", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ClaimDate",resultReportData);
				log.debug("ClaimDate code : result : " + rval);
				log.debug("ClaimDate : result value: " + resultReportData);
				resultDataMap.put("ClaimDate", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ClaimBenefitPaid",resultReportData);
				log.debug("ClaimBenefitPaid code : result : " + rval);
				log.debug("ClaimBenefitPaid : result value: " + resultReportData);
				resultDataMap.put("ClaimBenefitPaid", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ClaimChargeAmount",resultReportData);
				log.debug("ClaimChargeAmount code : result : " + rval);
				log.debug("ClaimChargeAmount : result value: " + resultReportData);
				resultDataMap.put("ClaimChargeAmount", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "DepositAmount",resultReportData);
				log.debug("DepositAmount code : result : " + rval);
				log.debug("DepositAmount : result value: " + resultReportData);
				resultDataMap.put("DepositAmount", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PaymentRunDate",resultReportData);
				log.debug("PaymentRunDate code : result : " + rval);
				log.debug("PaymentRunDate  : result value: " + resultReportData);
				resultDataMap.put("PaymentRunDate", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PaymentRunNum",resultReportData);
				log.debug("PaymentRunNum code : result : " + rval);
				log.debug("PaymentRunNum  : result value: " + resultReportData);
				resultDataMap.put("PaymentRunNum", resultReportData.get(0));

				rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PmsClaimId",resultReportData);
				log.debug("PmsClaimId code : result : " + rval);
				log.debug("PmsClaimId  : result value: " + resultReportData);
				resultDataMap.put("PmsClaimId", resultReportData.get(0));		

				outputText = new ObjectMapper().writeValueAsString(resultDataMap);
				ObjectMapper mapper = new ObjectMapper();
				Object json = mapper.readValue(outputText, Object.class);
				indented_json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);


			}
			else
			{

				resultDataMap.put("Message", "Please enter a valid Report name for Bulk Bill"); 

			}

			Map<String , Object> resultMap= new HashMap<String, Object>();
			resultMap.put("Claim Details", resultDataMap);
			outputText = new ObjectMapper().writeValueAsString(resultMap);
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(outputText, Object.class);
			indented_json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			reportcount = EasyclaimAPI.getInstance().getNextReportRow(sessionId);
		}

		reportData=indented_json;
		rval = EasyclaimAPI.getInstance().resetSession(sessionId);
		log.debug("resetSession : result : " + rval);

		return indented_json;
	}



	@GET
	@Path("/DVAProcessingReport") //To access DVA Processing report 
	public String dvaProcessingReport(@QueryParam("PayeeProviderNum") String PayeeProviderNum,
			@QueryParam("ClaimId") String ClaimId,
			@QueryParam("DateOfService") String DateOfService) throws JsonGenerationException, JsonMappingException, IOException{

		String statusReport = "HIC/HolDva/DVAProcessingReportRequest@6";

		sessionId=createSessionEasyclaimService();
		Vector<String> result = new Vector<String>(); // initialise the vector before passing it
		rval = EasyclaimAPI.getInstance().createBusinessObject(sessionId,statusReport, "", "", result);

		outputText = checkErrorMessage(sessionId ,rval);

		if (result.size() > 0) {
			String boPath = (String) result.get(0);
			log.debug("Path to this claim is : " + boPath); 
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			rval = EasyclaimAPI.getInstance().listBusinessObject(sessionId,
					boPath, "S", result);
			Map<String, String> dbsClaimVoucherMap = new HashMap<>();
			dbsClaimVoucherMap.put("PayeeProviderNum", PayeeProviderNum);
			dbsClaimVoucherMap.put("PmsClaimId", ClaimId);
			dbsClaimVoucherMap.put("ClaimDate",DateOfService);
			dbsClaimVoucherMap.put("SelectionCriteria",PayeeProviderNum+ClaimId+DateOfService);


			rval = setBusinessObjectByMap(dbsClaimVoucherMap, boPath, sessionId);

			outputText = sendContent(sessionId , statusReport);
			if(!outputText.equals("9501")){
				return outputText;
			}
			log.debug("send content result :"+rval);
		}


		int reportcount=0;
		while(reportcount == 0){

			Map<String, String> resultDataMap = new HashMap<String, String>();
			Vector<String> resultReportData = new Vector<String>();

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "../TransactionId",resultReportData);
			log.debug("transaction id : result value: " + resultReportData);

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "../StatusCode",resultReportData);
			log.debug("status code : result : " + rval);
			log.debug("status code id : result value: " + resultReportData);

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ChargeAmount",resultReportData);
			log.debug("ChargedAmount code : result : " + rval);
			log.debug("ChargedAmount : result value: " + resultReportData);
			resultDataMap.put("ChargeAmount", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ClaimBenefitPaid",resultReportData);
			log.debug("ClaimBenefitPaid code : result : " + rval);
			log.debug("ClaimBenefitPaid : result value: " + resultReportData);
			resultDataMap.put("ClaimBenefitPaid", resultReportData.get(0));

			if(Integer.parseInt(resultReportData.get(0))==0)
			{
				resultDataMap.put("PaymentStatus", "NOT DONE");
			}
			else 
			{
				resultDataMap.put("PaymentStatus", "DONE");
			}

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ServiceBenefitAmount",resultReportData);
			log.debug("ServiceBenefitAmount code : result : " + rval);
			log.debug("ServiceBenefitAmount : result value: " + resultReportData);
			resultDataMap.put("ServiceBenefitAmount", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "DateOfService",resultReportData);
			log.debug("DateOfService code : result : " + rval);
			log.debug("DateOfService : result value: " + resultReportData);
			resultDataMap.put("DateOfService", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ExplanationCode",resultReportData);
			log.debug("ExplanationCode code : result : " + rval);
			log.debug("ExplanationCode : result value: " + resultReportData);
			resultDataMap.put("ExplanationCode", resultReportData.get(0));	

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ItemNum",resultReportData);
			log.debug("ItemNum code : result : " + rval);
			log.debug("ItemNum : result value: " + resultReportData);
			resultDataMap.put("ItemNum", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PatientFirstName",resultReportData);
			log.debug("PatientFirstName code : result : " + rval);
			log.debug("PatientFirstName : result value: " + resultReportData);
			resultDataMap.put("PatientFirstName", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getBusinessObjectCondition(sessionId, "HIC/HolDva/DVAClaimRequest@6", "PatientFamilyName", resultReportData);
			log.debug("PatientFamilyName code : result : " + rval);
			log.debug("PatientFamilyName : result value: " + resultReportData);
			resultDataMap.put("PatientFamilyName", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PatientFamilyName",resultReportData);
			log.debug("PatientFamilyName code : result : " + rval);
			log.debug("PatientFamilyName : result value: " + resultReportData);
			resultDataMap.put("PatientFamilyName", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ServiceId",resultReportData);
			log.debug("ServiceId code : result : " + rval);
			log.debug("ServiceId : result value: " + resultReportData);
			resultDataMap.put("ServiceId", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ServicingProviderNum",resultReportData);
			log.debug("ServicingProviderNum code : result : " + rval);
			log.debug("ServicingProviderNum : result value: " + resultReportData);
			resultDataMap.put("ServicingProviderNum", resultReportData.get(0));	

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "VeteranFileNum",resultReportData);
			log.debug("VeteranFileNum code : result : " + rval);
			log.debug("VeteranFileNum : result value: " + resultReportData);
			resultDataMap.put("VeteranFileNum", resultReportData.get(0));  //DVA

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "AccountReferenceNum",resultReportData);
			log.debug("AccountReferenceNum code : result : " + rval);
			log.debug("AccountReferenceNum : result value: " + resultReportData);
			resultDataMap.put("AccountReferenceNum", resultReportData.get(0)); //DVA

			resultDataMap.put("Claim Type", "DVA"); //DBS



			Map<String , Object> resultMap= new HashMap<String, Object>();
			resultMap.put("Claim Details", resultDataMap);
			outputText = new ObjectMapper().writeValueAsString(resultMap);
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(outputText, Object.class);
			indented_json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			reportcount = EasyclaimAPI.getInstance().getNextReportRow(sessionId);
		}

		reportData=indented_json;
		rval = EasyclaimAPI.getInstance().resetSession(sessionId);
		log.debug("resetSession : result : " + rval);

		return indented_json;
	}


	@GET
	@Path("/DVAPaymentReport") // To access DVA Payment report 
	public String dvaPaymentReport(@QueryParam("PayeeProviderNum") String PayeeProviderNum,
			@QueryParam("ClaimId") String ClaimId,
			@QueryParam("DateOfService") String DateOfService) throws JsonGenerationException, JsonMappingException, IOException{

		String dvaPaymentReport = "HIC/HolDva/DVAPaymentReportRequest@6";

		sessionId=createSessionEasyclaimService();
		Vector<String> result = new Vector<String>(); // initialise the vector before passing it
		rval = EasyclaimAPI.getInstance().createBusinessObject(sessionId,dvaPaymentReport, "", "", result);

		outputText = checkErrorMessage(sessionId ,rval);

		if (result.size() > 0) {
			String boPath = (String) result.get(0);
			log.debug("Path to this claim is : " + boPath); 

			rval = EasyclaimAPI.getInstance().listBusinessObject(sessionId,
					boPath, "S", result);
			Map<String, String> dbsClaimVoucherMap = new HashMap<>();
			dbsClaimVoucherMap.put("PayeeProviderNum", PayeeProviderNum);
			dbsClaimVoucherMap.put("PmsClaimId", ClaimId);
			dbsClaimVoucherMap.put("ClaimDate",DateOfService);
			dbsClaimVoucherMap.put("SelectionCriteria",PayeeProviderNum+ClaimId+DateOfService);


			rval = setBusinessObjectByMap(dbsClaimVoucherMap, boPath, sessionId);

			outputText = sendContent(sessionId , dvaPaymentReport);
			if(!outputText.equals("9501")){
				return outputText;
			}
			log.debug("send content result :"+rval);
		}


		int reportcount=0;
		while(reportcount == 0){

			Map<String, String> resultDataMap = new HashMap<String, String>();
			Vector<String> resultReportData = new Vector<String>();

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "../TransactionId",resultReportData);
			log.debug("transaction id : result value: " + resultReportData);

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "../StatusCode",resultReportData);
			log.debug("status code : result : " + rval);
			log.debug("status code id : result value: " + resultReportData);

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "BSBCode",resultReportData);
			log.debug("BSBCode code : result : " + rval);
			log.debug("BSBCode : result value: " + resultReportData);
			resultDataMap.put("BSBCode", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "BankAccountNum",resultReportData);
			log.debug("BankAccountNum code : result : " + rval);
			log.debug("BankAccountNum : result value: " + resultReportData);
			resultDataMap.put("BankAccountNum", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "BankAccountName",resultReportData);
			log.debug("BankAccountName code : result : " + rval);
			log.debug("BankAccountName : result value: " + resultReportData);
			resultDataMap.put("BankAccountName", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ClaimDate",resultReportData);
			log.debug("ClaimDate code : result : " + rval);
			log.debug("ClaimDate : result value: " + resultReportData);
			resultDataMap.put("ClaimDate", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ClaimBenefitPaid",resultReportData);
			log.debug("ClaimBenefitPaid code : result : " + rval);
			log.debug("ClaimBenefitPaid : result value: " + resultReportData);
			resultDataMap.put("ClaimBenefitPaid", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "ClaimChargeAmount",resultReportData);
			log.debug("ClaimChargeAmount code : result : " + rval);
			log.debug("ClaimChargeAmount : result value: " + resultReportData);
			resultDataMap.put("ClaimChargeAmount", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "DepositAmount",resultReportData);
			log.debug("DepositAmount code : result : " + rval);
			log.debug("DepositAmount : result value: " + resultReportData);
			resultDataMap.put("DepositAmount", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PaymentRunDate",resultReportData);
			log.debug("PaymentRunDate code : result : " + rval);
			log.debug("PaymentRunDate  : result value: " + resultReportData);
			resultDataMap.put("PaymentRunDate", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PaymentRunNum",resultReportData);
			log.debug("PaymentRunNum code : result : " + rval);
			log.debug("PaymentRunNum  : result value: " + resultReportData);
			resultDataMap.put("PaymentRunNum", resultReportData.get(0));

			rval = EasyclaimAPI.getInstance().getReportElement(sessionId, "PmsClaimId",resultReportData);
			log.debug("PmsClaimId code : result : " + rval);
			log.debug("PmsClaimId  : result value: " + resultReportData);
			resultDataMap.put("PmsClaimId", resultReportData.get(0));		

			Map<String , Object> resultMap= new HashMap<String, Object>();
			resultMap.put("Claim Details", resultDataMap);
			outputText = new ObjectMapper().writeValueAsString(resultMap);
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(outputText, Object.class);
			indented_json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			reportcount = EasyclaimAPI.getInstance().getNextReportRow(sessionId);
		}

		reportData=indented_json;
		rval = EasyclaimAPI.getInstance().resetSession(sessionId);
		log.debug("resetSession : result : " + rval);

		return indented_json;
	}



	public int setBusinessObjectByMap(Map<String, String> map, String path,int sessionId){
		for (Map.Entry<String, String> entry : map.entrySet()) {
			rval = EasyclaimAPI.getInstance().setBusinessObjectElement(sessionId, path ,entry.getKey(),entry.getValue());
			//outputText = checkErrorMessage(sessionId ,rval);
			log.debug("setBusinessObjectElement" +entry.getKey()+" result : "	+ rval);
		}
		return rval;
	}


	public String sendContent(int sessionId , String PatientService){

		Vector<String> resultAuthoriseContent = new Vector<String>(); // initialise the vector before passing it
		rval = EasyclaimAPI.getInstance().authoriseContent(sessionId, "medicare", resultAuthoriseContent);
		if(rval != 0)
			return outputText = checkErrorMessage(sessionId ,rval);
		log.debug("authoriseContent : result : " + rval);
		log.debug("authoriseContent : Value : "+ resultAuthoriseContent);

		Vector<String> resultGetContent = new Vector<String>(); // initialise the vector before passing it
		String getContentValue = null;
		rval = EasyclaimAPI.getInstance().getContent(sessionId,resultGetContent);
		log.debug("getContent result : " + rval);
		getContentValue = (String) resultGetContent.get(0);
		log.debug("getContent value result : "+ getContentValue);


		rval = EasyclaimAPI.getInstance().createTransmission(sessionId, PatientService);
		log.debug("createTransmission result " + rval);

		rval = EasyclaimAPI.getInstance().setTransmissionElement(sessionId, "LocationId", "HPC00000");
		log.debug("setTransmissionElement LocationId result : "+ rval);

		rval = EasyclaimAPI.getInstance().setTransmissionElement(sessionId, "DateOfTransmission", dateFormat.format(date));
		log.debug("setTransmissionElement DateOfTransmission result : "+ rval);

		rval = EasyclaimAPI.getInstance().addContent(sessionId,getContentValue);
		log.debug("addContent result : " + rval);

		rval = EasyclaimAPI.getInstance().sendTransmission(sessionId);
		log.debug("sendTransmission result : " + rval);

		if(rval != 0 && rval != 9501){
			EasyclaimAPI.getInstance().getErrorText(sessionId,Integer.toString(rval),ErrortextVector);
			log.debug(ErrortextVector.get(0));	
			return outputText =  "{"+Integer.toString(rval)+" : "+ErrortextVector.get(0)+"}";
		}
		else {
			outputText=Integer.toString(rval);
		}
		return outputText;
	}

	public String checkErrorMessage(int sessionId,int rval){
		if(rval != 0){
			EasyclaimAPI.getInstance().getErrorText(sessionId,Integer.toString(rval),ErrortextVector);
			log.debug(ErrortextVector.get(0));	
			return outputText =  "{"+Integer.toString(rval)+" : "+ErrortextVector.get(0)+"}";

		}
		return outputText;
	}

	// method to setting the session elements to reaching the server
	public int createSessionEasyclaimService(){

		String versionId = null;
		String majorVersionId = null;
		int sessionId;
		int rval;

		// versionId of HicOnlineAPI
		versionId = EasyclaimAPI.getVersionId();
		log.debug("getVersionId result : " + versionId);
		majorVersionId = EasyclaimAPI.getMajorVersionId();
		log.debug("getMajorVersionId result : " + majorVersionId);
		sessionId = EasyclaimAPI.getInstance().createSessionEasyclaim(MedicareUtil.getPath()+"classes/newSample.jks", "medicare");
		log.debug("createEasyclaimSession result : " + sessionId);
		Map<String, String> map = new HashMap<>();
		map.put("Recipient", "ebus.test@medicareaustralia.gov.au");
		map.put("Server", "http://test.mcoe.humanservices.gov.au/ext");
		map.put("LocationId", "HPC00000");
		map.put("PmsProduct", "Hippocamp");
		map.put("PmsVersion", "1.0");
		map.put("TransmissionType", "P");
		map.put("LogicPackDir", MedicareUtil.getPath()+"classes/lib");
		
		for (Map.Entry<String, String> entry : map.entrySet()) {
			rval = EasyclaimAPI.getInstance().setSessionElement(sessionId,entry.getKey(),entry.getValue());
			log.debug("setSessionElement (" +entry.getKey()+","+entry.getValue() +")result : "	+ rval);
		}

		return sessionId;
	}


}
