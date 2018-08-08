package com.tatva.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tatva.domain.BookingPeriodBlock;
import com.tatva.service.IBookingPeriodBlockService;
import com.tatva.utils.DateUtil;

@Controller
@RequestMapping("/bookingPeriod")
public class BookingPeriodController {
	
	@Autowired
	private IBookingPeriodBlockService bookingPeriodBlockService;
	
	
	List<BookingPeriodBlock> listBookingBlock = null;  // list for grid view

	/**
	 * 
	 * @return Set time list AddBookingPeriodBlock view page for binding Model
	 * @throws Exception
	 */
	@RequestMapping (value="/addBookingPeriodForm.mpa")
	public ModelAndView addBookingPeriodForm()throws Exception  {
		BookingPeriodBlock  bookingPeriodBlock =new BookingPeriodBlock();
		bookingPeriodBlock.setTimeList(getTimeList());
		ModelAndView modelAndView=new ModelAndView("/page.addBookingPeriodBlock","bookingPeriodBlock",bookingPeriodBlock);
		return modelAndView;
	}
	
	
	/**
	 * Method for database entry for adding booking period block
	 * @param bookingPeriodBlock
	 * @param redirectAttributes
	 * @return list booking period block with success message
	 * @throws Exception
	 */
	@RequestMapping (value="/addBookingPeriodBlock.mpa")
	public ModelAndView addBookingPeriod(@ModelAttribute BookingPeriodBlock bookingPeriodBlock, RedirectAttributes redirectAttributes) throws Exception {
		bookingPeriodBlockService.insertBookingPeriod(bookingPeriodBlock);
		ModelAndView modelAndView=new ModelAndView("redirect:listBookingPeriodBlock.mpa");
		redirectAttributes.addFlashAttribute("messages", "Booking Period has been added successfully");
		return modelAndView;
	}
	
	
	/**
	 * listing the booking period block list with proper pagination & ascending-descending order
	 * @param request
	 * @return controller method for listing BookedPreriodAppointments
	 * @throws Exception
	 */
	@RequestMapping(value = "/listBookingPeriodBlock.mpa")
	 public ModelAndView listBookingPeriodBlock(HttpServletRequest request)throws Exception {

		HttpSession session = request.getSession();
		
		String temp=request.getParameter("temp");
		if(temp!=null) {
			session.setAttribute("page", 1);
			session.setAttribute("appRows", 10);
			listBookingBlock = null;
		}
		
		int page;
		
		if(StringUtils.isNotEmpty(request.getParameter("page")))
		{
			page = Integer.parseInt(request.getParameter("page"));
			
		}
		else if(session.getAttribute("page") != null)
		{
			page = (int)session.getAttribute("page");
		}
		else
		{
			page = 1;
		}
		
		
		
		int recordsPerPage = 10;
		
		String recordsPerPageStr = request.getParameter("recordsPerPage");
		
		
		if(StringUtils.isNotEmpty(recordsPerPageStr))
		{
			recordsPerPage = Integer.parseInt(recordsPerPageStr);
			session.setAttribute("appRows", recordsPerPage);
		}
		
		if(session.getAttribute("appRows") != null)
		{
			recordsPerPage = (int)session.getAttribute("appRows");
		}
		
		int noOfRecords = bookingPeriodBlockService.getTotalBooked();
		int noOfPages = (int)Math.ceil(noOfRecords * 1.0 / recordsPerPage);
		
		String pageStr = null;
		if(page > noOfPages){    // If anyone change the url then it will go to the first page number
			page = 1;
			pageStr = "1";
		}
		else{
			
			pageStr = request.getParameter("page");
		}
		
		session.setAttribute("page", page);
		 
		if(StringUtils.isNotEmpty(pageStr))
		{
			page = Integer.parseInt(pageStr);
		}

		List<BookingPeriodBlock> listOfBookingPeriodBlock = new ArrayList<>();

		if(listBookingBlock == null || listBookingBlock.isEmpty())
		{
			listOfBookingPeriodBlock = bookingPeriodBlockService.listBookingPeriods((page - 1) * recordsPerPage, recordsPerPage);
		}
		else
		{
			listOfBookingPeriodBlock = bookingPeriodBlockService.getListPage(listBookingBlock, (page - 1) * recordsPerPage, recordsPerPage);
		
		}
		for (BookingPeriodBlock bookingPeriodBlock : listOfBookingPeriodBlock) {
			bookingPeriodBlock.setPeriodStartDateString(DateUtil.convertDateFromDatetoString(bookingPeriodBlock.getPeriodStartDate()));
		}
		
		
		int start = (page - 1) * recordsPerPage;
		session.setAttribute("listBookingPeriodBlock", listOfBookingPeriodBlock);
		request.setAttribute("noOfPages", noOfPages);
		
        request.setAttribute("currentPage", page);
        request.setAttribute("start", start);

		
		
		return new ModelAndView("/page.listBookingPeriodBlock","listOfBookingPeriodBlock",listOfBookingPeriodBlock);
	}
	
	
	/**
	 * Request for changing the booking slot
	 * @return List of BookingPeriodBlock View page
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeBookingSlot.mpa")
	 public ModelAndView changeBookingSlot()throws Exception {
		List<BookingPeriodBlock> listOfBookingPeriodBlock= bookingPeriodBlockService.list();

		return new ModelAndView("/page.listBookingPeriodBlock","listOfBookingPeriodBlock",listOfBookingPeriodBlock);
	}
	
	
	/**
	 * Fetching the list of time for addBookingSlot
	 * @return Timelist object to the addBooking Slot method
	 */
	public List<String> getTimeList(){
		List<String> timeList=new ArrayList<String>();
		for(int i=1;i<=23;i++){
			for(int j=0;j<=45;j=j+15){
				String result = String.format("%02d:%02d", i,j);
				timeList.add(result);
			}
		}
		timeList.add("24:00");
		return timeList;
	}
	
	
	/**
	 * Ascending & Descending Ordering of BookingAppointmentList
	 * @param request
	 * @return listBoookingPeriod View Page
	 * @throws Exception
	 */
	@RequestMapping(value = "/orderBookingList.mpa")
	public ModelAndView orderBookingList(HttpServletRequest request)throws Exception
	{
		
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
		if(session.getAttribute("appRows") != null)
		{
			recordsPerPage = (int)session.getAttribute("appRows");
		}
		
		if(StringUtils.isEmpty(orderValue) || StringUtils.equals(orderValue, "null") || StringUtils.equals(orderValue, "DSC"))
		{
			orderValue = "ASC";
		}		
		else if(StringUtils.equals(orderValue, "ASC"))
		{
			orderValue = "DSC";
		}
		
		
		List<BookingPeriodBlock> listBookingBlock = bookingPeriodBlockService.listOrderdBookedPeriod((page - 1) * recordsPerPage, recordsPerPage, property, orderValue);
		List<BookingPeriodBlock> allListOfBookingPeriodBlock = bookingPeriodBlockService.listOrderdBookedPeriod(0, bookingPeriodBlockService.getTotalBooked(), property, orderValue);
		this.listBookingBlock = allListOfBookingPeriodBlock;
		request.setAttribute(property+"Order", orderValue);
		return listBookingPeriodBlock(request);

	}
	
}
