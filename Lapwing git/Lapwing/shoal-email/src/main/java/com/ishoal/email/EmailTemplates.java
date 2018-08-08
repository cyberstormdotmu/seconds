package com.ishoal.email;

import static com.ishoal.email.EmailMessage.anEmailMessage;
import static com.ishoal.email.TemplateProperties.EMAIL;
import static com.ishoal.email.TemplateProperties.USER_FIRST_NAME;
import static com.ishoal.email.TemplateProperties.USER_SURNAME;
import static com.ishoal.email.TemplateProperties.WEBSITE_ROOT_URL;

public class EmailTemplates {
    private EmailTemplates() {

    }

    public static EmailMessage.Builder anAccountActivationMessage(TemplateProperties props) {

        return anEmailMessage()
            .recipient(props.getValue(EMAIL))
            .subject("Your SilverWing account was approved !")
            .text(String.format("%s %s,\n\n" +
                "Your account has now been activated." +
                "\n\n" +
                "Please navigate your browser to %s/public/#/login to login.\n" +
                "\n\n" +
                "Kind Regards,\n" +
                "The SilverWing Team.",
                props.getValue(USER_FIRST_NAME),
                props.getValue(USER_SURNAME),
                props.getValue(WEBSITE_ROOT_URL)
            ));
    }
    
    public static EmailMessage.Builder anAccountDeactivationMessage(TemplateProperties props) {

        return anEmailMessage()
            .recipient(props.getValue(EMAIL))
            .subject("Your SilverWing account was rejected !")
            .text(String.format("%s %s,\n\n" +
                "Your account has been rejected." +
                "\n\n" +
                "Kind Regards,\n" +
                "The SilverWing Team.",
                props.getValue(USER_FIRST_NAME),
                props.getValue(USER_SURNAME),
                props.getValue(WEBSITE_ROOT_URL)
            ));
    }
}
