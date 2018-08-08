package com.ishoal.core.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

public class RegexMatcher extends TypeSafeMatcher<String> {

    private final Pattern pattern;

    public RegexMatcher(String regex) {
        pattern = Pattern.compile(regex);
    }

    @Override
    public void describeTo(Description description) {

        description.appendText("does not match regex " + pattern.toString());
    }

    @Override
    protected boolean matchesSafely(String s) {

        return pattern.matcher(s).matches();
    }


    public static Matcher<String> matchesRegex(String regex) {
        return new RegexMatcher(regex);
    }

}
