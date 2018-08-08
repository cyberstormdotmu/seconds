package com.ishoal.ws.common.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class UriHelperTest {

    @Test (expected=IllegalStateException.class)
    public void invalidUrlIsConvertedIntoRuntimeException() {
        UriHelper.uri("!http://1234.1234.1234/-----bobbins-url");
    }
    
    @Test
    public void shouldBeAbleToConstructUriFromValidStringl() {
        assertThat(UriHelper.uri("http://google.co.uk"), is(notNullValue()));
    }
}