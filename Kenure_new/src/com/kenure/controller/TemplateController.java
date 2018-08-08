package com.kenure.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kenure.entity.Site;
import com.kenure.entity.User;
import com.kenure.service.AppConstInitializeService;
import com.kenure.service.IUserService;
import com.kenure.utils.KenureUtilityContext;

/**
 * 
 * @author TatvaSoft
 *
 */
@RestController
public class TemplateController {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private AppConstInitializeService appService;
	
	@RequestMapping(value = "loginForm", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) {
		
		//setting up project path	
		appService.initializeAppConstObjService(request);
		
		if(request.getSession().getAttribute("mailSentNotify") != null){

			request.getSession().removeAttribute("mailSentNotify");
			ModelAndView mv = new ModelAndView();
			mv.setViewName("loginForm");
			mv.addObject("status", true);
			return mv;
		}

		if(request.getSession().getAttribute("registerCompleted") != null){

			request.getSession().removeAttribute("registerCompleted");
			ModelAndView mv = new ModelAndView();
			mv.setViewName("loginForm");
			mv.addObject("registercompleted", true);
			return mv;
		}

		return new ModelAndView("loginForm");
	}

	@RequestMapping(value = "forgetPasswordForm",method = RequestMethod.GET)
	public ModelAndView forgetPassword(HttpServletRequest request) {
		return new ModelAndView("forgetPasswordForm");
	}

	@RequestMapping(value = "resetPasswordForm",method = RequestMethod.GET)
	public ModelAndView resetPassword(HttpServletRequest request) {
		return new ModelAndView("resetPasswordForm");
	}

	@RequestMapping(value = "adminProfile",method = RequestMethod.GET)
	public ModelAndView adminProfile(HttpServletRequest request) {
		return new ModelAndView("adminProfile");
	}

	@RequestMapping(value = "switchUser",method = RequestMethod.GET)
	public ModelAndView switchUser(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("switchUser");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		String roleName = loggedUser.getRole().getRoleName();
		if(roleName.equals("admin")){
			model.addObject("currentRoleAdmin", true);
		}else if(roleName.equals("customer")){
			model.addObject("currentRoleCustomer", true);
		}
		return model;
	}

	@RequestMapping(value = "consumerProfile",method = RequestMethod.GET)
	public ModelAndView consumerProfile(HttpServletRequest request) {
		return new ModelAndView("consumerprofile");
	}

	@RequestMapping(value = "/M2MConfiguration",method = RequestMethod.GET)
	public ModelAndView M2MConfiguration(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("M2MConfiguration");
	}

	/*@RequestMapping(value = "/dataPlanManagement",method = RequestMethod.GET)
	public ModelAndView dataPlanManagement(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("dataPlanManagement");
	}*/

	@RequestMapping(value = "/addDataPlanForm",method = RequestMethod.GET)
	public ModelAndView addDataPlanForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditDataPlan");
	}

	/*@RequestMapping(value = "/editDataPlanForm",method = RequestMethod.GET)
	public ModelAndView editDataPlanForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditDataPlan");
	}
*/

	/*@RequestMapping(value = "config",method = RequestMethod.GET)
	public ModelAndView config(HttpServletRequest request) {
		return new ModelAndView("portalConfig");
	}*/

	@RequestMapping(value = "consumerEPManagement",method = RequestMethod.GET)
	public ModelAndView consumeEPManagement(HttpServletRequest request) {
		return new ModelAndView("customer-consumerManagement");
	}
	

	@RequestMapping(value = "consumerDashboard",method = RequestMethod.GET)
	public ModelAndView consumerDashboard(HttpServletRequest request) {
		return new ModelAndView("consumerDashboard");
	}


	@RequestMapping(value = "header",method = RequestMethod.GET)
	public ModelAndView header(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("header");
		Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
		User loggedUser = null;
		if(isNormalCustomer != null && isNormalCustomer){
			loggedUser = (User) request.getSession().getAttribute(KenureUtilityContext.normalCustomer);
		}else{
			loggedUser = (User) request.getSession().getAttribute(KenureUtilityContext.currentUser);
		}
		String roleName = loggedUser.getRole().getRoleName();

		if(roleName.equals("admin")){
			model.addObject("isAdmin", true);
		}else if (roleName.equals("customer")) {
			model.addObject("isCustomer", true);	
		}else if(roleName.equals("consumer")) {
			model.addObject("isConsumer", true);
		}else if(roleName.equals("installer")){
			model.addObject("isInstaller", true);
		}else if(roleName.equalsIgnoreCase("normalcustomer")){
			model.addObject("isCustomer", true);
		}
		model.addObject("userData",loggedUser);

		return model;
	}

	@RequestMapping(value = "footer",method = RequestMethod.GET)
	public ModelAndView footer(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("footer");
		return model;
	}

	@RequestMapping(value = "adminleftNavigationbar",method = RequestMethod.GET)
	public ModelAndView leftNavigationbar(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("adminLeftNavigationbar");
	}


	@RequestMapping(value = "consumerLeftNavigationbar",method = RequestMethod.GET)
	public ModelAndView consumerleftNavigationbar(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("consumerLeftNavigationbar");
	}

