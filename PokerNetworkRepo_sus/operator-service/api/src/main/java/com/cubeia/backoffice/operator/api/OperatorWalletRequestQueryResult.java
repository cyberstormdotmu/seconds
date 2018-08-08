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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement(name="OperatorWalletRequestQueryResult")
public class OperatorWalletRequestQueryResult {
    private List<OperatorWalletRequestLogDTO> entries = new ArrayList<OperatorWalletRequestLogDTO>();
    private int totalQueryResultSize;
    private int queryOffset;
    private int queryLimit;
    private OperatorWalletRequestOrder order;
    private boolean ascending;
    
    public OperatorWalletRequestQueryResult() {
    }
    
    public OperatorWalletRequestQueryResult(int queryOffset, int queryLimit,
        int totalQueryResultSize, List<OperatorWalletRequestLogDTO> entries, OperatorWalletRequestOrder order, boolean ascending) {
        
        this.queryLimit = queryLimit;
        this.queryOffset = queryOffset;
        this.totalQueryResultSize = totalQueryResultSize;
        this.entries = entries;
        this.ascending = ascending;
    }
    
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
    @XmlElement(name="entries")
    public List<OperatorWalletRequestLogDTO> getEntries() {
        return entries;
    }

    public void setEntries(List<OperatorWalletRequestLogDTO> entries) {
        this.entries = entries;
    }
    
    @XmlElement
    public int getTotalQueryResultSize() {
        return totalQueryResultSize;
    }
    
    public void setTotalQueryResultSize(int totalQueryResultSize) {
        this.totalQueryResultSize = totalQueryResultSize;
    }
    
    @XmlElement
    public int getQueryOffset() {
        return queryOffset;
    }
    
    public void setQueryOffset(int queryOffset) {
        this.queryOffset = queryOffset;
    }
    
    @XmlElement
    public int getQueryLimit() {
        return queryLimit;
    }

    public void setQueryLimit(int queryLimit) {
        this.queryLimit = queryLimit;
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
    
    
}
