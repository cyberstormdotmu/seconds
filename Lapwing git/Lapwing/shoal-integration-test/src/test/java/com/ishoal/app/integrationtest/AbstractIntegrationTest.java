package com.ishoal.app.integrationtest;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.ishoal.app.ShoalApp;
import com.ishoal.payment.buyer.config.ShoalPaymentTestConfiguration;
import com.ishoal.sms.config.ShoalSmsTestConfiguration;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static com.jayway.restassured.config.JsonConfig.jsonConfig;
import static com.jayway.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("/test.properties")
@SpringApplicationConfiguration(classes = {ShoalApp.class, ShoalPaymentTestConfiguration.class, ShoalSmsTestConfiguration.class})
@WebIntegrationTest(randomPort = true)
@TestExecutionListeners({
        DbMigrationTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class
})
public abstract class AbstractIntegrationTest {

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    @Value("${local.server.port}")
    private int port;

    protected AuthenticationHelper authenticationHelper;

    @Before
    public void setup() {
        RestAssured.port = port;
        RestAssuredConfig config = RestAssured.config().jsonConfig(
            jsonConfig().numberReturnType(BIG_DECIMAL));
        RestAssured.config = config;
        Filters.XSRF_FILTER.reset();
        ResetOptions resetOptions = new ResetOptions();
        resetOptions.setPort(port);
        resetOptions.setConfig(config);
        authenticationHelper = new AuthenticationHelper(resetOptions);
    }

    @After
    public void teardown() {
        RestAssured.reset();
        greenMail.reset();
    }

    protected void usingNoAuthentication() {
        authenticationHelper.usingNoAuthentication();
    }
}