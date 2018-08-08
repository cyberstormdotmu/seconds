package com.ishoal.core.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

public class GUIDMatcher extends TypeSafeMatcher<String> {

    private Pattern guidRegex = Pattern.compile("^[A-F0-9]{8}\\-([A-F0-9]{4}\\-){3}[A-F0-9]{12}$",
        Pattern.CASE_INSENSITIVE);

    @Override
    protected boolean matchesSafely(String s) {

        java.util.regex.Matcher matcher = guidRegex.matcher(s);
        return matcher.matches();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("not a guid");
    }

    @Factory
    public static <T> Matcher<String> aGuid() {
        return new GUIDMatcher();
    }
}
