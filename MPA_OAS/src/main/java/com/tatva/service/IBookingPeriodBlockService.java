package com.tatva.service;

import java.util.List;

import com.tatva.domain.BookingPeriodBlock;

public interface IBookingPeriodBlockService {
	public void insertBookingPeriod(BookingPeriodBlock bookingPeriodBlock);

	public List<BookingPeriodBlock> list();
	
	public List<BookingPeriodBlock> listOrderdBookedPeriod(int offset, int rows, String property, String orderValue);

	
	public List<BookingPeriodBlock> listBookingPeriods(int offset, int rows);
	
	public List<BookingPeriodBlock> getListPage(List<BookingPeriodBlock> list, int offset, int rows);
	
	public int getTotalBooked();
}
