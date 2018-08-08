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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement(name = "operatorWalletRequestLogDTO")
public class OperatorWalletRequestLogDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    
    private Long operatorId;
    
    private String type;
    
    private Date timestamp = new Date();
    
    private Long timestampMillis = System.currentTimeMillis();
    
    private String payload;
    
    private String response;
    
    private String requestId;
    
    private String message;
    
    private String amount;
    
    private String currency;
    
    private OperatorWalletRequestStatus requestStatus = OperatorWalletRequestStatus.CREATED;
    
    private List<OperatorWalletRequestLogEventDTO> events = new ArrayList<OperatorWalletRequestLogEventDTO>();
    
    @XmlElement
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @XmlElement
    public Long getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
    
    @XmlElement
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @XmlElement
    public String getPayload() {
        return payload;
    }
    
    public void setPayload(String payload) {
        this.payload = payload;
    }
    
    @XmlElement
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    @XmlElement
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    @XmlElement
    public Long getTimestampMillis() {
        return timestampMillis;
    }
    
    public void setTimestampMillis(Long timestampMillis) {
        this.timestampMillis = timestampMillis;
    }
    
    @XmlElement
    public OperatorWalletRequestStatus getRequestStatus() {
        return requestStatus;
    }
    
    public void setRequestStatus(OperatorWalletRequestStatus status) {
        this.requestStatus = status;
    }
    
    @XmlElement
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    @XmlElement
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @XmlElement
    public List<OperatorWalletRequestLogEventDTO> getEvents() {
        return events;
    }
    
    public void setEvents(List<OperatorWalletRequestLogEventDTO> events) {
        this.events = events;
    }
    
    @XmlElement
    public String getAmount() {
        return amount;
    }
    
    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    @XmlElement
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
