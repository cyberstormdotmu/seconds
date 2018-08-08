package com.ishoal.ws.security;

import com.ishoal.core.security.SecurePassword;
import org.junit.Test;

import java.util.HashSet;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class LoginUserDetailsTest {


    @Test
    public void shouldBeEqualIfSameInstance() {
        LoginUserDetails userDetails = userDetails(1);
        assertThat(userDetails, is(equalTo(userDetails)));
    }

    @Test
    public void shouldBeEqualIfIdsSame() {
        assertThat(userDetails(1), is(equalTo(userDetails(1))));
    }

    @Test
    public void shouldNotBeEqualIfIdsDiffer() {
        assertThat(userDetails(1), is(not(equalTo(userDetails(2)))));
    }

    @Test
    public void shouldNotBeEqualWhenToNull() {
        assertThat(userDetails(1), is(not(equalTo(null))));
    }

    @Test
    public void shouldNotBeEqualWhenToOtherObjectType() {
        assertThat(userDetails(1), is(not(equalTo("hello"))));
    }

    @Test
    public void hashCodeIsEqualWhenIdsSame() {
        assertThat(userDetails(1).hashCode(), is(equalTo(userDetails(1).hashCode())));
    }

    @Test
    public void theUserIdIsTheIdThatWasSet() {
        LoginUserDetails userDetails = new LoginUserDetails(142L, "user", SecurePassword.fromClearText("password"), new HashSet<>());
        assertThat(userDetails.getUserId(), is(142L));
    }

    private LoginUserDetails userDetails(long id) {
        LoginUserDetails userDetails = new LoginUserDetails(id, "user", SecurePassword.fromClearText("password"), new HashSet<>());
        return userDetails;
    }
}