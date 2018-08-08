package com.tatva.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatva.dao.IBookingPeriodBlockDAO;
import com.tatva.domain.BookingPeriodBlock;

/**
 * interface {@link IBookingPeriodBlockDAO}
 * @author pci94
 *
 */
@Repository
public class BookingPeriodBlockDAOImpl implements IBookingPeriodBlockDAO{

	@Autowired
    private SessionFactory sessionFactory;

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IBookingPeriodBlockDAO#save(com.tatva.domain.BookingPeriodBlock)
	 */
	public void save(BookingPeriodBlock bookingPeriodBlock) {
		/*
		 * save the booking period that Admin has blocked
		 */
		sessionFactory.getCurrentSession().saveOrUpdate(bookingPeriodBlock);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IBookingPeriodBlockDAO#list()
	 */
	@SuppressWarnings("unchecked")
	public List<BookingPeriodBlock> list() {
		/*
		 * list of blocked booking periods
		 */
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(BookingPeriodBlock.class);
		List<BookingPeriodBlock> rows = criteria.list();
		return rows;

	}

	@Override
	public List<BookingPeriodBlock> listOrderdBooked(int offset, int rows,
			String property, String orderValue) {
	
		try
		{
			Session session = sessionFactory.getCurrentSession();
			
			Criteria crit = session.createCriteria(BookingPeriodBlock.class);
			crit.setFirstResult(offset);
			crit.setMaxResults(rows);
			
			if(StringUtils.isNotEmpty(property))
			{
				if(StringUtils.isEmpty(orderValue) || StringUtils.equals(orderValue, "ASC"))
				{
					crit.addOrder(Order.asc(property));
				}
				else if(StringUtils.equals(orderValue, "DSC"))
				{
					crit.addOrder(Order.desc(property));
				}
			}
			return crit.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BookingPeriodBlock> listBooked(int offset, int rows) {
		

		try
		{
			Session session = sessionFactory.getCurrentSession();
			
			Criteria crit = session.createCriteria(BookingPeriodBlock.class);
			crit.setFirstResult(offset);
			crit.setMaxResults(rows);
			return crit.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BookingPeriodBlock> listApp() {
		return sessionFactory.getCurrentSession().createQuery("from BookingPeriodBlock").list();
	}
}
