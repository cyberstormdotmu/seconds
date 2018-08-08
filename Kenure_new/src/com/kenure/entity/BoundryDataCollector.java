package com.kenure.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="boundry_datacollector")
public class BoundryDataCollector {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="boundry_datacollector_id")
	private int boundryDatacollectorId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="site_id")
	private Site site;
	
	@OneToOne
	@JoinColumn(name="datacollector_id")
	private DataCollector datacollector;

	public int getBoundryDatacollectorId() {
		return boundryDatacollectorId;
	}

	public void setBoundryDatacollectorId(int boundryDatacollectorId) {
		this.boundryDatacollectorId = boundryDatacollectorId;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public DataCollector getDatacollector() {
		return datacollector;
	}

	public void setDatacollector(DataCollector datacollector) {
		this.datacollector = datacollector;
	}

}
