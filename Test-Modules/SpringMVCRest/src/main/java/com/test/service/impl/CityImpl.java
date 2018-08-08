package com.test.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.dao.ICityDao;
import com.test.model.City;
import com.test.service.ICity;

/**
 * 
 * @author pct53
 *
 */
@Service("city")
@Transactional
public class CityImpl implements ICity
{
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ICityDao cityDao;

	/*
	 * (non-Javadoc)
	 * @see com.test.service.ICity#insertCity(com.test.model.City)
	 */
	@Override
	@Transactional
	public void insertCity(City city) 
	{
		try
		{
			cityDao.insertCity(city);
		}
		catch (Exception e) 
		{
			log.error("Error while inserting city: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.service.ICity#updateCity(com.test.model.City)
	 */
	@Override
	@Transactional
	public void updateCity(City city) 
	{
		try
		{
			cityDao.updateCity(city);
		}
		catch (Exception e) 
		{
			log.error("Error while updating city: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.service.ICity#deleteCity(int)
	 */
	@Override
	@Transactional
	public void deleteCity(int cityId) 
	{
		try
		{
			cityDao.deleteCity(cityId);
		}
		catch (Exception e) 
		{
			log.error("Error while deleting city: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.service.ICity#selectCity(int)
	 */
	@Override
	@Transactional
	public City selectCity(int cityId) 
	{
		try
		{
			return cityDao.selectCity(cityId);
		}
		catch (Exception e) 
		{
			log.error("Error while selecting city: " + e.getMessage(), e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.service.ICity#selectCityList()
	 */
	@Override
	@Transactional
	public List<City> selectCityList() 
	{
		try
		{
			return cityDao.selectCityList();
		}
		catch (Exception e) 
		{
			log.error("Error while selecting city: " + e.getMessage(), e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.service.ICity#selectCountryCityList(int)
	 */
	@Override
	@Transactional
	public List<City> selectCountryCityList(int countryId) 
	{
		try
		{
			return cityDao.selectCountryCityList(countryId);
		}
		catch (Exception e) 
		{
			log.error("Error while selecting city of country: " + e.getMessage(), e);
			return null;
		}
	}

}
