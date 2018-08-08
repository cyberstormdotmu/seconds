package com.ishoal.ws.buyer.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ContactDto {
    private String title;
    private String firstName;
    private String surname;
    private String emailAddress;
    private String password;
    private String phoneNumber;

    public ContactDto() {

    }

    private ContactDto(Builder builder) {

        title = builder.title;
        firstName = builder.firstName;
        surname = builder.surname;
        emailAddress = builder.emailAddress;
        password = builder.password;
        phoneNumber = builder.phoneNumber;
    }

    public String getTitle() {

        return title;
    }

    public String getFirstName() {

        return firstName;
    }

    public String getSurname() {

        return surname;
    }

    public String getEmailAddress() {

        return emailAddress;
    }

    public String getPassword() {

        return password;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }

    public static Builder aContact() {

        return new Builder();
    }


    public static final class Builder {
        private String title;
        private String firstName;
        private String surname;
        private String emailAddress;
        private String password;
        private String phoneNumber;

        public Builder() {

        }

        public Builder(ContactDto copy) {

            this.title = copy.title;
            this.firstName = copy.firstName;
            this.surname = copy.surname;
            this.emailAddress = copy.emailAddress;
            this.password = copy.password;
            this.phoneNumber = copy.phoneNumber;
        }

        public Builder title(String val) {

            title = val;
            return this;
        }

        public Builder firstName(String val) {

            firstName = val;
            return this;
        }

        public Builder surname(String val) {

            surname = val;
            return this;
        }

        public Builder emailAddress(String val) {

            emailAddress = val;
            return this;
        }

        public Builder password(String val) {

            password = val;
            return this;
        }

        public Builder phoneNumber(String val) {

            phoneNumber = val;
            return this;
        }

        public ContactDto build() {

            return new ContactDto(this);
        }
    }
}
