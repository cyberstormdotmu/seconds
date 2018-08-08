package com.ishoal.ws.security;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ishoal.core.domain.Roles;
import com.ishoal.core.domain.User;
import com.ishoal.core.user.UserService;

public class LoginUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserService userService;

    public LoginUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userService.findByUsernameIgnoreCase(username);
        if(user == null || user.getRegistrationToken().matches("PENDING|REJECT")){
            return null;
        }
        return new LoginUserDetails(user.getId().asLong(), user.getUsername(), user.getHashedPassword(),authorities(user.getRoles()));
    }

    private Collection<GrantedAuthority> authorities(Roles roles) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        roles.stream().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        return authorities;
    }
}
