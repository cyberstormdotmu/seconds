package com.ishoal.app.integrationtest.buyer.profile;

import com.icegreen.greenmail.util.GreenMailUtil;
import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.ishoal.app.integrationtest.DirtiesDb;
import com.ishoal.app.integrationtest.interactions.buyer.BuyerTestHelper;
import org.junit.Before;
import org.junit.Test;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.ishoal.app.integrationtest.data.BuyerProfileTestData.NEW_USER_NAME;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@DirtiesDb
public class BuyerActivateIT extends AbstractIntegrationTest {

    private BuyerTestHelper helper;

    @Before
    public void before() {

        helper = new BuyerTestHelper(authenticationHelper);
    }

    @Test
    public void shouldAuthoriseUserAsBuyer() throws Exception {

        helper.registerANewBuyer();
        helper.activateBuyer();

        assertThatEmailWasSentTo(NEW_USER_NAME);
    }

    private void assertThatEmailWasSentTo(String newUserName) throws MessagingException {
        greenMail.waitForIncomingEmail(5000, 1);
        MimeMessage message = greenMail.getReceivedMessages()[0];
        assertThat(message.getRecipients(Message.RecipientType.TO)[0], is(new InternetAddress(newUserName)));
        assertThat(message.getSubject(), containsString("approved"));
        String messageBody = GreenMailUtil.getBody(message).trim();
        assertThat(messageBody, containsString("activated"));
        assertThatEmailHasCorrectLinkInIt(messageBody);
    }

    private void assertThatEmailHasCorrectLinkInIt(String message) {

        assertThat(message, containsString("http://www.ishoal.com"));
    }
}
