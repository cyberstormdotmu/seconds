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
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement(name = "money")
public class Money implements Serializable {

    private static final long serialVersionUID = 2153378632814990027L;

    private String currency;
    
    private BigDecimal amount;
    
    public Money(){}
    
    public Money(String currency, BigDecimal amount){
        this.currency = currency;
        this.amount = amount;}
    
    public Money(String currency, String amount){
        this.currency = currency;
        this.amount = new BigDecimal(amount);
    }

    @XmlElement
    public BigDecimal getAmount() {
		return amount;
	}
    
    public void setAmount(BigDecimal amount) {
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