package com.ishoal.core.email;

import com.ishoal.email.EmailMessage;
import com.ishoal.email.EmailService;
import com.ishoal.email.spring.SpringEmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static com.ishoal.email.EmailMessage.anEmailMessage;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SpringEmailServiceTest {

    public static final String DEFAULT_FROM_ADDRESS = "noreply@the-shoal.com";

    @Mock
    private JavaMailSenderImpl mailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageSentToSender;

    private EmailService emailService;

    @Before
    public void before() {
        emailService = new SpringEmailService(mailSender);
    }

    @Test
    public void shouldPassMessageToMailSender() throws Exception {

        String to = "roger.watkins@gnomesoft.co.uk";
        String subject = "Your iShoal.com account has been activated";
        String payload = "a test email from shoal";

        EmailMessage message = anEmailMessage()
            .recipient(to)
            .subject(subject)
            .text(payload)
            .build();

        emailService.sendMessage(message);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    public void shouldSendTheEmailDataToMailSender() throws Exception {

        String to = "roger.watkins@gnomesoft.co.uk";
        String subject = "Your iShoal.com account has been activated";
        String payload = "a test email from shoal";

        EmailMessage message = anEmailMessage()
            .recipient(to)
            .subject(subject)
            .text(payload)
            .build();

        emailService.sendMessage(message);

        verify(mailSender).send(messageSentToSender.capture());
        assertThat(messageSentToSender.getValue().getTo()[0], is(to));
        assertThat(messageSentToSender.getValue().getSubject(), is(subject));
        assertThat(messageSentToSender.getValue().getText(), is(payload));
    }

    @Test
    public void shouldSetTheFromAddress() {
        String to = "roger.watkins@gnomesoft.co.uk";
        String subject = "Your iShoal.com account has been activated";
        String payload = "a test email from shoal";

        EmailMessage message = anEmailMessage()
            .recipient(to)
            .subject(subject)
            .text(payload)
            .build();

        emailService.sendMessage(message);

        verify(mailSender).send(messageSentToSender.capture());

        assertThat(messageSentToSender.getValue().getFrom(), is(DEFAULT_FROM_ADDRESS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseErrorOnInvalidEmail() {

        anEmailMessage()
            .recipient("aninvalidemail")
            .build();
    }
}
