package com.test.service;

import java.util.List;

import com.test.model.Country;

/**
 * Service object for Country
 * 
 * @author pct53
 *
 */
public interface ICountry
{
	/**
	 * Inserts the country
	 * 
	 * @param country new country
	 */
	public void insertCountry(Country country);
	
	/**
	 * Updates the country
	 * 
	 * @param country updated country
	 */
	public void updateCountry(Country country);
	
	/**
	 * Deletes country
	 * 
	 * @param countryId country id to remove
	 */
	public void deleteCountry(int countryId);
	
	/**
	 * Selects country details
	 * 
	 * @param countryId country id to select
	 * 
	 * @return country details
	 */
	public Country selectCountry(int countryId);
	
	/**
	 * Selects country list
	 * 
	 * @return country list
	 */
	public List<Country> selectCountryList();
}
