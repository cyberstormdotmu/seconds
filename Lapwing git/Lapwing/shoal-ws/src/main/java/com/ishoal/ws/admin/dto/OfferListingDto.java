package com.ishoal.ws.admin.dto;

import org.joda.time.DateTime;

public class OfferListingDto {
    private String productCode;
    private String productName;
    private String offerReference;
    private DateTime offerStartDateTime;
    private DateTime offerEndDateTime;

    private OfferListingDto() {
        super();
    }

    private OfferListingDto(Builder builder) {
        this();
        productCode = builder.productCode;
        productName = builder.productName;
        offerReference = builder.offerReference;
        offerStartDateTime = builder.offerStartDateTime;
        offerEndDateTime = builder.offerEndDateTime;
       
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getOfferReference() {
        return offerReference;
    }

    public DateTime getOfferStartDateTime() {
        return offerStartDateTime;
    }

    public DateTime getOfferEndDateTime() {
        return offerEndDateTime;
    }

  

	public static Builder anOfferListing() {
        return new Builder();
    }


    public static final class Builder {
        private String productCode;
        private String productName;
        private String offerReference;
        private DateTime offerStartDateTime;
        private DateTime offerEndDateTime;
       
        private Builder() {
        }

        public Builder productCode(String val) {
            productCode = val;
            return this;
        }

       
        public Builder productName(String val) {
            productName = val;
            return this;
        }

        public Builder offerReference(String val) {
            offerReference = val;
            return this;
        }

        public Builder offerStartDateTime(DateTime val) {
            offerStartDateTime = val;
            return this;
        }

        public Builder offerEndDateTime(DateTime val) {
            offerEndDateTime = val;
            return this;
        }

        public OfferListingDto build() {
            return new OfferListingDto(this);
        }
    }
}
