package com.test.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.test.dao.ICityDao;
import com.test.model.City;

@Repository
public class CityDaoImpl implements ICityDao
{
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	SessionFactory sessionFactory;
	Session session;

	/*
	 * (non-Javadoc)
	 * @see com.test.dao.ICityDao#insertCity(com.test.model.City)
	 */
	@Override
	public void insertCity(City city) 
	{
		session = sessionFactory.getCurrentSession();
		session.save(city);
		log.info("City inserted : ID : " + city.getId() + ", Name : " + city.getCityName());
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.dao.ICityDao#updateCity(com.test.model.City)
	 */
	@Override
	public void updateCity(City city) 
	{
		session = sessionFactory.getCurrentSession();
		session.update(city);
		log.info("City updated : ID : " + city.getId() + ", Name : " + city.getCityName());
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.dao.ICityDao#deleteCity(int)
	 */
	@Override
	public void deleteCity(int cityId) 
	{
		City city = selectCity(cityId);
		session.delete(city);
		log.info("City deleted : ID : " + city.getId() + ", Name : " + city.getCityName());
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.dao.ICityDao#selectCity(int)
	 */
	@Override
	public City selectCity(int cityId) 
	{
		session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(City.class);
		crit.add(Restrictions.eq("id", cityId));
		crit.setMaxResults(1);
		log.info("City seleted : ID : " + cityId);
		log.debug("City seleted : ID : " + cityId);
		return (City)crit.list().get(0);
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.dao.ICityDao#selectCityList()
	 */
	@Override
	public List<City> selectCityList() 
	{
		session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(City.class);
		log.info("Fatching city list");
		return crit.list();
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.dao.ICityDao#selectCountryCityList(int)
	 */
	@Override
	public List<City> selectCountryCityList(int countryId) 
	{
		session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(City.class);
		crit.add(Restrictions.eq("countryId", countryId));
		crit.setMaxResults(1);
		log.info("Fatching city list of country Id: " + countryId);
		return crit.list();
	}

}
