package com.ishoal.core.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.DateTime;

public class BuyerWithdrawCredit {
	
	private Long id;
	private BuyerProfile buyer;
	private BigDecimal requestedAmount;
	private DateTime requestedDate;
	private String paymentType;
	private Date receivedDate;
	private String withdrawReference;
	private String withdrawStatus;
	private DateTime confirmedDate;
	
	private BuyerWithdrawCredit(Builder builder) {
		id = builder.id;
		buyer = builder.buyer;
		requestedAmount = builder.requestedAmount;
		requestedDate=builder.requestedDate;
		paymentType = builder.paymentType;
		receivedDate = builder.receivedDate;
		withdrawReference = builder.withdrawReference;
		withdrawStatus = builder.withdrawStatus;
		confirmedDate = builder.confirmedDate;
		
    }
	
	public static Builder aBuyerWithdrawCredit() {
        return new Builder();
    }

	public Long getId() {
		return id;
	}

	public BuyerProfile getBuyer() {
		return buyer;
	}

	public BigDecimal getRequestedAmount() {
		return requestedAmount;
	}

	public DateTime getRequestedDate() {
		return requestedDate;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public String getWithdrawReference() {
		return withdrawReference;
	}

	public String getWithdrawStatus() {
		return withdrawStatus;
	}

	public DateTime getConfirmedDate() {
		return confirmedDate;
	}
	
	
	public static final class Builder {
		
		private Long id;
		private BuyerProfile buyer;
		private BigDecimal requestedAmount;
		private DateTime requestedDate;
		private String paymentType;
		private Date receivedDate;
		private String withdrawReference;
		private String withdrawStatus;
		private DateTime confirmedDate;
        
		private Builder() {}

        public Builder id(Long val) {
        	id = val;
            return this;
        }
        public Builder buyer(BuyerProfile val) {
        	buyer = val;
            return this;
        }
        public Builder requestedAmount(BigDecimal val) {
        	requestedAmount = val;
            return this;
        }
        public Builder requestedDate(DateTime val) {
        	requestedDate = val;
            return this;
        }
        public Builder paymentType(String val) {
        	paymentType = val;
            return this;
        }
        public Builder receivedDate(Date val) {
        	receivedDate = val;
            return this;
        }
        public Builder withdrawReference(String val) {
        	withdrawReference = val;
            return this;
        }
        public Builder withdrawStatus(String val) {
        	withdrawStatus = val;
            return this;
        }
        public Builder confirmedDate(DateTime val) {
        	confirmedDate = val;
            return this;
        }
        public BuyerWithdrawCredit build() {
            return new BuyerWithdrawCredit(this);
        }
	}
	
}
