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
package com.cubeia.backoffice.operator.api;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement(name="OperatorWalletRequestQuery")
public class OperatorWalletRequestQuery {
	
    private Long operatorId;
    private Collection<OperatorWalletRequestStatus> statuses = new ArrayList<OperatorWalletRequestStatus>();
    private int queryOffset;
    private int queryLimit;
    private OperatorWalletRequestOrder order;
    private boolean ascending;
    
    
    public OperatorWalletRequestQuery() {}
    
    public OperatorWalletRequestQuery(int offset, int limit, OperatorWalletRequestOrder order, boolean ascending) {
        this.queryLimit = limit;
        this.queryOffset = offset;
        this.order = order;
        this.ascending = ascending;
    }

    @XmlElement
	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long userId) {
		this.operatorId = userId;
	}

	@XmlElement
	public Collection<OperatorWalletRequestStatus> getStatuses() {
        return statuses;
    }
	
	public void setStatuses(Collection<OperatorWalletRequestStatus> statuses) {
        this.statuses = statuses;
    }
	
	@XmlElement
	public int getQueryOffset() {
		return queryOffset;
	}

	public void setQueryOffset(int offset) {
		this.queryOffset = offset;
	}

	@XmlElement
	public int getQueryLimit() {
		return queryLimit;
	}

	public void setQueryLimit(int limit) {
		this.queryLimit = limit;
	}

	@XmlElement
	public OperatorWalletRequestOrder getOrder() {
		return order;
	}

	public void setOrder(OperatorWalletRequestOrder order) {
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
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
