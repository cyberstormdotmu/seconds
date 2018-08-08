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
package com.cubeia.backoffice.operator.service.entity;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Index;

@Entity
public class OperatorWalletRequestLog {
    
    private Long id;
    
    private Long operatorId;
    
    private String type;
    
    private Date timestamp = new Date();
    
    private Long timestampMillis = System.currentTimeMillis();
    
    private String payload;
    
    private String response;
    
    private String requestId;
    
    private String message;
    
    private RequestStatus status = RequestStatus.CREATED;
    
    private List<OperatorWalletRequestLogEvent> events = new ArrayList<>();
    
    private String amount;

    private String currency;
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Index(name="operatorIdIndex")
    public Long getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Column(length=1024)
    public String getPayload() {
        return payload;
    }
    
    public void setPayload(String payload) {
        this.payload = payload;
    }
    
    @Column(length=1024)
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public Long getTimestampMillis() {
        return timestampMillis;
    }
    
    public void setTimestampMillis(Long timestampMillis) {
        this.timestampMillis = timestampMillis;
    }
    
    @Index(name="statusIndex")
    @Enumerated(EnumType.STRING)
    public RequestStatus getStatus() {
        return status;
    }
    
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
    
    @Index(name="requestIdIndex")
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @OneToMany(mappedBy="operatorWalletRequestLog",cascade=ALL,fetch=FetchType.EAGER)
    public List<OperatorWalletRequestLogEvent> getEvents() {
        return events;
    }
    
    public void setEvents(List<OperatorWalletRequestLogEvent> events) {
        this.events = events;
    }
    
    public String getAmount() {
        return amount;
    }
    
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getCurrency() {
        return currency;
    }
}
