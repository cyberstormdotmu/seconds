package com.ishoal.core.security;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class SecurePasswordTest {

    @Test
    public void shouldHashAClearTextPassword() {
        assertThat(SecurePassword.fromClearText("password").getValue(), is(not("password")));
    }

    @Test
    public void shouldNotRehashAHashedPassword() {
        String hash = "$2a$10$vXNsJEXKLi0DrXvK1tSE0eu09AnXjoPF5rJU8iBylHMinkMXFcbKy";

        assertThat(SecurePassword.fromBCryptHash(hash).getValue(), is(hash));
    }

    @Test
    public void hashFunctionShouldWork() {
        String clearText = "v3ry53cur3p455w0rd!";
        SecurePassword password = SecurePassword.fromClearText(clearText);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertThat(encoder.matches(clearText, password.getValue()), is(true));
    }

    @Test
    public void shouldGiveDifferentValueOnEachTimePasswordIsHashed() {
        String clearText = "v3ry53cur3p455w0rd!";
        SecurePassword saltedPassword = SecurePassword.fromClearText(clearText);

        SecurePassword differentSalt = SecurePassword.fromClearText(clearText);

        assertThat(differentSalt.getValue(), is(not(equalTo(saltedPassword.getValue()))));
    }
}