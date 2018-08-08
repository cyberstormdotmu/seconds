package com.ishoal.email;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailMessage {
    private InternetAddress recipient;
    private String subject;
    private String messageBody;

    private EmailMessage(Builder builder) {

        recipient = builder.recipient;
        subject = builder.subject;
        messageBody = builder.messageBody;
    }

    public static Builder anEmailMessage() {

        return new Builder();
    }

    public InternetAddress getRecipient() {

        return recipient;
    }

    public String getSubject() {

        return subject;
    }

    public String getText() {

        return messageBody;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("recipient", recipient)
                .append("subject", subject)
                .append("messageBody", messageBody)
                .toString();
    }

    public static final class Builder {
        private InternetAddress recipient;
        private String subject;
        private String messageBody;

        public Builder() {

        }

        public Builder(EmailMessage copy) {

            this.recipient = copy.recipient;
            this.subject = copy.subject;
            this.messageBody = copy.messageBody;
        }

        public Builder recipient(String val) {

            validateEmailAddress(val);
            return this;
        }

        public Builder subject(String val) {

            subject = val;
            return this;
        }

        public Builder text(String val) {

            messageBody = val;
            return this;
        }

        public EmailMessage build() {

            return new EmailMessage(this);
        }

        private void validateEmailAddress(String val) {

            try {
                if (!val.contains("@")) {
                    throw new IllegalArgumentException(val);
                }
                recipient = new InternetAddress(val);
            } catch (AddressException e) {
                throw new IllegalArgumentException(val, e);
            }
        }
    }
}
