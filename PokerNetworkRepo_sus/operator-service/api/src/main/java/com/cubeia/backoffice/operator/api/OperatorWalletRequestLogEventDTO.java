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
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement(name = "OperatorWalletRequestLogEvent")
public class OperatorWalletRequestLogEventDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    
    private Date timestamp = new Date();
    
    private Long timestampMillis = System.currentTimeMillis();
    
    private String message;
    
    private OperatorWalletRequestEventStatus newStatus = OperatorWalletRequestEventStatus.CREATED;
    
    @XmlElement
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public OperatorWalletRequestEventStatus getNewStatus() {
        return newStatus;
    }
    
    public void setNewStatus(OperatorWalletRequestEventStatus newStatus) {
        this.newStatus = newStatus;
    }
    
    @XmlElement
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
