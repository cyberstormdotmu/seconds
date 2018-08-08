package com.ishoal.email;

import org.junit.Test;

import static com.ishoal.email.TemplateProperties.WEBSITE_ROOT_URL;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TemplatePropertiesFactoryTest {

    @Test
    public void shouldPassInjectedVariablesIntoProperties() {

        String domain = "http://something.com";
        TemplatePropertiesFactory propsFactory = new TemplatePropertiesFactory(domain);

        TemplateProperties properties = propsFactory.createProperties();

        assertThat(properties.getValue(WEBSITE_ROOT_URL), is(domain));
    }

}