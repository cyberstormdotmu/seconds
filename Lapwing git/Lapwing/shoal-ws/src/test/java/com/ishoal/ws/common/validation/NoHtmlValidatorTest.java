package com.ishoal.ws.common.validation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NoHtmlValidatorTest {

    private NoHtml.Validator validator = new NoHtml.Validator();

    @Test
    public void shouldAllowNull() {
        assertTrue(isValid(null));
    }

    @Test
    public void shouldAllowEmptyString() {
        assertTrue(isValid(""));
    }

    @Test
    public void shouldAllowPlainText() {
        assertTrue(isValid("this is not a love song"));
    }

    @Test
    public void shoulNotAllowHtml() {
        assertFalse(isValid("<p>this is not a love song</p>"));
    }

    @Test
    public void shoulNotAllowScriptTag() {
        assertFalse(isValid("<script>alert('attacked')</script>"));
    }

    @Test
    public void shoulNotAllowObfuscatedScriptTag() {
        assertFalse(isValid("%3c%73%63%72%69%70%74%3e%77%69%6e%64%6f%77%2e%6f%6e%6c%6f%61%64%20%3d%20%66%75%6e%63%74%69%6f%6e%28%29%20%7b%76%61%72%20%6c%69%6e%6b%3d%64%6f%63%75%6d%65%6e%74%2e%67%65%74%45%6c%65%6d%65%6e%74%73%42%79%54%61%67%4e%61%6d%65%28%22%61%22%29%3b%6c%69%6e%6b%5b%30%5d%2e%68%72%65%66%3d%22%68%74%74%70%3a%2f%2f%61%74%74%61%63%6b%65%72%2d%73%69%74%65%2e%63%6f%6d%2f%22%3b%7d%3c%2f%73%63%72%69%70%74%3e"));
    }

    private boolean isValid(String str) {
        return validator.isValid(str, null);
    }
}