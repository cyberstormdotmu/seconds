package com.kenure.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class SiteDistrictUtilMeter {

	@Id
	@Column(name="site_district_util_meter_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int siteDistrictUtilMeterId;
	
	
	
}