/*	@RequestMapping(value = "spareDataCollectorPanel",method = RequestMethod.GET)
	public ModelAndView spareDataCollectorPanel(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("spareDataCollector");
	}*/

	@RequestMapping(value = "editSpareDataCollectorForm",method = RequestMethod.GET)
	public ModelAndView editSpareDataCollector(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("editSpareDataCollector");
	}

	@RequestMapping(value = "addDataCollectorForm",method = RequestMethod.GET)
	public ModelAndView addDataCollector(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addSpareDataCollector");
	}

	@RequestMapping(value="/popup",method=RequestMethod.GET)
	public ModelAndView popup(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("Popup");
	}

	/*@RequestMapping(value = "countryManagement",method = RequestMethod.GET)
	public ModelAndView countryManagement(HttpServletRequest request) {
		return new ModelAndView("countryManagement");
	}*/

/*	@RequestMapping(value = "currencyManagement",method = RequestMethod.GET)
	public ModelAndView currencyManagement(HttpServletRequest request) {
		return new ModelAndView("currencyManagement");
	}*/

	@RequestMapping(value = "/addCountryForm",method = RequestMethod.GET)
	public ModelAndView addcountryForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditCountry");
	}

	@RequestMapping(value = "/editCountryForm",method = RequestMethod.GET)
	public ModelAndView editcountryForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditCountry");
	}

	@RequestMapping(value = "/addCurrencyForm",method = RequestMethod.GET)
	public ModelAndView addCurrencyForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditCurrency");
	}

	@RequestMapping(value = "/editCurrencyForm",method = RequestMethod.GET)
	public ModelAndView editCurrencyForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditCurrency");
	}

	@RequestMapping(value = "installerLeftNavigationbar",method = RequestMethod.GET)
	public ModelAndView installerLeftNavigationbar(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("installerLeftNavigationbar");
	}

	@RequestMapping(value = "installerDashboard",method = RequestMethod.GET)
	public ModelAndView installerDashboard(HttpServletRequest request) {
		return new ModelAndView("installerDashboard");
	}

	@RequestMapping(value = "installerProfile",method = RequestMethod.GET)
	public ModelAndView installerProfile(HttpServletRequest request) {
		return new ModelAndView("installerProfile");
	}

	@RequestMapping(value = "consumerRegistration",method = RequestMethod.GET)
	public ModelAndView consumerRegistration(HttpServletRequest request) {
		return new ModelAndView("consumerRegistration");
	}

	@RequestMapping(value = "consumerDetailsRegistration",method = RequestMethod.GET)
	public ModelAndView consumerDetailsRegistration(HttpServletRequest request) {

		String customerCode = (String) request.getSession().getAttribute("customerCode");
		String consumerAccountNumber = (String) request.getSession().getAttribute("consumerAccountNumber");

		ModelAndView mv = new ModelAndView();
		mv.addObject("customerCode", customerCode);
		mv.addObject("consumerAccountNumber", consumerAccountNumber);
		mv.setViewName("consumerDetailsRegistration");

		return mv;
	}

	/*@RequestMapping(value = "batteryManagement",method = RequestMethod.GET)
	public ModelAndView batteryManagement(HttpServletRequest request) {
		return new ModelAndView("batteryManagement");
	}*/

	@RequestMapping(value = "/addBatteryForm",method = RequestMethod.GET)
	public ModelAndView addBatteryForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditBatteryLife");
	}

	@RequestMapping(value = "/editBatteryForm",method = RequestMethod.GET)
	public ModelAndView editBatteryForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditBatteryLife");
	}

	@RequestMapping(value="/superUserAuthenticationRequire",method=RequestMethod.GET)
	public ModelAndView superUserAuthenticator(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("superUserAuthenticatorPopUp");
	}
