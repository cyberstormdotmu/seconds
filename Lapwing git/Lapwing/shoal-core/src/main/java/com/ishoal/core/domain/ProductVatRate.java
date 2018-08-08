package com.ishoal.core.domain;

import org.joda.time.DateTime;

public class ProductVatRate {
	private final Long id;
    private VatRate vatRate;
    private DateTime startDateTime;
    private DateTime endDateTime;

    private ProductVatRate(Builder builder) {
        vatRate = builder.vatRate;
        startDateTime = builder.startDateTime;
        endDateTime = builder.endDateTime;
        id=builder.id;
        if (this.startDateTime == null) {
            this.startDateTime = new DateTime().withTimeAtStartOfDay();
        }
    }

    public static Builder aProductVatRate() {
        return new Builder();
    }

    public VatRate getVatRate() {
        return vatRate;
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public Long getId() {
		return id;
	}

	public static final class Builder {
    	private Long id;
        private VatRate vatRate;
        private DateTime startDateTime;
        private DateTime endDateTime;

        private Builder() {
        }

        public Builder vatRate(VatRate val) {
            vatRate = val;
            return this;
        }

        public Builder startDateTime(DateTime val) {
            startDateTime = val;
            return this;
        }

        public Builder endDateTime(DateTime val) {
            endDateTime = val;
            return this;
        }
        public Builder id(Long val) {
            id = val;
            return this;
        }

        public ProductVatRate build() {
            return new ProductVatRate(this);
        }
    }
}
