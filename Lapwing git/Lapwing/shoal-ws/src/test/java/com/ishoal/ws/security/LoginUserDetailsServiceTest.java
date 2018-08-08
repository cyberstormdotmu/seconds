package com.ishoal.ws.security;

import com.ishoal.core.domain.Role;
import com.ishoal.core.domain.Roles;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.UserId;
import com.ishoal.core.security.SecurePassword;
import com.ishoal.core.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;

import static com.ishoal.core.domain.User.aUser;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginUserDetailsServiceTest {

    public static final String USERNAME = "test.user@ishoal.com";
    public static final String PASSWORD = "$2a$10$jbCGLKIuu6rDVX8I7dj64ODvkho8jY8ksQkngiRAhl90w2./nm8fO";

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginUserDetailsService service;

    @Test
    public void shouldReturnAUserThatExists() {
        theRepositoryWillReturnTheUser();
        UserDetails userDetails = service.loadUserByUsername(USERNAME);

        assertThat(userDetails, is(notNullValue()));
    }

    @Test
    public void shouldReturnNullForAUserThatDoesNotExist() {
        theRepositoryWillNotReturnTheUser();
        UserDetails userDetails = service.loadUserByUsername(USERNAME);

        assertThat(userDetails, is(nullValue()));
    }

    @Test
    public void shouldReturnTheCorrectUsername() {
        theRepositoryWillReturnTheUser();
        UserDetails userDetails = service.loadUserByUsername(USERNAME);

        assertThat(userDetails.getUsername(), is(USERNAME));
    }

    @Test
    public void shouldNotChangeThePassword() {
        theRepositoryWillReturnTheUser();
        UserDetails userDetails = service.loadUserByUsername(USERNAME);

        assertThat(userDetails.getPassword(), is(PASSWORD));
    }

    @Test
    public void shouldReturnTheAuthorities() {
        theRepositoryWillReturnTheUser();
        UserDetails userDetails = service.loadUserByUsername(USERNAME);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertThat(authorities.size(), is(1));
    }

    @Test
    public void shouldReturnTheExpectedAuthority() {
        theRepositoryWillReturnTheUser();
        UserDetails userDetails = service.loadUserByUsername(USERNAME);

        SimpleGrantedAuthority[] authorities = new SimpleGrantedAuthority[1];
        authorities = userDetails.getAuthorities().toArray(authorities);
        assertThat(authorities[0].getAuthority(), is("ROLE_BUYER"));
    }

    @Test
    public void genPaswd() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


        String password = "demoShoal2015";

        String encoded = encoder.encode(password);

        System.out.println(password + " -> " + encoded);
    }

    private void theRepositoryWillNotReturnTheUser() {
        when(userService.findByUsernameIgnoreCase(USERNAME)).thenReturn(null);
    }

    private void theRepositoryWillReturnTheUser() {
        when(userService.findByUsernameIgnoreCase(USERNAME)).thenReturn(theUser());
    }

    private User theUser() {
        Roles roles = Roles.someRoles().role(Role.BUYER).build();

        return aUser()
                .id(UserId.from(158L))
                .username(USERNAME)
                .hashedPassword(SecurePassword.fromBCryptHash(PASSWORD))
                .roles(roles)
                .build();
    }
}
