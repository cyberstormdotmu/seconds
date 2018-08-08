package com.ishoal.core.repository;

import com.ishoal.core.config.ShoalCoreTestConfiguration;
import com.ishoal.core.domain.Role;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("/test.properties")
@ContextConfiguration(classes = {ShoalCoreTestConfiguration.class})
@Sql("/sql/clear-down-data.sql")
@Sql("/sql/user-test-data.sql")
@Transactional
public class UserRepositoryTest {

    public static final String USERNAME = "oliver.squires@ishoal.com";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void aNonExistentUserCannotBeFound() {
        User user = userRepository.findByUsernameIgnoreCase("nobody@ishoal.com");
        assertThat(user, is(nullValue()));
    }

    @Test
    public void aUserThatExistsCanBeFound() {
        User user = userRepository.findByUsernameIgnoreCase(USERNAME);
        assertThat(user, is(not(nullValue())));
    }

    @Test
    public void aUserThatExistsCanBeFoundRegardlessOfCase() {
        User user = userRepository.findByUsernameIgnoreCase(USERNAME.toUpperCase());
        assertThat(user, is(not(nullValue())));
    }

    @Test
    public void theUserHasTheCorrectUsername() {
        User user = userRepository.findByUsernameIgnoreCase(USERNAME);
        assertThat(user.getUsername(), is(USERNAME));
    }

    @Test
    public void theUserHasTheCorrectPassword() {
        User user = userRepository.findByUsernameIgnoreCase(USERNAME);
        assertThat(user.getHashedPassword().getValue(), is("$2a$10$nMFDvhatK6bEpLjUX/0bLO/C3fcPDyO8neG/Wck4sRQ2v5CvHi4be"));
    }

    @Test
    public void theUserHasTheCorrectForename() {
        User user = userRepository.findByUsernameIgnoreCase(USERNAME);
        assertThat(user.getForename(), is("Oliver"));
    }

    @Test
    public void theUserHasTheCorrectSurname() {
        User user = userRepository.findByUsernameIgnoreCase(USERNAME);
        assertThat(user.getSurname(), is("Squires"));
    }

    @Test
    public void theUserHasTheCorrectRoles() {
        User user = userRepository.findByUsernameIgnoreCase(USERNAME);
        assertThat(user.getRoles(), containsInAnyOrder(Role.ADMIN, Role.BUYER));
    }

    @Test
    public void shouldReturnAllUsersWithoutARole() {
        Users users = userRepository.findAllUsersWithoutARole();
        assertThat(users.count(), is(1));

        assertThat(users.iterator().next().getUsername(), is("peter@ppb.com"));
    }
}
