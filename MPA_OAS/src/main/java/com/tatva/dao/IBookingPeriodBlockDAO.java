package com.tatva.dao;

import java.util.List;

import com.tatva.domain.BookingPeriodBlock;

/**
 * 
 * @author pci94
 *
 */
public interface IBookingPeriodBlockDAO {

	/**
	 * 
	 * @param bookingPeriodBlock
	 * save the booking period that Admin wants to block
	 */
	public void save(BookingPeriodBlock bookingPeriodBlock);

	/**
	 * 
	 * @return list of booking period that Admin has blocked.
	 */
	public List<BookingPeriodBlock> list();
	
	
	public List<BookingPeriodBlock> listOrderdBooked(int offset, int rows, String property, String orderValue);
	
	public List<BookingPeriodBlock> listBooked(int offset, int rows);
	
	public List<BookingPeriodBlock> listApp();
	
	
}
