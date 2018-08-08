package com.tatva.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.tatva.domain.AppointmentMaster;
import com.tatva.domain.AppointmentMasterHistory;
import com.tatva.model.AppointmentForm;
import com.tatva.model.RescheduledReport;
import com.tatva.model.ServiceTimeModel;
import com.tatva.model.WaitingTimeModel;
import com.tatva.reports.AppointmentCancelledRpt;
import com.tatva.reports.AppointmentLateRpt;
import com.tatva.reports.AppointmentNoShowRpt;
import com.tatva.reports.AppointmentRescheduledRpt;
import com.tatva.reports.AppointmentScheduleRpt;
import com.tatva.reports.ServiceTimeRpt;
import com.tatva.reports.WaitingTimeRpt;
import com.tatva.service.IAppointmentMasterHistoryService;
import com.tatva.service.IAppointmentMasterService;
import com.tatva.service.IReschduledReportService;
import com.tatva.utils.DateUtil;
import com.tatva.utils.ReportMailService;

@Controller
@RequestMapping("/report")
public class ReportController
{
	@Autowired
	IAppointmentMasterService appointmentMasterService;
	
	@Autowired
	IAppointmentMasterHistoryService appointmentMasterHistoryService;
	
	@Autowired
	IReschduledReportService rescheduledReportService;
	
		
	@RequestMapping(value="/Report.mpa",method=RequestMethod.GET)
	public String reportSelection()throws Exception {
		
		return "/page.reportSelection";
	}
	
	//=====================
	
	@RequestMapping(value="/appointmentReportReceipt.pdf" , method=RequestMethod.POST)
	public ModelAndView getAppointmentReport(@ModelAttribute("appointmentForm")AppointmentForm appointmentForm,HttpServletRequest request)throws Exception{
		
		return new ModelAndView("appointmentReceiptRpt","appointmentForm",appointmentForm);
		
	}
	
	
	
