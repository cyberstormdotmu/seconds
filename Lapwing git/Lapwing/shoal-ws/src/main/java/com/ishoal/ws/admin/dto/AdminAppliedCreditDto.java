package com.ishoal.ws.admin.dto;

import com.ishoal.ws.common.dto.TaxableAmountDto;
import org.joda.time.DateTime;

public class AdminAppliedCreditDto {
    private String status;
    private TaxableAmountDto amount;
    private String spendType;
    private String originalOrderReference;
    private String originalOrderLineProductCode;
    private String orderSpentOnReference;
    private DateTime created;
    private DateTime modified;

    private AdminAppliedCreditDto() {
        super();
    }

    private AdminAppliedCreditDto(Builder builder) {
        this();
        status = builder.status;
        amount = builder.amount;
        spendType = builder.spendType;
        originalOrderReference = builder.originalOrderReference;
        originalOrderLineProductCode = builder.originalOrderLineProductCode;
        orderSpentOnReference = builder.orderSpentOnReference;
        created = builder.created;
        modified = builder.modified;
    }

    public static Builder anAppliedCredit() {
        return new Builder();
    }

    public String getStatus() {
        return status;
    }

    public TaxableAmountDto getAmount() {
        return amount;
    }

    public String getSpendType() {
        return spendType;
    }

    public String getOriginalOrderReference() {
        return originalOrderReference;
    }

    public String getOriginalOrderLineProductCode() {
        return originalOrderLineProductCode;
    }

    public String getOrderSpentOnReference() {
        return orderSpentOnReference;
    }

    public DateTime getCreated() {
        return created;
    }

    public DateTime getModified() {
        return modified;
    }


    public static final class Builder {
        private String status;
        private TaxableAmountDto amount;
        private String spendType;
        private String originalOrderReference;
        private String originalOrderLineProductCode;
        private String orderSpentOnReference;
        private DateTime created;
        private DateTime modified;

        private Builder() {
        }

        public Builder status(String val) {
            status = val;
            return this;
        }

        public Builder amount(TaxableAmountDto val) {
            amount = val;
            return this;
        }

        public Builder spendType(String val) {
            spendType = val;
            return this;
        }

        public Builder originalOrderReference(String val) {
            originalOrderReference = val;
            return this;
        }

        public Builder originalOrderLineProductCode(String val) {
            originalOrderLineProductCode = val;
            return this;
        }

        public Builder orderSpentOnReference(String val) {
            orderSpentOnReference = val;
            return this;
        }

        public Builder created(DateTime val) {
            created = val;
            return this;
        }

        public Builder modified(DateTime val) {
            modified = val;
            return this;
        }

        public AdminAppliedCreditDto build() {
            return new AdminAppliedCreditDto(this);
        }
    }
}
