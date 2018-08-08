package com.ishoal.core.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class GenericMatcher<T> extends TypeSafeMatcher<T> {
    private FunctionMatcher<T> matcher;

    public GenericMatcher(FunctionMatcher<T> matcher) {

        this.matcher = matcher;
    }

    protected boolean matchesSafely(T obj) {

        return matcher.matches(obj);
    }

    @Override
    public void describeTo(Description description) {

        description.appendText("usernames are not equal");
    }

    public static <T> GenericMatcher lamdaMatch(FunctionMatcher<T> matcher) {

        return new GenericMatcher<>(matcher);
    }

    public interface FunctionMatcher<T> {
        boolean matches(T obj);
    }

}
