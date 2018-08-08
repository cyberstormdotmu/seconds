package com.test.service;

import java.util.List;

import com.test.model.City;

/**
 * Service object for City
 * 
 * @author pct53
 *
 */
public interface ICity
{
	
	/**
	 * Inserts the city
	 * 
	 * @param city new city
	 */
	public void insertCity(City city);
	
	/**
	 * Updates the city
	 * 
	 * @param city updated city
	 */
	public void updateCity(City city);
	
	/**
	 * Deletes city
	 * 
	 * @param cityId city id to remove
	 */
	public void deleteCity(int cityId);
	
	/**
	 * Selects city details
	 * 
	 * @param cityId city id to select
	 * 
	 * @return city details
	 */
	public City selectCity(int cityId);
	
	/**
	 * Selects city list
	 * 
	 * @return city list
	 */
	public List<City> selectCityList();
	
	/**
	 * Select city list of country
	 * 
	 * @param countryId the country id to select city list
	 * 
	 * @return city list
	 */
	public List<City> selectCountryCityList(int countryId);
}
