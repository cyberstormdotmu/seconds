package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class UserIdTest {

    @Test
    public void shouldConstructEmptyIdFromNull() {
        assertThat(UserId.from(null), is(UserId.emptyUserId));
    }
    
    @Test
    public void shouldBeEqualIfSameInstance() {
        UserId id = UserId.from(345L);
        assertThat(id, is(equalTo(id)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(UserId.from(456L), is(equalTo(UserId.from(456L))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(UserId.from(457L), is(not(equalTo(UserId.from(783L)))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(UserId.from(456L), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(UserId.from(456L), is(not(equalTo("hello"))));
    }    
}