	//========================
	/**
	 * PDF report Generation
	 * @param request
	 * @param model
	 * @return List for regular Data
	 * @throws Exception
	 */
	@RequestMapping(value="/selectedReport.pdf",method=RequestMethod.POST)
	public ModelAndView getSelectedReport(HttpServletRequest request,Model model)throws Exception {

		
		String reportname = request.getParameter("report_selection");
		String date=request.getParameter("date");
		Date d1=DateUtil.convertDateFromStringtoDate(date);
		date=DateUtil.convertInDashedFormat(d1);
		
		if(reportname.equals("apt_schedule")){
			List<AppointmentMaster> appointMasterList  = new ArrayList<>();
			appointMasterList = appointmentMasterService.selectAllAppointments(d1);
			String message = "There are no records available" ;
			if(appointMasterList.size() > 0)
			return new ModelAndView("appointmentMastersRpt", "appointmentList", appointMasterList);
			else{

				model.addAttribute("message",message);
				return new ModelAndView("appointmentMastersRpt", "appointmentList", appointMasterList);
			}

		}
		
		else if(reportname.equals("apt_reschedule")){
			List<AppointmentMasterHistory> appointmentHistoryList = appointmentMasterHistoryService.selectAllAppointmentsHistory();
			List<AppointmentMaster> appointmentMasterList = new ArrayList<>(); 
			appointmentMasterList = appointmentMasterService.selectAllAppointments(d1);
			
			List<RescheduledReport> reschduledReportList = new ArrayList<>() ;
			reschduledReportList = rescheduledReportService.selectRescheduledAppointments(appointmentHistoryList,appointmentMasterList);
			String message = "There are no records available" ;
			if(reschduledReportList.size() >0)
				return new ModelAndView("AppointmentrescheRpt" , "rescheduledReportList" , reschduledReportList  );
			else
				model.addAttribute("message",message);
				return new ModelAndView("AppointmentrescheRpt" , "rescheduledReportList" , reschduledReportList  );
		}
		
		else if(reportname.equals("apt_cancelled")){
			List<AppointmentMaster> cancelledAppointmentMasterList  = new ArrayList<>();
			cancelledAppointmentMasterList =appointmentMasterService.selectcancelledAppointments(d1);
			String message = "There are no records available" ;
			if(cancelledAppointmentMasterList.size() >0)
			return new ModelAndView("appointmentMastercRpt" , "cancelledAppointmentMasterList" , cancelledAppointmentMasterList);
			else{
				model.addAttribute("message",message);
				return new ModelAndView("appointmentMastercRpt" , "cancelledAppointmentMasterList" , cancelledAppointmentMasterList);
			}
		}
		
		else if(reportname.equals("waiting_time_rpt")){
			List<WaitingTimeModel> listWaitingTime =  new ArrayList<>();
				listWaitingTime = 	appointmentMasterService.getWaitingTime(date);
				String message = "There are no records available";
				if(listWaitingTime.size() > 0)
					return new ModelAndView("waitingTimeRpt","listWaitingTime",listWaitingTime);
				else {
					model.addAttribute("message" , message);
					return new ModelAndView("waitingTimeRpt" , "listWaitingTime" ,listWaitingTime );
				}
					
		}
		
		else if(reportname.equals("service_time_rpt")){
			
			List<ServiceTimeModel> listServiceTime = new ArrayList<>();
			listServiceTime =	appointmentMasterService.getServiceTime(date);
			String message = "There are no records available";
			if(listServiceTime.size() > 0)
			return new ModelAndView("serviceTimeRpt","listServiceTime",listServiceTime);
			else
			{
				model.addAttribute("message" , message);
				return new ModelAndView("serviceTimeRpt" , "listServiceTime" , listServiceTime);
			}
		
		}
		
		else if(reportname.equals("apt_late")){
			
			List<AppointmentMaster> lateAppointmentMasterList = new ArrayList<>();
			lateAppointmentMasterList = appointmentMasterService.selectLateAppointments(d1);
			String message = "There are no records available" ;
			if(lateAppointmentMasterList.size() >0)
			return new ModelAndView("appointmentMasterlRpt" , "lateAppointmentMasterList" , lateAppointmentMasterList);
			else
				model.addAttribute("message",message);
			return new ModelAndView("appointmentMasterlRpt" , "lateAppointmentMasterList" , lateAppointmentMasterList);
		}
		
		else if(reportname.equals("apt_no_show")){
			
			List<AppointmentMaster> noShowAppointmentMasterList = new ArrayList<>();
			noShowAppointmentMasterList =appointmentMasterService.selectNoShowAppointments(d1);
			String message = "There are no records available" ;
			if(noShowAppointmentMasterList.size() > 0)
			return new ModelAndView("appointmentMasternsRpt" , "noShowAppointmentMasterList" , noShowAppointmentMasterList);
			else{
				model.addAttribute("message",message);
				return new ModelAndView("appointmentMasternsRpt" , "noShowAppointmentMasterList" , noShowAppointmentMasterList);
			}
			}
		else{
			return null;
		}
	}
	
	

	
	
	
	/**
	 * Mailing functionality Implementation 
	 * @param request
	 * @param response
	 * @return Report Selection Page
	 * @throws Exception
	 */
	@RequestMapping(value="/mailSelectedReport.pdf",method=RequestMethod.POST)
	public String mailWaitingTimeReport(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String reportname = request.getParameter("report_selection");
		String date=request.getParameter("date");
		Date d1=DateUtil.convertDateFromStringtoDate(date);
		date=DateUtil.convertInDashedFormat(d1);
		
		if(reportname.equals("apt_schedule"))
		{
			List<AppointmentMaster> appointMasterList  = new ArrayList<>();
			appointMasterList = appointmentMasterService.selectAllAppointments(d1);
			if(appointMasterList.size() >0){
			AppointmentScheduleRpt rpt=new AppointmentScheduleRpt();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("appointmentList", appointMasterList);
			Document document=new Document();
			PdfWriter writer=null;
			
			Map<String, DataSource> emailAttachmentList = new HashMap<String, DataSource>();
			
			   ByteArrayOutputStream baos = new ByteArrayOutputStream();
			   PdfWriter.getInstance(document, baos);
			   document.open();
			   document= rpt.buildPdfDocument(model, document, writer, request, response);
			   document.close();
			   InputStream decodedInput= null;
			   DataSource source = null;

			   try{
				   decodedInput=new ByteArrayInputStream(((ByteArrayOutputStream) baos).toByteArray());
				   ReportMailService mailService=new ReportMailService();
						try{ 
							source = new ByteArrayDataSource(decodedInput, "application/pdf");
							emailAttachmentList.put("Scheduled Appointment Report", source);
						}catch (IOException e){
							e.printStackTrace();
						}
					 mailService.execute(emailAttachmentList,date);
					 
			   }catch (Exception e) {
				   e.printStackTrace();
			   }finally{
				   decodedInput.close();
			   }
			}
			   return "redirect:/report/Report.mpa";

		}
		else if(reportname.equals("apt_reschedule"))  // Functionality for Rescheduled Appointment Report
		{
			List<AppointmentMasterHistory> appointmentHistoryList = appointmentMasterHistoryService.selectAllAppointmentsHistory();
			List<AppointmentMaster> appointmentMasterList = new ArrayList<>();
			appointmentMasterList = appointmentMasterService.selectAllAppointments(d1);
			
			List<RescheduledReport> reschduledReportList = new ArrayList<>() ;
			reschduledReportList = rescheduledReportService.selectRescheduledAppointments(appointmentHistoryList,appointmentMasterList);
			
			if(reschduledReportList.size() >0){
			AppointmentRescheduledRpt rpt=new AppointmentRescheduledRpt();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("rescheduledReportList", reschduledReportList);
			Document document=new Document();
			PdfWriter writer=null;
			
			Map<String, DataSource> emailAttachmentList = new HashMap<String, DataSource>();

			   ByteArrayOutputStream baos = new ByteArrayOutputStream();
			   PdfWriter.getInstance(document, baos);
			   document.open();
			   document= rpt.buildPdfDocument(model, document, writer, request, response);
			   document.close();
			   InputStream decodedInput= null;
			   DataSource source = null;

			   try{
				   decodedInput=new ByteArrayInputStream(((ByteArrayOutputStream) baos).toByteArray());
				   ReportMailService mailService=new ReportMailService();
						try{ 
							source = new ByteArrayDataSource(decodedInput, "application/pdf");
							emailAttachmentList.put("Reschedule Appointment Report", source);
						}catch (IOException e){
							e.printStackTrace();
						}
					 mailService.execute(emailAttachmentList,date);
					 
			   }catch (Exception e) {
				   e.printStackTrace();
			   }finally{
				   decodedInput.close();
			   }
			}
			 return "redirect:/report/Report.mpa";

			
		}
		else if(reportname.equals("apt_cancelled"))    // Functionality for Appointment Cancelled report
		{
			List<AppointmentMaster> cancelledAppointmentMasterList  = new ArrayList<>();
			cancelledAppointmentMasterList =appointmentMasterService.selectcancelledAppointments(d1);
			if(cancelledAppointmentMasterList.size() >0){
			
			AppointmentCancelledRpt rpt=new AppointmentCancelledRpt();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("cancelledAppointmentMasterList", cancelledAppointmentMasterList);
			Document document=new Document();
			PdfWriter writer=null;
			
			Map<String, DataSource> emailAttachmentList = new HashMap<String, DataSource>();
			
			   ByteArrayOutputStream baos = new ByteArrayOutputStream();
			   PdfWriter.getInstance(document, baos);
			   document.open();
			   document= rpt.buildPdfDocument(model, document, writer, request, response);
			   document.close();
			   InputStream decodedInput= null;
			   DataSource source = null;

			   try{
				   decodedInput=new ByteArrayInputStream(((ByteArrayOutputStream) baos).toByteArray());
				   ReportMailService mailService=new ReportMailService();
						try{
							source = new ByteArrayDataSource(decodedInput, "application/pdf");
							emailAttachmentList.put("Cancelled Appointment Report", source);
						}catch (IOException e){
							e.printStackTrace();
						}
					 mailService.execute(emailAttachmentList,date);
					 
			   }catch (Exception e) {
				   e.printStackTrace();
			   }finally{
				   decodedInput.close();
			   }
			}
			 return "redirect:/report/Report.mpa";
		}
		else if(reportname.equals("waiting_time_rpt"))
		{
			List<WaitingTimeModel> listWaitingTime = new ArrayList<>();
			listWaitingTime= appointmentMasterService.getWaitingTime(date);
			if(listWaitingTime.size() >0){
			WaitingTimeRpt rpt=new WaitingTimeRpt();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("listWaitingTime", listWaitingTime);
			Document document=new Document();
			PdfWriter writer=null;
			
			Map<String, DataSource> emailAttachmentList = new HashMap<String, DataSource>();
			
			   ByteArrayOutputStream baos = new ByteArrayOutputStream();
			   PdfWriter.getInstance(document, baos);
			   document.open();
			   document= rpt.buildPdfDocument(model, document, writer, request, response);
			   document.close();
			   InputStream decodedInput= null;
			   DataSource source = null;

			   try{
				   decodedInput=new ByteArrayInputStream(((ByteArrayOutputStream) baos).toByteArray());
				   ReportMailService mailService=new ReportMailService();
						try{ 
							source = new ByteArrayDataSource(decodedInput, "application/pdf");
							emailAttachmentList.put("Waiting Time Report", source);
						}catch (IOException e){
							e.printStackTrace();
						}
					 mailService.execute(emailAttachmentList,date);
					 
			   }catch (Exception e) {
				   e.printStackTrace();
			   }finally{
				   decodedInput.close();
			   }
			}
			 return "redirect:/report/Report.mpa";

			
		}
		else if(reportname.equals("service_time_rpt"))  /*Functionality for Service_time_report */
		{
			List<ServiceTimeModel> listServiceTime= appointmentMasterService.getServiceTime(date);
			ServiceTimeRpt rpt=new ServiceTimeRpt();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("listServiceTime", listServiceTime);
			Document document=new Document();
			PdfWriter writer=null;
			
			Map<String, DataSource> emailAttachmentList = new HashMap<String, DataSource>();
			
			   ByteArrayOutputStream baos = new ByteArrayOutputStream();
			   PdfWriter.getInstance(document, baos);
			   document.open();
			   document= rpt.buildPdfDocument(model, document, writer, request, response);
			   document.close();
			   InputStream decodedInput= null;
			   DataSource source = null;

			   try{
				   decodedInput=new ByteArrayInputStream(((ByteArrayOutputStream) baos).toByteArray());
				   ReportMailService mailService=new ReportMailService();
						try{ 
							source = new ByteArrayDataSource(decodedInput, "application/pdf");
							emailAttachmentList.put("Service Time Report", source);
						}catch (IOException e){
							e.printStackTrace();
						}
					 mailService.execute(emailAttachmentList,date);
					 
			   }catch (Exception e) {
				   e.printStackTrace();
			   }finally{
				   decodedInput.close();
			   }
			 return "redirect:/report/Report.mpa";

		}
		
		else if(reportname.equals("apt_late"))  /*Functionality for Late Application Report*/
		{			
			List<AppointmentMaster> lateAppointmentMasterList = new ArrayList<>();
			lateAppointmentMasterList = appointmentMasterService.selectLateAppointments(d1);			
			if(lateAppointmentMasterList.size() >0){
			
			AppointmentLateRpt rpt=new AppointmentLateRpt();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("lateAppointmentMasterList", lateAppointmentMasterList);
			Document document=new Document();
			PdfWriter writer=null;
			
			Map<String, DataSource> emailAttachmentList = new HashMap<String, DataSource>();
			
			   ByteArrayOutputStream baos = new ByteArrayOutputStream();
			   PdfWriter.getInstance(document, baos);
			   document.open();
			   document= rpt.buildPdfDocument(model, document, writer, request, response);
			   document.close();
			   InputStream decodedInput= null;
			   DataSource source = null;

			   try{
				   decodedInput=new ByteArrayInputStream(((ByteArrayOutputStream) baos).toByteArray());
				   ReportMailService mailService=new ReportMailService();
						try{ 
							source = new ByteArrayDataSource(decodedInput, "application/pdf");
							emailAttachmentList.put("Late Appointment Report", source);
						}catch (IOException e){
							e.printStackTrace();
						}
					 mailService.execute(emailAttachmentList,date);
					 
			   }catch (Exception e) {
				   e.printStackTrace();
			   }finally{
				   decodedInput.close();
			   }
			}
			 return "redirect:/report/Report.mpa";

			
		}
		else if(reportname.equals("apt_no_show"))  // Functionality for Appointment No-Show Report 
		{
			
			List<AppointmentMaster> noShowAppointmentMasterList = new ArrayList<>();
			noShowAppointmentMasterList =appointmentMasterService.selectNoShowAppointments(d1);			
			
			if(noShowAppointmentMasterList.size() >0){
			AppointmentNoShowRpt rpt=new AppointmentNoShowRpt();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("noShowAppointmentMasterList", noShowAppointmentMasterList);
			Document document=new Document();
			PdfWriter writer=null;
			
			Map<String, DataSource> emailAttachmentList = new HashMap<String, DataSource>();
			
			   ByteArrayOutputStream baos = new ByteArrayOutputStream();
			   PdfWriter.getInstance(document, baos);
			   document.open();
			   document= rpt.buildPdfDocument(model, document, writer, request, response);
			   document.close();
			   InputStream decodedInput= null;
			   DataSource source = null;

			   try{
				   decodedInput=new ByteArrayInputStream(((ByteArrayOutputStream) baos).toByteArray());
				   ReportMailService mailService=new ReportMailService();
						try{ 
							source = new ByteArrayDataSource(decodedInput, "application/pdf");
							emailAttachmentList.put("Appointment No-Show Report", source);
						}catch (IOException e){
							e.printStackTrace();
						}
					 mailService.execute(emailAttachmentList,date);
					 
			   }catch (Exception e) {
				   e.printStackTrace();
			   }finally{
				   decodedInput.close();
			   }
			}
			 return "redirect:/report/Report.mpa";

		}
		else {
			
			return null;
		}
			}
}