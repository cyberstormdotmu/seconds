package com.tatva.controller;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tatva.domain.AppointmentMaster;
import com.tatva.domain.TransactionLogForm;
import com.tatva.model.AppointmentForm;
import com.tatva.service.IAppointmentMasterService;
import com.tatva.service.IAppoitmentCraftService;
import com.tatva.service.ITransactionLogService;
import com.tatva.service.ITransactionMasterService;
import com.tatva.utils.DateUtil;
import com.tatva.utils.MPAConstants;
import com.tatva.utils.MPAContext;

/**
 * this class is for appointment module
 * @author pci94
 *
 */
/**
 * @author pci98
 *
 */
@Controller
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private IAppointmentMasterService appointmentMasterService;
	
	@Autowired
	private ITransactionLogService transactionLogService;

	@Autowired
	private ITransactionMasterService transactionMasterService;

	@Autowired
	private IAppoitmentCraftService appointmentCraftService;
	
	public static String refNo;				//same will be passed in model & entity so that it is static
	
	List<AppointmentMaster> listAppointment = null;
	
	AppointmentMaster appointmentMaster=new AppointmentMaster();
	
	public String appointmentTypeRel=null;
	public String appointmentStatusRel=null;
	public String appointmentDateStringRel=null;

	/**
	 * method for navigating on registrationForm
	 * @return viewName
	 * @throws Exception 
	 */
	@RequestMapping(value="/appointmentForm.mpa",method=RequestMethod.GET)
	public String appointmentForm(Model model,@ModelAttribute("appointmentForm")AppointmentForm appointmentForm)throws Exception {
		
		model.addAttribute("appointmentForm",new AppointmentForm());
		return "/page.appointment";
	}

	/**
	 * checkpoint for server side validation
	 * @return viewName
	 */
	@RequestMapping(value="/appointmentForm.mpa",method=RequestMethod.POST)
	public ModelAndView registerAppointmentForm(Model model,@ModelAttribute("appointmentForm")AppointmentForm appointmentForm , BindingResult result,HttpServletRequest request)throws Exception {
		Boolean validationStatus = appointmentMasterService.registerValidate(appointmentForm);

		if(validationStatus == true){


			model.addAttribute("appointmentForm", appointmentForm);
			return new ModelAndView("/page.confirmation", "appointmentForm", appointmentForm);
		}
		else{
			return new ModelAndView("/page.appointment", "appointmentForm", appointmentForm);
		}
	}

	/**
	 * Method for geneating the Confirmation Form 
	 * @param appointmentForm
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/confirmationForm.mpa" , method = RequestMethod.POST)
	public ModelAndView confirmationForm(@ModelAttribute("appointmentForm")AppointmentForm appointmentForm , BindingResult result,HttpServletRequest request)throws Exception  
	{
		String referenceGenerator = refNo;
		appointmentMasterService.registerAppointment(appointmentForm , referenceGenerator);
		transactionMasterService.addTransactions(appointmentForm , referenceGenerator);
		appointmentCraftService.addCrafts(appointmentForm.getCraftNumbers() , referenceGenerator );
		return new ModelAndView("/page.appointmentReceipt", "appointmentForm", appointmentForm);
	}

	/**
	 * Checking the registered appointment status
	 * @param referenceNo
	 * @param noShow
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeAppointmentSatuts.mpa")
	public ModelAndView changeAppointmentSatuts(@RequestParam String referenceNo,@RequestParam String noShow) throws Exception {
		referenceNo=referenceNo.replace(",", "");
		AppointmentMaster appointmentMaster=appointmentMasterService.getById(referenceNo);
		TransactionLogForm transactionLogForm=new TransactionLogForm(); 
		
		
			if(noShow.equals("true")){
				transactionLogForm.setLogDate(new Date(new java.util.Date().getTime()));
				transactionLogForm.setLogTime(new Time(new java.util.Date().getTime()));
				transactionLogForm.setReferenceNo(appointmentMaster.getReferenceNo());
				transactionLogForm.setStaffId(MPAContext.currentUser);
				transactionLogForm.setLogType(MPAConstants.APPOINTMENT_NO_SHOW);

				appointmentMaster.setProcessTime(new Time(new java.util.Date().getTime()));
				appointmentMaster.setProcessDate(new Date(new java.util.Date().getTime()));
				appointmentMaster.setAppointmentStatus(MPAConstants.APPOINTMENT_NO_SHOW);
			}else if(MPAConstants.APPOINTMENT_CHECKED_IN.equals(appointmentMaster.getAppointmentStatus())){
				transactionLogForm.setLogDate(new Date(new java.util.Date().getTime()));
				transactionLogForm.setLogTime(new Time(new java.util.Date().getTime()));
				transactionLogForm.setReferenceNo(appointmentMaster.getReferenceNo());
				transactionLogForm.setStaffId(MPAContext.currentUser);
				transactionLogForm.setLogType(MPAConstants.APPOINTMENT_IN_PROGRESS);
				
				appointmentMaster.setProcessTime(new Time(new java.util.Date().getTime()));
				appointmentMaster.setProcessDate(new Date(new java.util.Date().getTime()));
				appointmentMaster.setAppointmentStatus(MPAConstants.APPOINTMENT_IN_PROGRESS);
			}else if(MPAConstants.APPOINTMENT_IN_PROGRESS.equals(appointmentMaster.getAppointmentStatus())){
				transactionLogForm.setLogDate(new Date(new java.util.Date().getTime()));
				transactionLogForm.setLogTime(new Time(new java.util.Date().getTime()));
				transactionLogForm.setReferenceNo(appointmentMaster.getReferenceNo());
				transactionLogForm.setStaffId(MPAContext.currentUser);
				transactionLogForm.setLogType(MPAConstants.APPOINTMENT_COMPLETE);
				
				appointmentMaster.setCompleteTime(new Time(new java.util.Date().getTime()));
				appointmentMaster.setCompleteDate(new Date(new java.util.Date().getTime()));
				appointmentMaster.setAppointmentStatus(MPAConstants.APPOINTMENT_COMPLETE);
			}
		appointmentMasterService.registerAppointment(appointmentMaster);
		transactionLogService.saveTransactionLog(transactionLogForm);
		
		ModelAndView mav = new ModelAndView("redirect:listAppointment.mpa?temp1=temp");
		return mav;
	}


	/**
	 * View Page navigation
	 * @return checkAppointment View Page
	 * @throws Exception
	 */
	@RequestMapping(value="/checkAppointment.mpa", method = RequestMethod.GET) 
	public String checkAppointment() throws Exception  {
		return "/page.checkAppointment";
	}


	/**
	 * fetching the appointment from user passport for generating the appointment list
	 * @param model
	 * @param passport
	 * @return list appointment view Page
	 * @throws Exception
	 */
	@RequestMapping(value="/checkAppointment.mpa", method = RequestMethod.POST)
	public String listAppointmentForUser(Model model,@RequestParam(value="passport")String passport)throws Exception {
		List<AppointmentForm> listAppointments = appointmentMasterService.listAppointments(passport);
		model.addAttribute("listAppointments",listAppointments);
		return "/page.listAppointmentForUser";
	}

	/**
	 * Cancel the appointment via Reference No
	 * @param request
	 * @return checkAppointment View Page
	 * @throws Exception
	 */
	@RequestMapping(value="/cancelAppointment.mpa")
	public String cancelAppointment(HttpServletRequest request)throws Exception {
		String refNo=request.getParameter("refNo");
		appointmentMasterService.cancelAppointment(refNo);
		return "redirect:/appointment/checkAppointment.mpa";
	}

	/**
	 * Rescheduling the Appointment  Selection 
	 * @param model
	 * @param request
	 * @return Reschedule Appointment View Page
	 * @throws Exception
	 */
	@RequestMapping (value="/rescheduleAppointment.mpa")
	public String rescheduleAppointmentRequest(Model model,HttpServletRequest request)throws Exception {
		String refNo=request.getParameter("refNo");
		AppointmentForm appointmentForm=appointmentMasterService.rescheduleAppointment(refNo);
		model.addAttribute("appointmentForm",appointmentForm);
		return "/page.rescheduleAppointment";
	}

	/**
	 * Appointment Rescheduling using POST request
	 * @param appointmentForm
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping (value="/rescheduleAppointment.mpa",method=RequestMethod.POST)
	public String rescheduleAppointment(@ModelAttribute("appointmentForm")AppointmentForm appointmentForm , BindingResult result) throws Exception {
		appointmentMasterService.rescheduleAppointment(appointmentForm);
		return "redirect:/appointment/checkAppointment.mpa";
	}

	
	@RequestMapping(value="/availableTimes.mpa",method= RequestMethod.POST,headers="Accept=*/*",produces="application/json")
	public @ResponseBody Set<String> availableTimes(HttpServletRequest request)throws Exception {

		String date=request.getParameter("date");
		String appointmentType=request.getParameter("appointmentType");
		Set<String> availableTimes=appointmentMasterService.availableTime(date, appointmentType);

		return availableTimes;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Request for the List Appointment 
	 * @param request
	 * @return appointmentList View Page
	 * @throws Exception
	 */
	@RequestMapping(value="/listAppointment.mpa")
	public ModelAndView listAppointments(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		String temp=request.getParameter("temp");
		String temp1=request.getParameter("temp1");
		
		if(temp!=null) {
			session.setAttribute("page", 1);
			session.setAttribute("userRows", 10);
			session.setAttribute("searchAptSize", null);
			listAppointment = null;
		}
		if(temp1!=null){
			listAppointment = null;
		}
		int noOfRecords=0;
		if(session.getAttribute("searchAptSize")!=null){
			session.setAttribute("page", 1);
			noOfRecords=listAppointment.size();
		} else{
			noOfRecords = appointmentMasterService.getTotalUsers();
		}
		
		
		int page;
		
		if(StringUtils.isNotEmpty(request.getParameter("page")))
		{
			page = Integer.parseInt(request.getParameter("page"));  //  Next requested page
		}
		else if(session.getAttribute("page") != null)
		{
			page = (int)session.getAttribute("page");  // if pagination requested then fetching from session
		}
		else
		{
			page = 1;  // default page no 1
		}
		
		int recordsPerPage = 10;
		String pageStr = request.getParameter("page");
		String recordsPerPageStr = request.getParameter("recordsPerPage");
		
		session.setAttribute("page", page);
		if(StringUtils.isNotEmpty(recordsPerPageStr))
		{
			recordsPerPage = Integer.parseInt(recordsPerPageStr);
			session.setAttribute("userRows", recordsPerPage);
		}
		
		if(session.getAttribute("userRows") != null)
		{
			recordsPerPage = (int)session.getAttribute("userRows");
		}
		if(StringUtils.isNotEmpty(pageStr))
		{
			page = Integer.parseInt(pageStr);
		}
		
		List<AppointmentMaster> appointmentList=new ArrayList<>();
		if((listAppointment == null || listAppointment.isEmpty()) && session.getAttribute("searchAptSize")==null)
		{
			appointmentList = appointmentMasterService.listAppointments((page - 1) * recordsPerPage, recordsPerPage);  // first time loading
		}
		else
		{
			if(listAppointment.size()>0){
			appointmentList = appointmentMasterService.getListPage(listAppointment, (page - 1) * recordsPerPage, recordsPerPage);
			}
		}
		for (AppointmentMaster appointmentMaster : appointmentList) {
			appointmentMaster.setAppointmentDateString(DateUtil.convertDateFromDatetoString(appointmentMaster.getAppointmentDate()));
		}
		
		int noOfPages = (int)Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		int start = (page - 1) * recordsPerPage;
		request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("start", start);
        ModelAndView mav=new ModelAndView("/page.listAppointments");
        mav.addObject("listAppointment", appointmentList);
        mav.addObject("searchAppointment",new AppointmentMaster());
        return mav;
		
	}
	
	/**
	 * 
	 * @param request for Ascending & Descending Order
	 * @return Requesting the List method according to Ordering
	 * @throws Exception
	 */
	@RequestMapping(value="/orderAppointmentList.mpa")
	public ModelAndView orderAppointmentList(HttpServletRequest request){

		Map<String, String[]> requestParamMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = requestParamMap.entrySet();
		
		Iterator<Entry<String, String[]>> it = entrySet.iterator();
		
		String property = null;
		String orderValue = null;
		
		if(it.hasNext())
		{
			Map.Entry<String, String[]> entry = it.next();
			
			property = entry.getKey();
			orderValue = entry.getValue()[0];
		}
		
		HttpSession session = request.getSession();
		int page = (int)session.getAttribute("page");
		int recordsPerPage = 10;
		if(session.getAttribute("userRows") != null)
		{
			recordsPerPage = (int)session.getAttribute("userRows");
		}		
		if(StringUtils.isEmpty(orderValue) || StringUtils.equals(orderValue, "null") || StringUtils.equals(orderValue, "DSC"))
		{
			orderValue = "ASC";
		}
		else if(StringUtils.equals(orderValue, "ASC"))
		{
			orderValue = "DSC";
		}
		List<AppointmentMaster> allAppointmentList=null;
		if(session.getAttribute("searchAptSize")!=null){
			allAppointmentList=appointmentMasterService.listSearchOrder(0,listAppointment.size(),property,orderValue,appointmentMaster,appointmentTypeRel,appointmentDateStringRel,appointmentStatusRel);
		}else{
			allAppointmentList = appointmentMasterService.listOrderdAppointment(0, appointmentMasterService.getTotalUsers(), property, orderValue);
		}
		this.listAppointment = allAppointmentList;
		request.setAttribute(property+"Order", orderValue);

		return listAppointments(request);
	}
	
	
	/**
	 * Implementing the Search functionality for Appointments
	 * @param appointment
	 * @param appointmentTypeRel
	 * @param appointmentDateStringRel
	 * @param appointmentStatusRel
	 * @param session
	 * @return Grid View Page for listAppointments
	 */
	@RequestMapping(value="/searchAppointment.mpa",method=RequestMethod.POST)
	public ModelAndView searchAppointment(@ModelAttribute ("searchAppointment") AppointmentMaster appointment,
											@RequestParam("appointmentTypeRel") String appointmentTypeRel,
											@RequestParam("appointmentDateStringRel") String appointmentDateStringRel,
											@RequestParam("appointmentStatusRel") String appointmentStatusRel,
											HttpSession session){		
		
		this.appointmentMaster=appointment;
		listAppointment=null;
		listAppointment=appointmentMasterService.searchAppointment(appointment,appointmentTypeRel,appointmentDateStringRel,appointmentStatusRel);
		this.appointmentTypeRel=appointmentTypeRel;
		this.appointmentDateStringRel=appointmentDateStringRel;
		this.appointmentStatusRel=appointmentStatusRel;
		session.setAttribute("searchAptSize", listAppointment.size());
		ModelAndView mav = new ModelAndView("redirect:listAppointment.mpa");
		return mav;
		
	}
}
