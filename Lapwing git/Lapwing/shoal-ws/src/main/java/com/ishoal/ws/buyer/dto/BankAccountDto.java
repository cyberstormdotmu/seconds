package com.ishoal.ws.buyer.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BankAccountDto {
    private String accountName;
    private String sortCode;
    private String accountNumber;
    private String bankName;
    private String buildingName;
    private String streetAddress;
    private String locality;
    private String postTown;
    private String postcode;

    public BankAccountDto() {

    }

    private BankAccountDto(Builder builder) {

        accountName = builder.accountName;
        sortCode = builder.sortCode;
        accountNumber = builder.accountNumber;
        bankName = builder.bankName;
        buildingName = builder.buildingName;
        streetAddress = builder.streetAddress;
        locality = builder.locality;
        postTown = builder.postTown;
        postcode = builder.postcode;
    }

    public String getAccountName() {

        return accountName;
    }

    public String getSortCode() {

        return sortCode;
    }

    public String getAccountNumber() {

        return accountNumber;
    }

    public String getBankName() {

        return bankName;
    }

    public String getBuildingName() {

        return buildingName;
    }

    public String getStreetAddress() {

        return streetAddress;
    }

    public String getLocality() {

        return locality;
    }

    public String getPostTown() {

        return postTown;
    }

    public String getPostcode() {

        return postcode;
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }


    public static Builder aBankAccount() {

        return new Builder();
    }

    public static final class Builder {
        private String accountName;
        private String sortCode;
        private String accountNumber;
        private String bankName;
        private String buildingName;
        private String streetAddress;
        private String locality;
        private String postTown;
        private String postcode;

        public Builder() {

        }

        public Builder(BankAccountDto copy) {

            this.accountName = copy.accountName;
            this.sortCode = copy.sortCode;
            this.accountNumber = copy.accountNumber;
            this.bankName = copy.bankName;
            this.buildingName = copy.buildingName;
            this.streetAddress = copy.streetAddress;
            this.locality = copy.locality;
            this.postTown = copy.postTown;
            this.postcode = copy.postcode;
        }

        public Builder accountName(String val) {

            accountName = val;
            return this;
        }

        public Builder sortCode(String val) {

            sortCode = val;
            return this;
        }

        public Builder accountNumber(String val) {

            accountNumber = val;
            return this;
        }

        public Builder bankName(String val) {

            bankName = val;
            return this;
        }

        public Builder buildingName(String val) {

            buildingName = val;
            return this;
        }

        public Builder streetAddress(String val) {

            streetAddress = val;
            return this;
        }

        public Builder locality(String val) {

            locality = val;
            return this;
        }

        public Builder postTown(String val) {

            postTown = val;
            return this;
        }

        public Builder postcode(String val) {

            postcode = val;
            return this;
        }

        public BankAccountDto build() {

            return new BankAccountDto(this);
        }
    }
}
