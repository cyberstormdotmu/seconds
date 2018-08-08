package com.kenure.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="system_config")
public class VPNConfiguration {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="system_config_id")
	private int configId;
	
	@Column(name="VPN_server_name")
	private String vpnServerName;
	
	@Column(name="VPN_domain_name")
	private String vpnDomainName;
	
	@Column(name="VPN_username")
	private String vpnUserName;
	
	@Column(name="VPN_password")
	private String vpnPassword;
	
	@Column(name="no_of_bytes_per_endpoint_read")
	private String noOfBytesPerEndpointRead;
	
	@Column(name="no_of_bytes_per_packet")
	private String noOfBytesPerPacket;
	
	@Column(name="simpro_username")
	private String simproUsername;
	
	@Column(name="simpro_password")
	private String simproPassword;
	
	@Column(name="simpro_acc_no")
	private String simproAccNo;
	
	@Column(name="simpro_url")
	private String simproUrl;
	
	@Column(name="abnormal_threshold")
	private Double abnormalThreshold;
	
	public String getNoOfBytesPerEndpointRead() {
		return noOfBytesPerEndpointRead;
	}

	public void setNoOfBytesPerEndpointRead(String noOfBytesPerEndpointRead) {
		this.noOfBytesPerEndpointRead = noOfBytesPerEndpointRead;
	}

	public String getNoOfBytesPerPacket() {
		return noOfBytesPerPacket;
	}

	public void setNoOfBytesPerPacket(String noOfBytesPerPacket) {
		this.noOfBytesPerPacket = noOfBytesPerPacket;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public String getVpnServerName() {
		return vpnServerName;
	}

	public void setVpnServerName(String vpnServerName) {
		this.vpnServerName = vpnServerName;
	}

	public String getVpnDomainName() {
		return vpnDomainName;
	}

	public void setVpnDomainName(String vpnDomainName) {
		this.vpnDomainName = vpnDomainName;
	}

	public String getVpnUserName() {
		return vpnUserName;
	}

	public void setVpnUserName(String vpnUserName) {
		this.vpnUserName = vpnUserName;
	}

	public String getVpnPassword() {
		return vpnPassword;
	}

	public void setVpnPassword(String vpnPassword) {
		this.vpnPassword = vpnPassword;
	}

	public String getSimproUsername() {
		return simproUsername;
	}

	public void setSimproUsername(String simproUsername) {
		this.simproUsername = simproUsername;
	}

	public String getSimproPassword() {
		return simproPassword;
	}

	public void setSimproPassword(String simproPassword) {
		this.simproPassword = simproPassword;
	}

	public String getSimproAccNo() {
		return simproAccNo;
	}

	public void setSimproAccNo(String simproAccNo) {
		this.simproAccNo = simproAccNo;
	}

	public String getSimproUrl() {
		return simproUrl;
	}

	public void setSimproUrl(String simproUrl) {
		this.simproUrl = simproUrl;
	}

	public Double getAbnormalThreshold() {
		return abnormalThreshold;
	}

	public void setAbnormalThreshold(Double abnormalThreshold) {
		this.abnormalThreshold = abnormalThreshold;
	}
	
	
	
}
