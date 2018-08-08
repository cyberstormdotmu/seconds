package com.test.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.test.dao.ICountryDao;
import com.test.model.Country;

@Repository("countryDao")
public class CountryDaoImpl implements ICountryDao
{
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SessionFactory sessionFactory;
	Session session;
	
	/*
	 * (non-Javadoc)
	 * @see com.test.dao.ICountryDao#insertCountry(com.test.model.Country)
	 */
	@Override 
	public void insertCountry(Country country) 
	{
		session = sessionFactory.getCurrentSession();
		session.save(country);
		log.info("Country inserted : ID : " + country.getId() + ", Name : " + country.getCountryName());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.test.dao.ICountryDao#updateCountry(com.test.model.Country)
	 */
	@Override
	public void updateCountry(Country country) 
	{
		session = sessionFactory.getCurrentSession();
		session.update(country);
		log.info("Country updated : ID : " + country.getId() + ", Name : " + country.getCountryName());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.test.dao.ICountryDao#deleteCountry(int)
	 */
	@Override
	public void deleteCountry(int countryId) 
	{
		Country country = selectCountry(countryId);
		session.delete(country);
		log.info("Country deleted : ID : " + country.getId() + ", Name : " + country.getCountryName());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.test.dao.ICountryDao#selectCountry(int)
	 */
	@Override
	public Country selectCountry(int countryId) 
	{
		session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Country.class);
		crit.add(Restrictions.eq("id", countryId));
		crit.setMaxResults(1);
		log.info("Country seleted : ID : " + countryId);
		return (Country)crit.list().get(0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.test.dao.ICountryDao#selectCountryList()
	 */
	@Override
	public List<Country> selectCountryList() 
	{
		session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Country.class);
		log.info("Fatching country list");
		log.debug("Fatching country list");
		List<Country> list = crit.list();
		return list;
	}

	
	
}
