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

import java.math.BigDecimal;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cubeia.backoffice.wallet.api.dto.Account;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Defines a request for a transfer.
 *
 * Depending on the account's type the system will
 * determine which other account will be used in the transaction.
 * <ul>
 * <li>
 * If the account is of type {@link Account.AccountType#SESSION_ACCOUNT} the transfer will be against the
 * user's corresponding {@link Account.AccountType#STATIC_ACCOUNT}.
 * </li>
 * <li>
 * If it is a {@link Account.AccountType#STATIC_ACCOUNT} the transfer will be done against the user's remote wallet account.
 * </li>
 * </ul>
 *
 * @author Fredrik Johansson, Cubeia Ltd
 * @deprecated use TransactionRequest instead
 */
@XmlRootElement(name="TransferRequest")
public class TransferRequest  {
	
	public static enum TransferType {
		/**
		 * The Account will be debited, i.e. money will
		 * be withdrawn from the account.
		 */
		DEBIT,
		
		/**
		 * The Account will be credited, i.e. money
		 * will be added to the account.
		 */
		CREDIT
	}
	
	/**
	 * Used for idempotency
	 */
	private UUID requestId;
	
	private BigDecimal amount;
	
	private TransferType transferType;
	
	private Long operatorId;
	
	private String comment;
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@XmlElement
	public UUID getRequestId() {
		return requestId;
	}

	public void setRequestId(UUID requestId) {
		this.requestId = requestId;
	}

	@XmlElement
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@XmlElement
	public TransferType getTransferType() {
		return transferType;
	}

	public void setTransferType(TransferType transferType) {
		this.transferType = transferType;
	}

	@XmlElement
	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	@XmlElement
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
