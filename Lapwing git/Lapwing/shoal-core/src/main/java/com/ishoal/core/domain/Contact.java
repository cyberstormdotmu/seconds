package com.ishoal.core.domain;

public class Contact {

    private String title;
    private String firstName;
    private String surname;
    private String emailAddress;
    private String password;
    private String phoneNumber;

    private Contact(Builder builder) {

        phoneNumber = builder.phoneNumber;
        password = builder.password;
        emailAddress = builder.emailAddress;
        surname = builder.surname;
        firstName = builder.firstName;
        title = builder.title;
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



    public static Builder aContact() {

        return new Builder();
    }

    public static final class Builder {
        private String phoneNumber;
        private String password;
        private String emailAddress;
        private String surname;
        private String firstName;
        private String title;

        public Builder() {

        }

        public Builder(Contact copy) {

            this.phoneNumber = copy.phoneNumber;
            this.password = copy.password;
            this.emailAddress = copy.emailAddress;
            this.surname = copy.surname;
            this.firstName = copy.firstName;
            this.title = copy.title;
        }

        public Builder phoneNumber(String val) {

            phoneNumber = val;
            return this;
        }

        public Builder password(String val) {

            password = val;
            return this;
        }

        public Builder emailAddress(String val) {

            emailAddress = val;
            return this;
        }

        public Builder surname(String val) {

            surname = val;
            return this;
        }

        public Builder firstName(String val) {

            firstName = val;
            return this;
        }

        public Builder title(String val) {

            title = val;
            return this;
        }

        public Contact build() {

            return new Contact(this);
        }
    }
}
