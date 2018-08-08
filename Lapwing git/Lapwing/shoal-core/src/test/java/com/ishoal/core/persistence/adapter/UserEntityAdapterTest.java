package com.ishoal.core.persistence.adapter;

import com.ishoal.core.persistence.entity.UserEntity;
import com.ishoal.core.security.SecurePassword;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserEntityAdapterTest {

    public static final String HASHED_PASSWORD = "$2a$10$vXNsJEXKLi0DrXvK1tSE0eu09AnXjoPF5rJU8iBylHMinkMXFcbKy";
    private UserEntityAdapter adapter;

    @Before
    public void before() {
        this.adapter = new UserEntityAdapter();
    }

    @Test
    public void adaptShouldReturnUserWithPassword() {
        assertThat(adapter.adaptWithPassword(entity()).getHashedPassword().getValue(), is(HASHED_PASSWORD));
    }

    @Test
    public void adaptWithoutPasswordShouldReturnUserWithoutPassword() {
        assertThat(adapter.adapt(entity()).getHashedPassword(), is(SecurePassword.PROTECTED));
    }

    private UserEntity entity() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUsername("rosie");
        entity.setPassword(HASHED_PASSWORD);
        return entity;
    }
}