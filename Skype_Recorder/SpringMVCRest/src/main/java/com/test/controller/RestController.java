package com.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.model.City;
import com.test.model.Country;
import com.test.service.ICity;
import com.test.service.ICountry;

/**
 * Rest based controller for all rest requests
 * @author pct53
 *
 */
@Controller
public class RestController 
{
	@Autowired
	private ICountry country;
	@Autowired
	private ICity city;
	
	/**
	 * Gets country list
	 * 
	 * @return country list
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/country", headers="Accept=application/json")
	public @ResponseBody List<Country> getCountryList()
	{
		System.out.println(country.selectCountryList());
		return country.selectCountryList();
	}
	
	/**
	 * Gets country details
	 * 
	 * @param countryId the country Id
	 * @return country details
	 */
	@RequestMapping(method=RequestMethod.GET, value="/country/{countryId}", headers="Accept=application/json")
	public @ResponseBody Country getCountry(@PathVariable int countryId) 
	{
		return country.selectCountry(countryId);
	}

	/**
	 * Instert country
	 * 
	 * @param countryName Country name to insert
	 */
    @RequestMapping(method=RequestMethod.POST, value="/country/{countryName}")
	public @ResponseBody void addCountry(@PathVariable String countryName) 
    {
		Country c = new Country();
		c.setCountryName(countryName);
		country.insertCountry(c);
	}
	
    /**
     * Update country details
     * 
     * @param countryId the country Id
     * @param countryName new country name 
     */
	@RequestMapping(method=RequestMethod.PUT, value="/country/{countryId}/{countryName}")
	public @ResponseBody void updateCountry(@PathVariable int countryId, 
											@PathVariable String countryName) 
	{
		Country c = new Country();
		c.setId(countryId);
		c.setCountryName(countryName);
		country.updateCountry(c);
	}
	
	/**
	 * Removes country
	 * 
	 * @param countryId country Id to remove
	 */
	@RequestMapping(method=RequestMethod.DELETE, value="/country/{countryId}")
	public @ResponseBody void removeCountry(@PathVariable int countryId) 
	{
		country.deleteCountry(countryId);
	}
	
	/**
	 * Gets city list
	 * 
	 * @return the city list
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/city", headers = "Accept=application/json")
	public @ResponseBody List<City> getCityList()
	{
		return city.selectCityList();
	}
	
	/**
	 * Gets city list of country
	 * 
	 * @param countryId the country Id
	 * 
	 * @return country city list
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/city/country/{countryId}", headers = "Accept=application/json")
	public @ResponseBody List<City> getCountryCityList(@PathVariable int countryId)
	{
		return city.selectCountryCityList(countryId);
	}
	
	/**
	 * Gets city details
	 * 
	 * @param cityId the city id
	 * 
	 * @return city details
	 */
	@RequestMapping(method=RequestMethod.GET, value="/city/{cityId}", headers="Accept=application/json")
	public @ResponseBody City getCity(@PathVariable int cityId) 
	{
		return city.selectCity(cityId);
	}

	/**
	 * Inserts the city
	 * 
	 * @param countryId the country Id
	 * @param cityName the City name
	 */
    @RequestMapping(method=RequestMethod.POST, value="/city/{countryId}/{cityName}")
	public @ResponseBody void addCity(@PathVariable int countryId,
									  @PathVariable String cityName) 
    {
		City c = new City();
		c.setCountryId(countryId);
		c.setCityName(cityName);
		city.insertCity(c);
	}
	
    /**
     * Update City details
     * 
     * @param cityId the city Id
     * @param countryId the new/old country Id
     * @param cityName the new/old city name
     */
	@RequestMapping(method=RequestMethod.PUT, value="/city/{cityId}/{countryId}/{cityName}")
	public @ResponseBody void updateCity(@PathVariable int cityId, 
										 @PathVariable int countryId,
										 @PathVariable String cityName) 
	{
		City c = new City();
		c.setId(cityId);
		c.setCountryId(countryId);
		c.setCityName(cityName);
		
		city.updateCity(c);
	}
	
	/**
	 * Removes City
	 * 
	 * @param cityId the city id
	 */
	@RequestMapping(method=RequestMethod.DELETE, value="/city/{cityId}")
	public @ResponseBody void removeCity(@PathVariable int cityId) 
	{
		city.deleteCity(cityId);
	}
	
	/**
	 * Gets country
	 * 
	 * @return country
	 */
	public ICountry getCountry() {
		return country;
	}

	/**
	 * Sets country
	 * 
	 * @param country country
	 */
	public void setCountry(ICountry country) {
		this.country = country;
	}

	/**
	 * Gets city
	 * 
	 * @return city	 
	 */
	public ICity getCity() {
		return city;
	}

	/**
	 * Sets city
	 * 
	 * @param city city
	 */
	public void setCity(ICity city) {
		this.city = city;
	}
	
	

}
