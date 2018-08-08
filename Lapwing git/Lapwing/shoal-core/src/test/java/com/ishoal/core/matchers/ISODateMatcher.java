package com.ishoal.core.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.joda.time.format.ISODateTimeFormat;

public class ISODateMatcher extends TypeSafeMatcher<String> {

    @Override
    protected boolean matchesSafely(String s) {
        try {
            ISODateTimeFormat.dateTime().parseDateTime(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("not in ISO date format");
    }

    @Factory
    public static <T> Matcher<String> inIsoDateFormat() {
        return new ISODateMatcher();
    }
}
