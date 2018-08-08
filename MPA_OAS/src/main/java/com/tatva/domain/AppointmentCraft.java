package com.tatva.domain;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tatva.domain.composite.CompositeCraft;


@Entity
@Table(name = "APPOINTMENT_CRAFT")
public class AppointmentCraft  {


	// ----------------------------------------------------------------------------------------------------------------	
	/*
	 Composite Key declaration & getter-setters*
	 * */
	@EmbeddedId
	private CompositeCraft craftId;

	/*generated getters & setters*/
	public CompositeCraft getCraftId() {
		return craftId;
	}

	public void setCraftId(CompositeCraft craftId) {
		this.craftId = craftId;
	}
	// ----------------------------------------------------------------------------------------------------------------	

	/*
	 Different Entity Mapping Declaration and getters-setters
	 * */

	@MapsId("REF_NO")
	@JoinColumn(name = "REF_NO" , nullable = false) // Declaration for foreign key
	@OneToOne
	private AppointmentMaster appMaster;


	public AppointmentMaster getAppMaster() {
		return appMaster;
	}

	public void setAppMaster(AppointmentMaster appMaster) {
		this.appMaster = appMaster;
	}

}
//-------------------------------------------------------------------------------------------------------------------

/*
	 New class to embaded column variables in one composite key*
 * */


//------------------------------------------------------------------------------------------------------------------