package com.ishoal.ws.admin.dto;

public class AdminOrderBuyerDto {
    private String buyerUserName;
    private String buyerForename;
    private String buyerSurname;

    private AdminOrderBuyerDto() {
        super();
    }

    private AdminOrderBuyerDto(Builder builder) {
        this();
        buyerUserName = builder.buyerUserName;
        buyerForename = builder.buyerForename;
        buyerSurname = builder.buyerSurname;
    }

    public static Builder aBuyer() {
        return new Builder();
    }

    public String getBuyerUserName() {
        return buyerUserName;
    }

    public String getBuyerForename() {
        return buyerForename;
    }

    public String getBuyerSurname() {
        return buyerSurname;
    }

    public static final class Builder {
        private String buyerUserName;
        private String buyerForename;
        private String buyerSurname;

        private Builder() {
        }

        public Builder buyerUserName(String val) {
            buyerUserName = val;
            return this;
        }

        public Builder buyerForename(String val) {
            buyerForename = val;
            return this;
        }

        public Builder buyerSurname(String val) {
            buyerSurname = val;
            return this;
        }

        public AdminOrderBuyerDto build() {
            return new AdminOrderBuyerDto(this);
        }
    }
}
