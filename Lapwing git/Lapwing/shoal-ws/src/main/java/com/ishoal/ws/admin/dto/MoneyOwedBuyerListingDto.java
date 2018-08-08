package com.ishoal.ws.admin.dto;

public class MoneyOwedBuyerListingDto {
    private String vendorName;
    private String userName;
    private String owedCredit;

    private MoneyOwedBuyerListingDto() {
        super();
    }

    private MoneyOwedBuyerListingDto(Builder builder) {
        this();
        vendorName = builder.vendorName;
        userName = builder.userName;
        owedCredit = builder.owedCredit;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getUserName() {
        return userName;
    }

    public String getOwedCredit() {
        return owedCredit;
    }

    public static final class Builder {
        private String vendorName;
        private String userName;
        private String owedCredit;

        private Builder() {
        }

        public Builder buyerId(String val) {
            vendorName = val;
            return this;
        }

        public Builder userName(String val) {
            userName = val;
            return this;
        }

        public Builder firstName(String val) {
            owedCredit = val;
            return this;
        }

    }

}
