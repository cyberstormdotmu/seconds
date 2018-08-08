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

package com.cubeia.backoffice.wallet.api.dto.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.cubeia.backoffice.wallet.api.dto.TransactionsOrder;

/**
 * <p>Cubeia Wallet Protocol Unit implementation.</p>
 * 
 * <p>This bean is mapped to the XML specification by using JAXB
 * annotation framework.</p> 
 * 
 * NULL Values are considered wild cards. 
 * 
 * If you do not provide a limit then the response will not contain any Account
 * data but only the total count for the query.
 */
@XmlRootElement(name="ListTransactionsByAttributeRequest")
public class ListTransactionsByAttributeRequest {
	
    private Long accountId;
    private Long userId;
    
    private String key;
    private String value;
    
	private int offset;
	private int limit; 
	private TransactionsOrder order; 
	private boolean ascending;

	public ListTransactionsByAttributeRequest() {};
	
	public ListTransactionsByAttributeRequest(Long accountId, String key, int limit) {
        this(accountId, null, key, null, 0, limit, TransactionsOrder.ID, true);
    }

    public ListTransactionsByAttributeRequest(Long accountId, Long userId, String key, String value, int offset,
        int limit, TransactionsOrder order, boolean ascending) {
        this.accountId = accountId;
        this.userId = userId;
        this.key = key;
        this.value = value;
        this.offset = offset;
        this.limit = limit;
        this.order = order;
        this.ascending = ascending;
    }

    @XmlElement
	public Long getAccountId() {
        return accountId;
    }
	
	public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
	
    @XmlElement
	public Long getUserId() {
        return userId;
    }
	
	public void setUserId(Long userId) {
        this.userId = userId;
    }

	@XmlElement
	public String getKey() {
        return key;
    }
	
	public void setKey(String key) {
        this.key = key;
    }
	
    @XmlElement
	public String getValue() {
        return value;
    }
	
	public void setValue(String value) {
        this.value = value;
    }
	
	@XmlElement
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@XmlElement
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@XmlElement
	public TransactionsOrder getOrder() {
		return order;
	}

	public void setOrder(TransactionsOrder order) {
		this.order = order;
	}

	@XmlElement
	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
        result = prime * result + (ascending ? 1231 : 1237);
        result = prime * result + limit;
        result = prime * result + offset;
        result = prime * result + ((order == null) ? 0 : order.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ListTransactionsByAttributeRequest other = (ListTransactionsByAttributeRequest) obj;
        if (accountId == null) {
            if (other.accountId != null)
                return false;
        } else if (!accountId.equals(other.accountId))
            return false;
        if (ascending != other.ascending)
            return false;
        if (limit != other.limit)
            return false;
        if (offset != other.offset)
            return false;
        if (order != other.order)
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

	
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
