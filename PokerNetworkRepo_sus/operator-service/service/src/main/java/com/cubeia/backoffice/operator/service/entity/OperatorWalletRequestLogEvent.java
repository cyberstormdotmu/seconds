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

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Index;

@Entity
public class OperatorWalletRequestLogEvent {
    
    private Long id;
    
    private OperatorWalletRequestLog operatorWalletRequestLog;
    
    private Date timestamp = new Date();
    
    private Long timestampMillis = System.currentTimeMillis();
    
    private String message;
    
    private RequestEventStatus newStatus = RequestEventStatus.CREATED;
    
    public OperatorWalletRequestLogEvent() {
        
    }
    
    public OperatorWalletRequestLogEvent(OperatorWalletRequestLog entry, RequestEventStatus newStatus) {
        this(entry, newStatus, null);
    }
    
    public OperatorWalletRequestLogEvent(OperatorWalletRequestLog entry, RequestEventStatus newStatus, String message) {
        operatorWalletRequestLog = entry;
        this.newStatus = newStatus;
        this.message = message;
    }
    
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
    public RequestEventStatus getNewStatus() {
        return newStatus;
    }
    
    public void setNewStatus(RequestEventStatus newStatus) {
        this.newStatus = newStatus;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @ManyToOne
    public OperatorWalletRequestLog getOperatorWalletRequestLog() {
        return operatorWalletRequestLog;
    }
    
    public void setOperatorWalletRequestLog(OperatorWalletRequestLog operatorWalletRequestLog) {
        this.operatorWalletRequestLog = operatorWalletRequestLog;
    }
}
