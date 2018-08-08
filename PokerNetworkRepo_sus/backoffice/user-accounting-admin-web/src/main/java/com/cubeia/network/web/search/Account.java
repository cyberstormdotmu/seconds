/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cubeia.network.web.search;

import java.util.Date;
import java.util.Map;

import com.cubeia.network.shared.web.wicket.search.SearchEntity;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements SearchEntity {
	private static final long serialVersionUID = -6036782208928311686L;
	
	@JsonProperty("id")
    private Long accountId;
    private Long userId;
    private Long walletId;
    private String name;
    private String status;
    private String type;
    private Date created;
    private Date closed;
    private String currencyCode;
    private int fractionalDigits;

    @JsonDeserialize(keyAs = String.class, contentAs = Attribute.class)
    private Map<String, Attribute> attributes;
    
	public Long getAccountId() {
		return accountId;
	}
	
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getWalletId() {
		return walletId;
	}
	
	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Date getClosed() {
		return closed;
	}
	
	public void setClosed(Date closed) {
		this.closed = closed;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public int getFractionalDigits() {
		return fractionalDigits;
	}

	public void setFractionalDigits(int fractionalDigits) {
		this.fractionalDigits = fractionalDigits;
	}
	
	public Map<String, Attribute> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
	}
	
	
	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", userId=" + userId
				+ ", walletId=" + walletId + ", name=" + name + ", status="
				+ status + ", type=" + type + ", created=" + created
				+ ", closed=" + closed + ", currencyCode=" + currencyCode
				+ ", fractionalDigits=" + fractionalDigits + ", attributes="
				+ attributes + "]";
	}
    
}