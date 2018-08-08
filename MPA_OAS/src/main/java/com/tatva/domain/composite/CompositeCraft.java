package com.tatva.domain.composite;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompositeCraft implements Serializable{


	/* Column variable declaration and generated getters-setters*
	 */

	@Column(name = "REF_NO")
	private String referenceNo;

	@Column(name = "CRAFT_NO")
	private String craftNo;


	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getCraftNo() {
		return craftNo;
	}
	public void setCraftNo(String craftNo) {
		this.craftNo = craftNo;
	}


}