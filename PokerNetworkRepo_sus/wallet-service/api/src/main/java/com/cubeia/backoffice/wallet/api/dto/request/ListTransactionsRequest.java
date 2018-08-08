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

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
 * 
 * @author Fredrik Johansson; Cubeia Ltd
 */
@XmlRootElement(name="ListTransactionsRequest")
public class ListTransactionsRequest {
	
	private Long id1; 
	private Boolean id1credit;
	private boolean id1IsUserId; 
	private Long id2;
	private Boolean id2credit; 
	private boolean id2IsUserId; 
	private Date startDate; 
	private Date endDate;
	private int offset;
	private int limit; 
	private TransactionsOrder order; 
	private boolean ascending;

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@XmlElement
	public Long getId1() {
		return id1;
	}

	public void setId1(Long id1) {
		this.id1 = id1;
	}

	@XmlElement
	public Boolean getId1credit() {
		return id1credit;
	}

	public void setId1credit(Boolean id1credit) {
		this.id1credit = id1credit;
	}

	@XmlElement
	public boolean isId1IsUserId() {
		return id1IsUserId;
	}

	public void setId1IsUserId(boolean id1IsUserId) {
		this.id1IsUserId = id1IsUserId;
	}

	@XmlElement
	public Long getId2() {
		return id2;
	}

	public void setId2(Long id2) {
		this.id2 = id2;
	}

	@XmlElement
	public Boolean getId2credit() {
		return id2credit;
	}

	public void setId2credit(Boolean id2credit) {
		this.id2credit = id2credit;
	}

	@XmlElement
	public boolean isId2IsUserId() {
		return id2IsUserId;
	}

	public void setId2IsUserId(boolean id2IsUserId) {
		this.id2IsUserId = id2IsUserId;
	}

	@XmlElement
    @XmlJavaTypeAdapter(com.cubeia.backoffice.wallet.api.util.DateAdapter.class) 
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@XmlElement
    @XmlJavaTypeAdapter(com.cubeia.backoffice.wallet.api.util.DateAdapter.class) 
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
        result = prime * result + (ascending ? 1231 : 1237);
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((id1 == null) ? 0 : id1.hashCode());
        result = prime * result + (id1IsUserId ? 1231 : 1237);
        result = prime * result
            + ((id1credit == null) ? 0 : id1credit.hashCode());
        result = prime * result + ((id2 == null) ? 0 : id2.hashCode());
        result = prime * result + (id2IsUserId ? 1231 : 1237);
        result = prime * result
            + ((id2credit == null) ? 0 : id2credit.hashCode());
        result = prime * result + limit;
        result = prime * result + offset;
        result = prime * result + ((order == null) ? 0 : order.hashCode());
        result = prime * result
            + ((startDate == null) ? 0 : startDate.hashCode());
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
        ListTransactionsRequest other = (ListTransactionsRequest) obj;
        if (ascending != other.ascending)
            return false;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        if (id1 == null) {
            if (other.id1 != null)
                return false;
        } else if (!id1.equals(other.id1))
            return false;
        if (id1IsUserId != other.id1IsUserId)
            return false;
        if (id1credit == null) {
            if (other.id1credit != null)
                return false;
        } else if (!id1credit.equals(other.id1credit))
            return false;
        if (id2 == null) {
            if (other.id2 != null)
                return false;
        } else if (!id2.equals(other.id2))
            return false;
        if (id2IsUserId != other.id2IsUserId)
            return false;
        if (id2credit == null) {
            if (other.id2credit != null)
                return false;
        } else if (!id2credit.equals(other.id2credit))
            return false;
        if (limit != other.limit)
            return false;
        if (offset != other.offset)
            return false;
        if (order != other.order)
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        return true;
    }

	

}
