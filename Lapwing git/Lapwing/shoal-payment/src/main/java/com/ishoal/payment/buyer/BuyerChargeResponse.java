package com.ishoal.payment.buyer;

import org.joda.time.DateTime;

public class BuyerChargeResponse {
    private ChargeReference chargeReference;
    private DateTime created;

    private BuyerChargeResponse(Builder builder) {
        chargeReference = builder.chargeReference;
        created = builder.created;
    }

    public static Builder aChargeResponse() {
        return new Builder();
    }

    public ChargeReference getChargeReference() {
        return chargeReference;
    }

    public DateTime getCreated() {
        return created;
    }

    public static final class Builder {
        private ChargeReference chargeReference;
        private DateTime created;

        private Builder() {
        }

        public Builder chargeReference(ChargeReference val) {
            chargeReference = val;
            return this;
        }

        public Builder created(DateTime val) {
            created = val;
            return this;
        }

        public BuyerChargeResponse build() {
            return new BuyerChargeResponse(this);
        }
    }
}