/*
	//********** Installation and Commissioning **************

	@RequestMapping(value="/installationAndCommissioning",method=RequestMethod.GET)
	public ModelAndView installationAndCommissioning(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("installationAndCommissioningDashBoard");
	}

	@RequestMapping(value="/commissioningphase2",method=RequestMethod.GET)
	public ModelAndView commissioningphase2(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("commissioningphase2");
	}

	@RequestMapping(value="/dcEpConfigurationAndTest",method=RequestMethod.GET)
	public ModelAndView dcEpConfigurationAndTest(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("dcEpConfigurationAndTest");
	}
	
	@RequestMapping(value="/selectedLinesPopup",method=RequestMethod.GET)
	public ModelAndView selectedLinesPopup(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("selectedLinesPopup");
	}
	
	@RequestMapping(value="/addNewLinePopup",method=RequestMethod.GET)
	public ModelAndView addNewLinePopup(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("addNewLinePopup");
	}
	
	@RequestMapping(value="/commissioning",method=RequestMethod.GET)
	public ModelAndView commissioning(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("commissioning");
	}
	
	@RequestMapping(value="/verification",method=RequestMethod.GET)
	public ModelAndView verification(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("verification");
	}
	
	@RequestMapping(value="/dataCollectorInstallation",method=RequestMethod.GET)
	public ModelAndView dataCollectorInstallation(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("dataCollectorInstallationPage");
	}
	
	//********** Maintenance **************
	
	@RequestMapping(value = "assetInspectionSchedule",method = RequestMethod.GET)
	public ModelAndView assetInspectionSchedule(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("assetInspectionSchedule");
	}
	
	@RequestMapping(value = "batteryReplacementSchedule",method = RequestMethod.GET)
	public ModelAndView batteryReplacementSchedule(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("batteryReplacementSchedule");
	}
	
	@RequestMapping(value = "consumerMeterAlerts",method = RequestMethod.GET)
	public ModelAndView consumerMeterAlerts(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("consumerMeterAlerts");
	}
	
	@RequestMapping(value = "networkAlerts",method = RequestMethod.GET)
	public ModelAndView networkAlerts(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("networkAlerts");
	}
	
	@RequestMapping(value = "dcMessageQueue",method = RequestMethod.GET)
	public ModelAndView dcMessageQueue(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("dcMessageQueue");
	}
	
	@RequestMapping(value = "/repeatersPlanning",method = RequestMethod.GET)
	public ModelAndView repeatersPlanning(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("repeatersPlanningPage");
	}*/
	

	//*************** Report & Analytics ********************************
	
	@RequestMapping(value = "dataUsageReport",method = RequestMethod.GET)
	public ModelAndView dataUsageReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("dataUsageReport");
	}
	
	@RequestMapping(value = "billingDataReport",method = RequestMethod.GET)
	public ModelAndView billingDataReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("billingDataReport");
	}
	@RequestMapping(value="/abnormalConsumptionReport",method = RequestMethod.GET)
	public ModelAndView abnormalConsumptionReportPageRedirect(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("abnormalConsumptionReport");
	}
	
	@RequestMapping(value = "/networkWaterLossReport",method = RequestMethod.GET)
	public ModelAndView networkWaterLossReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("networkWaterLossReport");
	}
	
	@RequestMapping(value = "/alertReport",method = RequestMethod.GET)
	public ModelAndView alertReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("alertReport");
	}
	
	@RequestMapping(value = "/aggregateConsumptionReport",method = RequestMethod.GET)
	public ModelAndView aggregateConsumptionReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("aggregateConsumptionReport");
	}
	
	@RequestMapping(value = "/dcConnectionMap",method = RequestMethod.GET)
	public ModelAndView dcConnectionMap(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("dcConnectionMapPage");
	}
	
	@RequestMapping(value = "/networkConsumptionReport",method = RequestMethod.GET)
	public ModelAndView networkConsumptionReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("networkConsumptionReportPage");
	}
	@RequestMapping(value="/abnormalConsumptionActionGraph",method=RequestMethod.GET)
	public ModelAndView abnormalConsumptionActionGraph(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("abnormalConsumptionActionGraph");
	}
	
	@RequestMapping(value="/freeReport",method=RequestMethod.GET)
	public ModelAndView freereport(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("freeReport");
	}
	
	@RequestMapping(value="/whatIfReport",method=RequestMethod.GET)
	public ModelAndView whatIfReport(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("finanicialWhatIfReport");
	}
	
	@RequestMapping(value = "/consumptionReport",method = RequestMethod.GET)
	public ModelAndView consumptionReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("consumptionReportPage");
	}
	
	@RequestMapping(value = "/usageReport",method = RequestMethod.GET)
	public ModelAndView usageReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("usageReportPage");
	}
	
	@RequestMapping(value = "/billingReport",method = RequestMethod.GET)
	public ModelAndView billingHistoryReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("billingReportPage");
	}
}
