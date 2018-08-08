package com.ishoal.core.persistence.entity;

import com.ishoal.core.domain.Role;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class UserRoleEntityTest {

    @Test
    public void shouldBeEqualIfSameInstance() {
        UserRoleEntity entity = entity(Role.BUYER);
        assertThat(entity, is(equalTo(entity)));
    }

    @Test
    public void shouldBeEqualIfIdsSame() {
        assertThat(entity(Role.BUYER), is(equalTo(entity(Role.BUYER))));
    }

    @Test
    public void shouldNotBeEqualWhenToNull() {
        assertThat(entity(Role.BUYER), is(not(equalTo(null))));
    }

    @Test
    public void shouldNotBeEqualWhenToOtherObjectType() {
        assertThat(entity(Role.BUYER), is(not(equalTo("hello"))));
    }

    @Test
    public void hashCodeIsEqualWhenIdsSame() {
        assertThat(entity(Role.BUYER).hashCode(), is(equalTo(entity(Role.BUYER).hashCode())));
    }

    private UserRoleEntity entity(Role role) {
        UserRoleEntity entity = new UserRoleEntity();
        entity.setRole(role);
        return entity;
    }
}