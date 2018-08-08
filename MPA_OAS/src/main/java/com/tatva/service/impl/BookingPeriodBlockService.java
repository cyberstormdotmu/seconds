package com.tatva.service.impl;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatva.dao.IAppointmentMasterDAO;
import com.tatva.dao.IBookingPeriodBlockDAO;
import com.tatva.domain.AppointmentMaster;
import com.tatva.domain.BookingPeriodBlock;
import com.tatva.service.IBookingPeriodBlockService;
import com.tatva.utils.DateUtil;

@Service
public class BookingPeriodBlockService implements IBookingPeriodBlockService{

	@Autowired
	private IBookingPeriodBlockDAO bookingPeriodBlockDAO;
	
	@Autowired
	private IAppointmentMasterDAO appointmentMasterDAO;

	
	public void insertBookingPeriod(BookingPeriodBlock bookingPeriodBlock) {
		try{
		String endTime=null;
		String startTime=null;
		String startDate=null;
		if(bookingPeriodBlock!=null){
			if(bookingPeriodBlock.getPeriodStartDateString()!=null)
				startDate=bookingPeriodBlock.getPeriodStartDateString();
			if(bookingPeriodBlock.getPeriodStartTimeString()!=null)
				startTime=bookingPeriodBlock.getPeriodStartTimeString();
			if(bookingPeriodBlock.getPeriodEndTimeString()!=null)
				endTime=bookingPeriodBlock.getPeriodEndTimeString();
		}
		if(startDate!=null)
			bookingPeriodBlock.setPeriodStartDate(DateUtil.convertDateFromStringtoDate(startDate));
		if(startTime!=null){
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			Date timeD = (Date)formatter.parse(startTime);
			Time t=new Time(timeD.getTime());
			bookingPeriodBlock.setPeriodStartTime(t);
		}
		if(endTime!=null){
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			Date timeD = (Date)formatter.parse(endTime);
			Time t=new Time(timeD.getTime());
			bookingPeriodBlock.setPeriodEndTime(t);
		}
		List<AppointmentMaster> listAppointment= appointmentMasterDAO.listAppointment(bookingPeriodBlock.getPeriodStartDate());
		for (AppointmentMaster appointmentMaster : listAppointment) {
			Date appointmentDate=appointmentMaster.getAppointmentDate();
			Time appointmentTime= appointmentMaster.getAppointmentTime();
			int flag=appointmentDate.compareTo(bookingPeriodBlock.getPeriodStartDate());
			if(flag==0){
				if("single".equals(appointmentMaster.getAppointmentType())){
					if(!((appointmentTime.getTime() > bookingPeriodBlock.getPeriodStartTime().getTime() && 
							(appointmentTime.getTime()+ (15 * 60 * 1000)) > bookingPeriodBlock.getPeriodStartTime().getTime())
							&&
					   (appointmentTime.getTime() < bookingPeriodBlock.getPeriodStartTime().getTime() && (appointmentTime.getTime()+(15 * 60 * 1000)) < bookingPeriodBlock.getPeriodStartTime().getTime()))){
						System.out.println("Conflict");
						appointmentMasterDAO.cancelAppointment(appointmentMaster.getReferenceNo());
					}
				}else if("group".equals(appointmentMaster.getAppointmentType())){
					if(!((appointmentTime.getTime() > bookingPeriodBlock.getPeriodStartTime().getTime() && 
							(appointmentTime.getTime()+ (30 * 60 * 1000)) > bookingPeriodBlock.getPeriodStartTime().getTime())
							&&
					   (appointmentTime.getTime() < bookingPeriodBlock.getPeriodStartTime().getTime() && (appointmentTime.getTime()+(30 * 60 * 1000)) < bookingPeriodBlock.getPeriodStartTime().getTime()))){
						System.out.println("Conflict");
						appointmentMasterDAO.cancelAppointment(appointmentMaster.getReferenceNo());
					}
				}
			}
		}
		bookingPeriodBlockDAO.save(bookingPeriodBlock);
		}catch (Exception e) {
		}
		
	}


	@Override
	public List<BookingPeriodBlock> list() {
		List<BookingPeriodBlock> list=bookingPeriodBlockDAO.list();
		
		for (BookingPeriodBlock bookingPeriodBlock : list) {
			bookingPeriodBlock.setPeriodStartDateString(DateUtil.convertDateFromDatetoString(bookingPeriodBlock.getPeriodStartDate()));
		}
		return list;
	}

	@Override
	public List<BookingPeriodBlock> listOrderdBookedPeriod(int offset, int rows,
			String property, String orderValue) {
		
		return bookingPeriodBlockDAO.listOrderdBooked(offset, rows, property, orderValue);
	}


	@Override
	public int getTotalBooked() {
		return bookingPeriodBlockDAO.listApp().size();
	}

	@Override
	public List<BookingPeriodBlock> listBookingPeriods(int offset, int rows) {
		return bookingPeriodBlockDAO.listBooked(offset, rows);
	}

	@Override
	public List<BookingPeriodBlock> getListPage(List<BookingPeriodBlock> list, int offset, int rows) {
		
		List<BookingPeriodBlock> listBookingBlock = new ArrayList<BookingPeriodBlock>();
		
		for(int i = offset; i < offset + rows; i++)
		{
			listBookingBlock.add(list.get(i));
			if(i == list.size() - 1)
			{
				break;
			}
		}
		
		return listBookingBlock;
	}
}