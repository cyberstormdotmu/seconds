package com.ishoal.ws.admin.dto;

import com.ishoal.ws.common.dto.TaxableAmountDto;
import org.joda.time.DateTime;

public class AdminBuyerCreditDto {

    private TaxableAmountDto amount;
    private DateTime created;

    private AdminBuyerCreditDto() {
        super();
    }

    private AdminBuyerCreditDto(Builder builder) {
        this();
        amount = builder.amount;
        created = builder.created;
    }

    public static Builder aCredit() {
        return new Builder();
    }

    public TaxableAmountDto getAmount() {
        return amount;
    }

    public DateTime getCreated() {
        return created;
    }

    public static final class Builder {
        private TaxableAmountDto amount;
        private DateTime created;

        private Builder() {
        }

        public Builder amount(TaxableAmountDto val) {
            amount = val;
            return this;
        }

        public Builder created(DateTime val) {
            created = val;
            return this;
        }

        public AdminBuyerCreditDto build() {
            return new AdminBuyerCreditDto(this);
        }
    }
}
