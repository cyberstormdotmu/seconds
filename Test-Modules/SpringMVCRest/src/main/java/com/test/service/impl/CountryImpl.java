package com.test.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.dao.ICountryDao;
import com.test.model.Country;
import com.test.service.ICountry;

/**
 * 
 * @author pct53
 *
 */
@Service("country") 
@Transactional
public class CountryImpl implements ICountry
{
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ICountryDao countryDao;

	/*
	 * (non-Javadoc)
	 * @see com.test.service.ICountry#insertCountry(com.test.model.Country)
	 */
	@Override
	@Transactional
	public void insertCountry(Country country) 
	{
		try
		{
			countryDao.insertCountry(country);
		}
		catch (Exception e) 
		{
			log.error("Error while inserting country: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.service.ICountry#updateCountry(com.test.model.Country)
	 */
	@Override
	@Transactional
	public void updateCountry(Country country) 
	{
		try
		{
			countryDao.updateCountry(country);
		}
		catch (Exception e) 
		{
			log.error("Error while updating country: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.service.ICountry#deleteCountry(int)
	 */
	@Override
	@Transactional
	public void deleteCountry(int countryId) 
	{
		try
		{
			countryDao.deleteCountry(countryId);
		}
		catch (Exception e) 
		{
			log.error("Error while deleting country: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.service.ICountry#selectCountry(int)
	 */
	@Override
	@Transactional
	public Country selectCountry(int countryId) 
	{
		try
		{
			return countryDao.selectCountry(countryId);
		}
		catch (Exception e) 
		{
			log.error("Error while selecting country: " + e.getMessage(), e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.test.service.ICountry#selectCountryList()
	 */
	@Override
	@Transactional
	public List<Country> selectCountryList() 
	{
		try
		{
			return countryDao.selectCountryList();
		}
		catch (Exception e) 
		{
			log.error("Error while selecting country list: " + e.getMessage(), e);
			return null;
		}
	}

}
